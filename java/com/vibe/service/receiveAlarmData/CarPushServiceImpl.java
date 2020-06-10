package com.vibe.service.receiveAlarmData;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.receiveAlarmData.CarPushDao;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.AssetTypeManager;
import com.vibe.monitor.asset.type.ProbeType;
import com.vibe.monitor.drivers.parkingcharge.probe.ParkingLotProbe;
import com.vibe.monitor.result.MonitorResult;
import com.vibe.monitor.server.MonitorServer;
import com.vibe.pojo.parking.CarColorElement;
import com.vibe.pojo.parking.CarParkingStop;
import com.vibe.util.StringUtils;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 分类名称
 * @description 停车管理服务类
 * @author hyd132@126.com
 * @create 2020/04/16
 * @module 智慧园区
 */
@Service(value = "carPushServiceImpl")
@Transactional
public class CarPushServiceImpl implements CarPushService {

    @Autowired
    private AssetStore assetStore;

    @Autowired
    private MonitorServer monitorServer;

//    @Autowired
//    private MonitorService monitorService;

    @Autowired
    private CarPushDao carPushDao;

    /**
     * 推送车位停车信息
     * @param carParkingStop 停车车位信息对象
     */
    @Override
    public ResponseModel<String> getCarParkingStopInfo(CarParkingStop carParkingStop) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long time = new Long(1480428796)*1000L; //1480428796是unix时间戳，
//        String format = sdf.format(new Date(time));
        AssetTypeManager<ProbeType> probeTypes = assetStore.getProbeTypes();
        if(probeTypes.size()>0){
            for (ProbeType probeType : probeTypes) {
                if(probeType.getName().equals("ParkingLotProbe")){
                    Collection<Asset<ProbeType>> assets = assetStore.getAssets(probeType);
                    if(assets != null){
                        for (Asset<ProbeType> asset : assets) {
                            ParkingLotProbe probe = (ParkingLotProbe) asset;
                            if (StringUtils.equals(carParkingStop.getLotName(),probe.getLotName())){
                                carParkingStop.setProbeId(probe.getId());
                                String carColor = carParkingStop.getCarColor();
                                if (null != carColor){
                                    carParkingStop.setCarColor(CarColorElement.getCarColorByNum(carColor));
                                }
//                               CarColorElement.valueOf()
                                //如果停车状态为1 ，表示只有进车时间，没有出车时间
                                if (StringUtils.equals(String.valueOf(carParkingStop.getIsNull()),"1")){
                                    carParkingStop.setInLotTime(sdf.format(new Date(Long.valueOf(carParkingStop.getInLotTime()))));
                                    carParkingStop.setOutLotTime(null);
                                    carPushDao.saveBatchCarParkingStop(carParkingStop);
                                    //如果停车状态为2，表示根据停车名称更新停车时间及停车信息
                                }else if (StringUtils.equals(String.valueOf(carParkingStop.getIsNull()),"2")){
                                    carParkingStop.setOutLotTime(sdf.format(new Date(Long.valueOf(carParkingStop.getOutLotTime()))));
                                    carPushDao.updateParkInfoByParkingStop(carParkingStop);
                                }
                                monitorServer.getResultDispatcher().dispatch(new MonitorResult(probe,1 == carParkingStop.getIsNull()));
                                //进出车时间不同代表是真实数据
//                                if (carParkingStop.getInLotTime() != carParkingStop.getOutLotTime()){


//                                    carParkingStop.setInLotTime(sdf.format(new Date(Long.valueOf(carParkingStop.getInLotTime()))));
//                                    carParkingStop.setOutLotTime(sdf.format(new Date(Long.valueOf(carParkingStop.getOutLotTime()))));
                                    /*if(carPushDao.saveBatchCarParkingStop(carParkingStop)){
                                        System.out.println("推送成功");
                                    }else {
                                        System.out.println("推送失败");
                                    }*/
//                                }
                            }
                        }
                    }
                }
            }
            return ResponseModel.success("推送车位停车信息成功").code(ResultCode.SUCCESS);
        }
        return ResponseModel.success("设备未匹配").code(ResultCode.SUCCESS);
    }

    /**
     * 推送停车场信息统计
     * @param carParkingStop 停车车位信息对象
     */
    @Override
    public ResponseModel<String> getCarParkInfoByCar(CarParkingStop carParkingStop) {
       /* for (Asset<?> asset : assetStore.getAssets()) {
            if (asset instanceof ParkingLotService) {
                ParkingLotService parkingLotService = (ParkingLotService) asset;
                for (Monitor<?> monitor : parkingLotService.getMonitors()) {
                    ParkingLotProbe probe = (ParkingLotProbe) monitor;
                    if (StringUtils.equals(carParkingStop.getLotName(),probe.getLotName())){
                        monitorServer.getResultDispatcher().dispatch(new MonitorResult(probe,1 == carParkingStop.getIsNull()));
                    }
                }
            }
            this.save(carParkingStop);
        }*/
//        carPushDao.insertParkingInfo(carParkingStop);
        CarParkingStop carParking = carPushDao.selectCarParkingByParkId(carParkingStop);
        if (null != carParking){
            carPushDao.updateParkInfoByParkId(carParkingStop);
        }else {
            carPushDao.insertParkingInfo(carParkingStop);
        }
        return ResponseModel.success("推送停车场信息成功").code(ResultCode.SUCCESS);
    }

    /**
     *
     * @param probeId 设备id
     * @return ResponseModel<CarParkingStop> 对象
     */
    @Override
    public ResponseModel<CarParkingStop> queryCarParkingById(Integer probeId) {
        CarParkingStop carParkingStop = carPushDao.selectCarParkingById(probeId);
        return ResponseModel.success(carParkingStop).code(ResultCode.SUCCESS);
//        return carParkingStop;
    }

    /**
     * 过车记录分页查询
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param carParkingStop 停车场对象
     * @return
     */
    @Override
    public Page<CarParkingStop> queryCarParkingListRecord(Integer pageNum, Integer pageSize, CarParkingStop carParkingStop) {
//        Page<CarParkingStop> pageView = new Page<CarParkingStop>();
        //分页工单列表
        PageHelper.startPage(pageNum, pageSize);
        //查询停车记录分页列表
        List<CarParkingStop> carParkingStopList = carPushDao.selectCarParkingListByPage(carParkingStop);
//        com.github.pagehelper.Page page = (com.github.pagehelper.Page) carParkingStopList;
//        pageView.setRows(carParkingStopList);
//        pageView.setPageNum(page.getPageNum());
//        pageView.setPages(page.getPages());
//        pageView.setPageSize(page.getPageSize());
//        pageView.setTotal(page.getTotal());
//        pageView.setSize(taskVOList.size());
        return toPage(carParkingStopList);
    }

    private static <T> Page<T> toPage(List<T> list) {
        PageInfo<T> page = new PageInfo<>(list);
        Page<T> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }
}
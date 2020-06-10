package com.vibe.service.energy.demo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.common.Application;
import com.vibe.common.data.NumberFormat;
import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.energy.EnergyStatisticsMapper;
import com.vibe.mapper.energy.demo.EnergyDemoMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.pojo.MonitorData;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.utils.EasyUIJson;

@Service
public class EnergyDemoImpl implements EnergyDemo {
    private final int CATALOG = 2001;
    private final int DIANTI = 52;
    private final int TOLTALENERGY = 34;

    @Autowired
    private Application application;

    @Autowired
    private AssetStore assetStore;

    /*@Autowired
    private MonitorDataMapper mapper;*/
    @Autowired
    private EnergyStatisticsMapper mapper;
    @Autowired
    private SelectOptionDao selectOptionDao;

    @Autowired
    private EnergyDemoMapper energyDemoMapper;

    //获取当天的每小时变化的数据
    @Override
    public Map<String, Object> queryEnergyDemoListByPage(CommonMonitorDataVo vo, Integer page, Integer rows) {
        vo.setSelectType(2);
        vo.setRedioType(1);
        LocalDate now = LocalDate.now();
        String startTime = now.getYear() + "01-01";
        PageHelper.startPage(page, rows);
        List<CommonMonitorDataVo> valueList = mapper.queryValueList(vo.getMonitorId(), startTime, now.toString(), vo.getTableName(), vo.getSubNum());

        if (valueList != null && valueList.size() > 0) {

            for (CommonMonitorDataVo monitorData : valueList) {
                Date moment = monitorData.getMoment();
                if (moment != null) {
                    String xaxi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(moment).substring(11, 13);
                    monitorData.setName(xaxi);

                }
                Object value = monitorData.getValue();
                if (value == null) {
                    monitorData.setValue(monitorData.getError());
                } else {
                    String unit = monitorData.getUnit();
                    Double dvalue = Double.parseDouble(value.toString());
                    String doubleValue = NumberFormat.formatDoubleTwo(dvalue);
                    monitorData.setValue(doubleValue + (unit == null ? "" : unit));
                }
            }
        }
        PageInfo<CommonMonitorDataVo> pageInfo = new PageInfo<CommonMonitorDataVo>(valueList);
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal(pageInfo.getTotal());
        uiJson.setRows(valueList);

        List<Object> xAxis = new LinkedList<Object>();
        List<Object> values = new LinkedList<Object>();
        for (MonitorData data : valueList) {
            values.add(data.getValue() == null ? "_" : data.getValue());
            // xAxis.add(data.getMoment());
            xAxis.add(data.getName());//时间的字符串
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("xAxis", xAxis);
        map.put("values", values);
        map.put("monitorListData", uiJson);
        map.put("data", getRealMonthValue(vo));
        return map;
    }
    //获取近24小时的总值
	/*public  String getSumValue(Integer monitorId){
		
		 long current=System.currentTimeMillis();//当前时间毫秒数
		 long day=current-1000*3600*24;
		 long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
	     String lastTime = new Timestamp(current).toString();
	     String startTime = new Timestamp(day).toString(); 
	     String newStartTime = new Timestamp(zero).toString();
	     
		String value = mapper.getRecentlyOneDay(monitorId,newStartTime,lastTime);
		
		return value;
	}*/

    //饼图flag=space,catalog
    @Override
    public Map<String, Object> getClassifyValue(Integer id, String flag) {

        List<Object> xAxis = new LinkedList<Object>();
        List<Object> values = new LinkedList<Object>();
        List<Integer> catalogList = new ArrayList<Integer>();

        if ("catalog".equals(flag)) {//id为catalogId
            if (id == DIANTI) {
                //是分类电梯
                setDtValue(xAxis, values, catalogList);
            } else {//照明
                List<Asset<?>> parentlist = getCaildIds(1);
                if (parentlist != null) {
                    for (Asset<?> parent : parentlist) {
                        xAxis.add(parent.getCaption());
                        catalogList.add(parent.getId());
                        //values.add(getSumValue(asset.getId()) == null ? "-" :getSumValue(asset.getId()));
                    }
                }
                List<Integer> spaces = energyDemoMapper.findProbeBySpace(catalogList, id);
                for (Integer monitorId : spaces) {
                    CommonMonitorDataVo vo = new CommonMonitorDataVo();
                    vo.setMonitorId(monitorId);
                    Double itemValue = getRealMonthValue(vo);
                    values.add(itemValue == null ? "_" : NumberFormat.formatDoubleTwo(itemValue));

                }
            }
        } else {
            //空间,id为parentId
            List<CommonSelectOption> selectOptionList = selectOptionDao.querySelectOptionList(TOLTALENERGY, CATALOG);


            if (selectOptionList != null) {
                for (CommonSelectOption option : selectOptionList) {
                    xAxis.add(option.getName());
                    catalogList.add(option.getId());
                }
            }
            if (catalogList != null && catalogList.size() > 0) {
                List<Integer> monitors = energyDemoMapper.findProbeByCatalogs(catalogList);
                for (Integer monitorId : monitors) {
                    CommonMonitorDataVo vo = new CommonMonitorDataVo();
                    vo.setMonitorId(monitorId);
                    Double itemValue = getRealMonthValue(vo);
                    values.add(itemValue == null ? "_" : NumberFormat.formatDoubleTwo(itemValue));
                }
            }


        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("xAxis", xAxis);
        map.put("values", values);
        return map;
    }

    private Double getRealMonthValue(CommonMonitorDataVo vo) {
        vo.setSelectType(2);
        vo.setRedioType(0);
        String tableName = vo.getTableName();
        int subNum = vo.getSubNum();
        String nowTime = LocalDateTime.now().toString().substring(0, subNum);
        Double itemValue = mapper.queryRealTimeTotal(vo.getMonitorId(), tableName, subNum, nowTime);
        return itemValue;
    }

    //获取空间子节点的id
    private List<Asset<?>> getCaildIds(Integer id) {
        List<Asset<?>> list = new ArrayList<Asset<?>>();
        Asset<?> findAsset = assetStore.findAsset(id);
        if (findAsset != null && findAsset.isCompound()) {
            CompoundAsset<?> parentC = (CompoundAsset<?>) findAsset;
            Collection<Asset<?>> children = parentC.children();
            for (Asset<?> asset : children) {
                if (asset != null && AssetKind.SPACE.equals(asset.getKind())) {
                    list.add(asset);
                }
            }
        }
        return list;
    }

    //三维客户端根据不同的id进行染色
    public Map<String, Object> selectValue(Integer id, Integer monitorId) {

        List<Object> values = new LinkedList<Object>();
        List<Integer> catalogList = new ArrayList<Integer>();

        if (id == DIANTI) {
            //是分类电梯,fengzhaung
            setDtValue(null, values, catalogList);

        } else {//照明
            short siteId = application.getSiteId();
            int rootId = (int) siteId << 16;
            List<Asset<?>> parentlist = getCaildIds(rootId);
            if (parentlist != null) {
                for (Asset<?> parent : parentlist) {
                    catalogList.add(parent.getId());
                }
            }
            List<Integer> spaces = energyDemoMapper.findProbeBySpace(catalogList, id);

            if (spaces != null && spaces.size() > 0) {
                // for (Integer monitor : spaces) {
                CommonMonitorDataVo vo = new CommonMonitorDataVo();
                vo.setMonitorId(monitorId);
                Double itemValue = getRealMonthValue(vo);
                values.add(itemValue == null ? "_" : NumberFormat.formatDoubleTwo(itemValue));
                // }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        CommonMonitorDataVo vo = new CommonMonitorDataVo();
        vo.setMonitorId(monitorId);
        map.put("totalValue", getRealMonthValue(vo));
        map.put("values", values);
        return map;
    }

    //封装电梯的分类值
    private void setDtValue(List<Object> xAxis, List<Object> values, List<Integer> catalogList) {
        List<CommonSelectOption> selectOptionList = selectOptionDao.querySelectOptionList(DIANTI, CATALOG);
        if (selectOptionList != null) {
            for (CommonSelectOption option : selectOptionList) {
                if (xAxis != null) {
                    xAxis.add(option.getName());
                }
                catalogList.add(option.getId());
            }
        }
        List<Integer> monitors = energyDemoMapper.findProbeByCatalogs(catalogList);
        for (Integer monitor : monitors) {
            CommonMonitorDataVo vo = new CommonMonitorDataVo();
            vo.setMonitorId(monitor);
            Double itemValue = getRealMonthValue(vo);
            values.add(itemValue == null ? "_" : NumberFormat.formatDoubleTwo(itemValue));
        }
    }


}

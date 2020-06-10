package com.vibe.controller.receiveAlarmData;

import com.vibe.pojo.parking.CarParkingStop;
import com.vibe.service.receiveAlarmData.CarPushService;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类功能名称
 *
 * @Description 中钢停车场推送接口
 * @Author lxx-nice@163.com
 * @Create 2020/04/15
 * @Module 智慧园区
 */
@RestController
@RequestMapping("/timeloit")
public class CarPushController {


    @Autowired
    private CarPushService carPushService;

    /**
     * 获取每个车位的信息包括车位是否停车，停车获取车牌号等信息
     * @param carParkingStop 停车车位信息对象
     */
    @RequestMapping(value = "/getParkinglot",method = RequestMethod.POST)
    public ResponseModel<String> getCarParkingStopInfo(@RequestBody CarParkingStop carParkingStop){
        return carPushService.getCarParkingStopInfo(carParkingStop);
    }

    /**
     * 获取停车场车位的空余车位数和停车车位数,做到实时数据同步
     * @param carParkingStop  停车车位信息对象
     */
    @RequestMapping(value = "/getCarParkInfo")
    public ResponseModel<String> getCarParkInfoByCar(@RequestBody CarParkingStop carParkingStop){
        return carPushService.getCarParkInfoByCar(carParkingStop);
    }

    /**
     * 获取停车场车位通过设备id
     * @param probeId 设备id
     * @return CarParkingStop 对象
     */
    @RequestMapping("/carById")
    public ResponseModel<CarParkingStop> queryCarParkingById(@RequestParam("probeId") Integer probeId){
        return carPushService.queryCarParkingById(probeId);
    }
    /**
     * 停车场车位记录
     */
    @RequestMapping("/carList")
    public ResponseModel<Page<CarParkingStop>> queryCarParkingListRecord(@Param("pageNum") Integer pageNum,
                                                                         @Param("pageSize") Integer pageSize,
                                                                         CarParkingStop carParkingStop){
        return ResponseModel.success(carPushService.queryCarParkingListRecord(pageNum,pageSize,carParkingStop)).code(ResultCode.SUCCESS);
    }
}

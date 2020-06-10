package com.vibe.service.receiveAlarmData;

import com.vibe.pojo.parking.CarParkingStop;
import com.vibe.util.constant.ResponseModel;
import com.vibe.utils.Page;

import java.util.List; /**
 * 分类名称
 * ClassName: CarPushService
 * Package:com.vibe.service.receiveAlarmData
 * Description:
 *
 * @Date:2020/4/16 13:29
 * @Author: hyd132@136.com
 */
public interface CarPushService {

    ResponseModel<String> getCarParkingStopInfo(CarParkingStop carParkingStop);

    ResponseModel<String> getCarParkInfoByCar(CarParkingStop carParkingStop);

    ResponseModel<CarParkingStop> queryCarParkingById(Integer probeId);

    Page<CarParkingStop> queryCarParkingListRecord(Integer pageNum, Integer pageSize, CarParkingStop carParkingStop);
}

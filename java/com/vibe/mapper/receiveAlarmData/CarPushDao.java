package com.vibe.mapper.receiveAlarmData;

import com.vibe.pojo.parking.CarParkingStop;
import com.vibe.util.constant.ResponseModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类名称
 * ClassName: CarPushDao
 * Package:com.vibe.mapper.receiveAlarmData
 * Description:
 *
 * @Date:2020/4/16 13:47
 * @Author: hyd132@136.com
 */
public interface CarPushDao {
    /**
     * 批量添加车位停车信息
     *
     * @param carParkingStop 停车场管理对象
     */
    boolean saveBatchCarParkingStop(CarParkingStop carParkingStop);

    /**
     * 添加停车场信息统计
     *
     * @param carParkingStop 停车场管理对象
     */
    void insertParkingInfo(CarParkingStop carParkingStop);

    /**
     * @param probeId 设备id
     * @return CarParkingStop 停车场管理对象
     */
    CarParkingStop selectCarParkingById(@Param("probeId") Integer probeId);

    /**
     * 过车记录分页查询
     *
     * @param carParkingStop 停车场管理对象
     * @return
     */
    List<CarParkingStop> selectCarParkingListByPage(CarParkingStop carParkingStop);


    /**
     * 通过parkId 查询停车信息
     *
     * @param carParkingStop 停车场管理对象
     * @return
     */
    CarParkingStop selectCarParkingByParkId(CarParkingStop carParkingStop);

    /**
     * 更新停车信息通过parkId
     *
     * @param carParkingStop 停车场管理对象
     */
    void updateParkInfoByParkId(CarParkingStop carParkingStop);

    /**
     * 更新停车信息通过停车名称
     *
     * @param carParkingStop 停车场管理对象
     */
    void updateParkInfoByParkingStop(CarParkingStop carParkingStop);
}

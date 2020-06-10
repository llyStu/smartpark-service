package com.vibe.pojo.parking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 过车记录
 *
 * @Description
 * @Author lxx-nice@163.com
 * @Create 2020/04/01
 * @Module 智慧园区
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarPass {
    /**
     * id
     */
    private int id;
    /**
     * 过车方向
     */
    private String carDirection;
    /**
     * 车辆颜色
     */
    private String carColor;
    /**
     * 车辆进出类型
     */
    private String carInAndOut;
    /**
     * 车牌号
     */
    private String carNum;
    /**
     * 车道号
     */
    private String laneNum;
    /**
     * 卡口相机通道名称
     */
    private String channelName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 抓拍大图
     */
    private String imageBig;
    /**
     * 抓拍小图
     */
    private String imageSmall;
    /**
     * 停车场名称
     */
    private String parkName;

}

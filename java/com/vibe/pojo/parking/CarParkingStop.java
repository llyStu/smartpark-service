package com.vibe.pojo.parking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分类名称
 *
 * @author hyd132@126.com
 * @description 车位停车
 * @create 2020/04/16
 * @module 智慧园区
 */
@Data
public class CarParkingStop {

    //t_parklot_info 表的id
    private Integer id;
    //车场编号
    private String parkId;
    //是否停车  1:车位空 2:有停车
    private Integer isNull;
    //车位名称或编号
    private String lotName;
    //车牌号
    private String carNum;
    //车牌颜色
    private Integer carNumColor;
    //进入车位时间
    private String inLotTime;
    //驶出车位时间
    private String outLotTime;
    // 照相机id
    private String cameraId;
    // 照相机属性
    private String cameraName;
    //车辆类型 1:小型车 2:大型车
    private Integer carType;
    //车辆颜色
    private String carColor;
    //图片存放路径
    private String carImgUrl;
    //设备id
    private Integer probeId;
    //已使用车位数
    private Integer usedLot;
    //剩余车位
    private Integer idleLot;
    //余位屏数
    private Integer esidualScreenNum;

    //车位开始进车时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inLotStartTime;
    //车位结束进车时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inLotEndTime;
    //车位开始驶出时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outLotStartTime;

    //车位结束驶出时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outLotEndTime;
}
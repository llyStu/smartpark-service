package com.vibe.pojo.parking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 *
 * @Description 出入记录
 * @Author lxx-nice@163.com
 * @Create 2020/04/01
 * @Module 智慧园区
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarEntryExit {
    /**
     * id
     */
    private int id;
    /**
     * 车牌号
     */
    private String carNum;
    /**
     * 卡号
     */
    private String cardNum;
    /**
     * 停车状态
     */
    private String parkingState;
    /**
     * 停车类型
     */
    private String parkingType;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 停车场名称
     */
    private String parkName;
    /**
     * 停车收费金额
     */
    private String charge;
    /**
     * 入场卡口相机通道名称
     */
    private String inCameraChannelName;
    /**
     * 出场卡口相机通道名称
     */
    private String outCameraChannelName;
    /**
     * 入场车道号
     */
    private String inChannelNum;
    /**
     * 出场车道号
     */
    private String outChannelNum;
    /**
     * 入场方式
     */
    private String inMode;
    /**
     * 出场方式
     */
    private String outMode;
    /**
     * 入场时间
     */
    private String inTime;
    /**
     * 出场时间
     */
    private String outTime;
    /**
     * 入场道闸通道名称
     */
    private String inChannelName;
    /**
     * 出场道闸通道名称
     */
    private String outChannelName;
    /**
     * 入场抓拍照片（大图）
     */
    private String inImageBig;
    /**
     * 入场抓拍照片（小图）
     */
    private String inImageSmall;
    /**
     * 出场抓拍照片（大图）
     */
    private String outImageBig;
    /**
     * 出场抓拍照片（小图）
     */
    private String outImageSmall;

}

package com.vibe.pojo.parking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 停车场收费
 *
 * @Description
 * @Author lxx-nice@163.com
 * @Create 2020/03/31
 * @Module 智慧园区
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarCharge {
    /**
     * id
     */
    private int id;
    /**
     * 车牌号
     */
    private String carNum;
    /**
     * 进场时间
     */
    private String carInTime;
    /**
     * 了出场时间
     */
    private String carOutTime;
    /**
     * 车辆类型
     */
    private String carType;
    /**
     * 卡号
     */
    private String cardNum;
    /**
     * 收费描述
     */
    private String feeDesc;
    /**
     * 收费规则
     */
    private int feeRule;
    /**
     * 收费金额
     */
    private double feeAmount;
    /**
     * 收费时间
     */
    private String feeTime;
    /**
     * 收费终端
     */
    private String feeTerminal;
    /**
     * 收费类型
     */
    private String feeType;
    /**
     * 入场卡口通道名称
     */
    private String inChannelName;
    /**
     * 出场卡口通道名称
     */
    private String outChannelName;
    /**
     * 优惠金额
     */
    private double discount;
    /**
     * 优惠券编号
     */
    private String coupon;
    /**
     * 优惠类型
     */
    private String discountType;
    /**
     * 实收金额
     */
    private double amountReceived;
    /**
     * 备注
     */
    private String remark;
    /**
     * 收费员
     */
    private String charger;
    /**
     * 车主
     */
    private String carOwner;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 停车时长
     */
    private String timeLength;
    /**
     * 进场抓拍大图
     */
    private String inImageBig;
    /**
     * 进场抓拍小图
     */
    private String inImageSmall;
    /**
     * 出场抓拍大图
     */
    private String outImageBig;
    /**
     * 出场抓拍小图
     */
    private String outImageSmall;

}

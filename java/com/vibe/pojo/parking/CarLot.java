package com.vibe.pojo.parking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 类功能名称
 *
 * @Description 场位统计
 * @Author lxx-nice@163.com
 * @Create 2020/03/31
 * @Module 智慧园区
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarLot{
    /**
     * id
     */
    private int id;
    /**
     * 停车场名称
     */
    private String parkName;
    /**
     * 总位数车
     */
    private int totalLot;
    /**
     * 剩余车位
     */
    private int idleLot;
    /**
     * 车牌个数
     */
    private int carNum;
    /**
     * 入口个数
     */
    private int entranceNum;
    /**
     * 出口个数
     */
    private int exitNum;
    /**
     * 已使用车位
     */
    private int usedLot;
    /**
     * 余位屏数
     */
    private int esidualScreenNum;


}

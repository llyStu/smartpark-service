package com.vibe.pojo;

import java.util.List;

/*
 * Route class for daily check
 * */
public class CheckRoute {

    private int id;
    private String name;//巡检路线名字
    private int orderId;//巡检工单id
    private String memo;//备注
    private List<SingleTask> checkSites;//单个巡检路线中的巡检点列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<SingleTask> getCheckSites() {
        return checkSites;
    }

    public void setCheckSites(List<SingleTask> checkSites) {
        this.checkSites = checkSites;
    }


}

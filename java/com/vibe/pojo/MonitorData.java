package com.vibe.pojo;

import java.util.Date;

public class MonitorData {

    private Object value;

    private String error;

    private Date moment;

    private String unit;

    private String name;

    private Integer id;

    private Object avgData;//监测数据平均值
    private Object maxData;//监测数据最大值
    private Object minData;//监测数据最小值
    private Object stdData;//监测数据均方差
    private String str_moment;//监测数据横坐标


    public String getStr_moment() {
        return str_moment;
    }

    public void setStr_moment(String str_moment) {
        this.str_moment = str_moment;
    }

    public Object getAvgData() {
        return avgData;
    }

    public void setAvgData(Object avgData) {
        this.avgData = avgData;
    }

    public Object getMaxData() {
        return maxData;
    }

    public void setMaxData(Object maxData) {
        this.maxData = maxData;
    }

    public Object getMinData() {
        return minData;
    }

    public void setMinData(Object minData) {
        this.minData = minData;
    }

    public Object getStdData() {
        return stdData;
    }

    public void setStdData(Object stdData) {
        this.stdData = stdData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}

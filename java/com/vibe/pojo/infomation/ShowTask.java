package com.vibe.pojo.infomation;

import java.util.Date;

public class ShowTask {
    public static final String INVALID = "cancel", VALID = "show";
    private Date startTime, endTime;
    private Integer id, interval, equipment;
    private String url, type;

    public ShowTask() {
    }

    public ShowTask(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipment() {
        return equipment;
    }

    public void setEquipment(Integer equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "ShowTask [startTime=" + startTime + ", endTime=" + endTime + ", id=" + id + ", interval=" + interval
                + ", equipment=" + equipment + ", url=" + url + ", type=" + type + "]";
    }
}

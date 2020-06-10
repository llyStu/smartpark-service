package com.vibe.pojo.auty;

import java.util.Arrays;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Auty {
    private Integer id, dutyPeople;
    private String memo, dutyPeopleName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;
    private int[] ids;

    private Integer pageNum, pageSize;

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDutyPeople() {
        return dutyPeople;
    }

    public void setDutyPeople(Integer dutyPeople) {
        this.dutyPeople = dutyPeople;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getDutyPeopleName() {
        return dutyPeopleName;
    }

    public void setDutyPeopleName(String dutyPeopleName) {
        this.dutyPeopleName = dutyPeopleName;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Auty [id=" + id + ", dutyPeople=" + dutyPeople + ", memo=" + memo + ", dutyPeopleName=" + dutyPeopleName
                + ", startTime=" + startTime + ", stopTime=" + stopTime + ", ids=" + Arrays.toString(ids) + "]";
    }
}

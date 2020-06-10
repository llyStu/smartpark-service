package com.vibe.pojo.seawater;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.vibe.pojo.user.User;
import com.vibe.utils.time.TimeCalculate;


public class WorkPeriod {

    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer workTemplateId;
    private String runData;
    private int runState;
    private Integer runFirstLogId;//当条运行工况标记id
    private int falg;
    private User user;
    /**
     * 临时字段
     */
    private String runTime;//运行时长
    private String workName;
    private int workState;

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public int getWorkState() {
        return workState;
    }

    public void setWorkState(int workState) {
        this.workState = workState;
    }

    public String getRunData() {
        return runData;
    }

    public void setRunData(String runData) {
        this.runData = runData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getWorkTemplateId() {
        return workTemplateId;
    }

    public void setWorkTemplateId(Integer workTemplateId) {
        this.workTemplateId = workTemplateId;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        int day = (int) (runTime / (24 * 60 * 60 * 1000));
        int hour = (int) (runTime / (60 * 60 * 1000) - day * 24);
        int min = (int) ((runTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        int s = (int) (runTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        TimeCalculate time = new TimeCalculate(day, hour, min, s);
        this.runTime = time.toString();
    }

    public int getRunState() {
        return runState;
    }

    public void setRunState(int runState) {
        this.runState = runState;
    }

    public Integer getRunFirstLogId() {
        return runFirstLogId;
    }

    public void setRunFirstLogId(Integer runFirstLogId) {
        this.runFirstLogId = runFirstLogId;
    }

    public int getFalg() {
        return falg;
    }

    public void setFalg(int falg) {
        this.falg = falg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}

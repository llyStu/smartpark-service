package com.vibe.pojo;

import com.vibe.pojo.Maintenace;

public class MaintenaceVo extends Maintenace {

    private String typeName;
    private String taskIntervalName;

    public String getTaskIntervalName() {
        return taskIntervalName;
    }

    public void setTaskIntervalName(String taskIntervalName) {
        this.taskIntervalName = taskIntervalName;
    }

    private Integer deviceId;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

}

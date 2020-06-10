package com.vibe.pojo;

public class HandInputData {
    private int id;
    private int monitor;
    private String lookTime;
    private float value;
    private int person;
    private String monitorStr;
    private String parentName;
    private Integer parentId;
    private Integer childId;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    private String childName;


    public String getMonitorStr() {
        return monitorStr;
    }

    public void setMonitorStr(String monitorStr) {
        this.monitorStr = monitorStr;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonitor() {
        return monitor;
    }

    public void setMonitor(int monitor) {
        this.monitor = monitor;
    }

    public String getLookTime() {
        return lookTime;
    }

    public void setLookTime(String lookTime) {
        this.lookTime = lookTime;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

}	

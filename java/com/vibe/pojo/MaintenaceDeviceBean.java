package com.vibe.pojo;

public class MaintenaceDeviceBean {
    private int id;
    private String picture;
    private String maintenance_time;
    private String maintenance_content;
    private int maintenance_type;
    private String remark;
    private int maintenance_person;
    private int device_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMaintenance_time() {
        return maintenance_time;
    }

    public void setMaintenance_time(String maintenance_time) {
        this.maintenance_time = maintenance_time;
    }

    public String getMaintenance_content() {
        return maintenance_content;
    }

    public void setMaintenance_content(String maintenance_content) {
        this.maintenance_content = maintenance_content;
    }

    public int getMaintenance_type() {
        return maintenance_type;
    }

    public void setMaintenance_type(int maintenance_type) {
        this.maintenance_type = maintenance_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMaintenance_person() {
        return maintenance_person;
    }

    public void setMaintenance_person(int maintenance_person) {
        this.maintenance_person = maintenance_person;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }


}

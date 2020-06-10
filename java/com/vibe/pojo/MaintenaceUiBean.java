package com.vibe.pojo;

public class MaintenaceUiBean {
    private int id;
    private String picture;
    private String maintenance_time;
    private String maintenance_content;
    private int maintenance_type;
    private String remark;
    private int maintenance_person;
    private String maintenance_type_str;
    private String maintenance_person_str;

    public String getMaintenance_type_str() {
        return maintenance_type_str;
    }

    public void setMaintenance_type_str(String maintenance_type_str) {
        this.maintenance_type_str = maintenance_type_str;
    }

    public String getMaintenance_person_str() {
        return maintenance_person_str;
    }

    public void setMaintenance_person_str(String maintenance_person_str) {
        this.maintenance_person_str = maintenance_person_str;
    }

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


}

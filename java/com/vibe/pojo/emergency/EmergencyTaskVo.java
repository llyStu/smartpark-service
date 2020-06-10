package com.vibe.pojo.emergency;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class EmergencyTaskVo {
    private Integer etid;
    private String uname, number, name, type, location;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modified_min;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modified_max;
    private Integer pageNum = 1, pageSize = 10;

    public Integer getEtid() {
        return etid;
    }

    public void setEtid(Integer etid) {
        this.etid = etid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getModified_min() {
        return modified_min;
    }

    public void setModified_min(Date modified_min) {
        this.modified_min = modified_min;
    }

    public Date getModified_max() {
        return modified_max;
    }

    public void setModified_max(Date modified_max) {
        this.modified_max = modified_max;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}

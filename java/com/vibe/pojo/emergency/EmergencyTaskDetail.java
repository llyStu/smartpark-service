package com.vibe.pojo.emergency;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class EmergencyTaskDetail {
    private Integer tdid, etid, uid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date execution;
    private String location, resource, description, uname;

    public Integer getTdid() {
        return tdid;
    }

    public void setTdid(Integer tdid) {
        this.tdid = tdid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getExecution() {
        return execution;
    }

    public void setExecution(Date execution) {
        this.execution = execution;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

}

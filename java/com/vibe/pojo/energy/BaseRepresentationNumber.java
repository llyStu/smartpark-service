package com.vibe.pojo.energy;

import java.sql.Timestamp;

import javax.management.loading.PrivateClassLoader;

import org.snmp4j.security.PrivAES;

public class BaseRepresentationNumber {

    private Integer xulie;
    private Timestamp time;
    private String name;
    private double value;
    private String caption;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getXulie() {
        return xulie;
    }

    public void setXulie(Integer xulie) {
        this.xulie = xulie;
    }

}

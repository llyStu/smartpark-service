package com.vibe.pojo.energy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vibe.pojo.Data;

public class Energy {
    private String name;//水电类别名称
    private String unit;//单位
    private Object value;//数值
    private String floor; //楼层
    private int parentId; //空间父子级关联
    private Timestamp time;
    private String zhanbi; //比例
    private int xulie;
    private int floorId;
    private int nameId;//水电类别名称id
    private List<Energy> nodes = new ArrayList<>();

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public int getXulie() {
        return xulie;
    }

    public void setXulie(int xulie) {
        this.xulie = xulie;
    }

    public String getZhanbi() {
        return zhanbi;
    }

    public void setZhanbi(String zhanbi) {
        this.zhanbi = zhanbi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<Energy> getNodes() {
        return nodes;
    }

    public void setNodes(List<Energy> nodes) {
        this.nodes = nodes;
    }

    public Energy() {

    }

    public Energy(String name, String unit, double value, String zhanbi) {
        this.name = name;
        this.unit = unit;
        this.value = value;
        this.zhanbi = zhanbi;
    }

}

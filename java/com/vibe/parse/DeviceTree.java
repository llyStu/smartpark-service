package com.vibe.parse;

import java.util.List;

/**
 * 分类名称
 * @description 设备树父子级关联对象
 * @author hyd132@126.com
 * @create 2020/05/27
 * @module 智慧园区
 */
public class DeviceTree{

    private Integer id;
    private Integer parent;
    private String name;
    private String type;
    private String caption;
    private String catalog;

    private List<DeviceTree> deviceTreeList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public List<DeviceTree> getDeviceTreeList() {
        return deviceTreeList;
    }

    public void setDeviceTreeList(List<DeviceTree> deviceTreeList) {
        this.deviceTreeList = deviceTreeList;
    }
}
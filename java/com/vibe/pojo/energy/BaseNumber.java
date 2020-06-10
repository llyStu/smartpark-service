package com.vibe.pojo.energy;

/**
 * 类功能名称
 *
 * @Description 导出每月最后数据
 * @Author lxx-nice@163.com
 * @Create 2020/04/23
 * @Module 智慧园区
 */
public class BaseNumber {
    private String year;
    private String month;
    private String nameId;
    private String name;
    private Object value;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

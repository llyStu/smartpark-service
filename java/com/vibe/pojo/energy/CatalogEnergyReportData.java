package com.vibe.pojo.energy;

import java.util.Date;

public class CatalogEnergyReportData {
    private Integer catalog;
    private String name, unit;
    private Date moment;
    private Float inc;

    public Integer getCatalog() {
        return catalog;
    }

    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
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

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public Float getInc() {
        return inc;
    }

    public void setInc(Float inc) {
        this.inc = inc;
    }
}

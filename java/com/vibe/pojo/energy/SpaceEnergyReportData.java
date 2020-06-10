package com.vibe.pojo.energy;

import java.util.Date;

public class SpaceEnergyReportData {
    private Integer spaceId;
    private String spaceType, spaceName, spaceCaption, probeCaption, probeUnit;
    private Date moment;
    private Float inc, unitArea;

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

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSpaceCaption() {
        return spaceCaption;
    }

    public void setSpaceCaption(String spaceCaption) {
        this.spaceCaption = spaceCaption;
    }

    public String getProbeCaption() {
        return probeCaption;
    }

    public void setProbeCaption(String probeCaption) {
        this.probeCaption = probeCaption;
    }

    public String getProbeUnit() {
        return probeUnit;
    }

    public void setProbeUnit(String probeUnit) {
        this.probeUnit = probeUnit;
    }

    public Float getUnitArea() {
        return unitArea;
    }

    public void setUnitArea(Float unitArea) {
        this.unitArea = unitArea;
    }
}

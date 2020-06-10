package com.vibe.pojo.energy;

public class EnergyData {

    private String spaceName;
    private Integer probeId;
    private String dateTime;
    private String value;
    private Object yoy; //同比
    private Integer spaceId;
    private String unit;

    public EnergyData() {
        super();
        // TODO Auto-generated constructor stub
    }


    public Object getYoy() {
        return yoy;
    }

    public void setYoy(Object yoy) {
        this.yoy = yoy;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public Integer getProbeId() {
        return probeId;
    }

    public void setProbeId(Integer probeId) {
        this.probeId = probeId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


}

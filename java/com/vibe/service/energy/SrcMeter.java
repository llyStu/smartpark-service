package com.vibe.service.energy;

public class SrcMeter {
    private String parent;
    private String catalog;
    private String type;
    private String source;
    private String parent_meter;
    private String grade;
    private String meterType;
    private String itemizeType;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getParent_meter() {
        return parent_meter;
    }

    public void setParent_meter(String parent_meter) {
        this.parent_meter = parent_meter;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getItemizeType() {
        return itemizeType;
    }

    public void setItemizeType(String itemizeType) {
        this.itemizeType = itemizeType;
    }

    @Override
    public String toString() {
        return "SrcMeter [parent=" + parent + ", catalog=" + catalog + ", type=" + type + ", source=" + source
                + ", parent_meter=" + parent_meter + ", grade=" + grade + ", meterType=" + meterType + ", itemizeType="
                + itemizeType + "]";
    }

}

package com.vibe.parse;

public class IEC104Monitor extends BaseMonitorBean {

    private String address;
    private String commonAddr;
    private String parent_meter;
    private String grade;
    private String meter_type;
    private String itemize_type;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommonAddr() {
        return commonAddr;
    }

    public void setCommonAddr(String commonAddr) {
        this.commonAddr = commonAddr;
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

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getItemize_type() {
        return itemize_type;
    }

    public void setItemize_type(String itemize_type) {
        this.itemize_type = itemize_type;
    }

    @Override
    public String getAiType() {
        // TODO Auto-generated method stub
        return App.IEC104_AI_TYPE;
    }

    @Override
    public String getAoType() {
        // TODO Auto-generated method stub
        return App.IEC104_AO_TYPE;
    }

    @Override
    public String getDiType() {
        // TODO Auto-generated method stub
        return App.IEC104_DI_TYPE;
    }

    @Override
    public String getDoType() {
        // TODO Auto-generated method stub
        return App.IEC104_DO_TYPE;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        if (App.TEST) {
            return super.toString();
        } else {
            return super.toString() + "insert into t_asset_prop(asset, name, value) values (" + getId()
                    + ", \"Address\", \"" + getAddress()
                    + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"CommonAddr\", \""
                    + getCommonAddr() + "\");\ninsert into t_probe_monitor(probe_id,parent_meter,grade,meter_type,itemize_type) values ("
                    + getId() + "," + getParent_meter() + "," + getGrade() + "," + getMeter_type() + "," + getItemize_type() + ");";
        }
    }

}

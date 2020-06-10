package com.vibe.service.receiveAlarmData;

public class TimeloitAlarmData {
    //设备编号
    private String deviceCode;
    //网关编号
    private String gatewayCode;

    //报警类型 0：恢复 1：报警
    private int type;
    //报警信息
    private String alarmInfo;
    //报警值
    private String val;
    //如果是modbus协议的设备需要加地址确定是唯一的。
    private String address;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    @Override
    public String toString() {
        return "TimeloitAlarmData [deviceCode=" + deviceCode + ", gatewayCode=" + gatewayCode + ", type=" + type
                + ", alarmInfo=" + alarmInfo + ", val=" + val + ", address=" + address + "]";
    }


}

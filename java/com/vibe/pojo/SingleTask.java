package com.vibe.pojo;

import java.util.List;

/*
 * Single task class for daily check, record of check sites
 * */
public class SingleTask {

    private int id;
    private int siteName;//巡检点名称
    private int routeId;//巡检路线id
    private int sequence;//巡检点出现次序
    private String memo;//备注
    private List<SingleProp> props;//单个巡检点的巡检项目
    private String qrCode;//设备上的二维码信息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSiteName() {
        return siteName;
    }

    public void setSiteName(int siteName) {
        this.siteName = siteName;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<SingleProp> getProps() {
        return props;
    }

    public void setProps(List<SingleProp> props) {
        this.props = props;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


}

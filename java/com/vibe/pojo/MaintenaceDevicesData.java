package com.vibe.pojo;

import java.util.List;

public class MaintenaceDevicesData {
    private MaintenaceBean maintenaceBean;

    private List<Integer> deviceIds;


    public MaintenaceBean getMaintenaceBean() {
        return maintenaceBean;
    }

    public void setMaintenaceBean(MaintenaceBean maintenaceBean) {
        this.maintenaceBean = maintenaceBean;
    }

    public List<Integer> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Integer> deviceIds) {
        this.deviceIds = deviceIds;
    }


}

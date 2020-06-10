package com.vibe.pojo;

import java.util.List;

public class MaintenaceDevicesBean {
    private MaintenaceUiBean maintenaceBean;
    private List<SimpleDeviceForUi> deviceForUis;

    public MaintenaceUiBean getMaintenaceBean() {
        return maintenaceBean;
    }

    public void setMaintenaceBean(MaintenaceUiBean maintenaceBean) {
        this.maintenaceBean = maintenaceBean;
    }

    public List<SimpleDeviceForUi> getDeviceForUis() {
        return deviceForUis;
    }

    public void setDeviceForUis(List<SimpleDeviceForUi> deviceForUis) {
        this.deviceForUis = deviceForUis;
    }


}

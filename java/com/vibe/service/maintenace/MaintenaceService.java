package com.vibe.service.maintenace;


import com.vibe.pojo.MaintenaceDevicesBean;
import com.vibe.pojo.MaintenaceDevicesData;
import com.vibe.pojo.PageResult;

public interface MaintenaceService {

    public PageResult<MaintenaceDevicesBean> queryMaintenaceByDeviceId(int deviceId);

    public void addMaintenace(MaintenaceDevicesData maintenaceDevicesData);

    public void updateMaintenace(MaintenaceDevicesData maintenaceDevicesData);

    public void deleteMaintenace(int parseInt);

}

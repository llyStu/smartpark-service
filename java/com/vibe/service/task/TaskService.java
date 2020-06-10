package com.vibe.service.task;


import java.util.List;

import com.vibe.scheduledtasks.CommonTaskBean;
import com.vibe.scheduledtasks.ScheduledTaskBean;

public interface TaskService {

    public List<ScheduledTaskBean> queryScheduledTasks(ScheduledTaskBean taskBean);

    public void updateScheduledTask(ScheduledTaskBean taskBean);

    public void insertScheduledTask(ScheduledTaskBean taskBean);

    public void deleteScheduledTask(int id);

    public List<CommonTaskBean> queryTasks(CommonTaskBean taskBean);

    public void insertTask(CommonTaskBean taskBean);

    public void updateTask(CommonTaskBean taskBean);

    /*public  List<MaintenaceDevicesBean> queryMaintenaceByDeviceId(int deviceId);
    public void addMaintenace(MaintenaceDevicesData maintenaceDevicesData);
    public void updateMaintenace(MaintenaceDevicesData maintenaceDevicesData);
    public void deleteMaintenace(int parseInt);*/
    public void deleteTask(int id);

}

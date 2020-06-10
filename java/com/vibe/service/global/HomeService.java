package com.vibe.service.global;

import com.vibe.pojo.*;

import java.util.List;
import java.util.Map;

public interface HomeService {

    public List<HomeBean> getHomes();

    public List<HomeBean> getUserHomes(int userId);

    public void updateHomes(int userId, List<Integer> userHomes);

    public String homeInterfaceMonitor(int homeId);

    public String homeInterfaceAlarm(int homeId);

    public String homeInterfaceFault(int homeId);

    public String homeInterfaceTask(int homeId);

    public String homeInterfaceEnvironment(int homeId);

    public Map<Object, Object> homeInterfaceAsset(String codeName, int type);

    public int alarmCount();

    public Map<Object, Object> allInterface(String url);

    public List<HomeCamera> homeCamera(String codeName);

    public Map<Object, Object> allDeviceStateData();

    public Map<Short, XiaofangCount> xiaofangCountData();

    AlarmModuleAll homeInterfaceAlarmByModule(String modules);

    List<AlarmModule> homeInterfaceAlarmById(String ids);

    int countAlarm();

    List<AlarmModule> countAlarmByCatalogs(String catalog);

    Map<String, Integer> countMonitorByCatalogs(String catalog);

    Map<String, Object> deviceTypeAlarmProportion();

    Map<String, List<Object>> countMonitorStatus(String ids);

    Map<String, Integer> homeAssetHealth(Integer spaceId, Integer kind);

    Map<String, Object> getEnergyModule(EnergyModule energyModule);
}

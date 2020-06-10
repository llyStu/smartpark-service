package com.vibe.mapper.maintenace;

import com.vibe.pojo.MaintenaceBean;
import com.vibe.pojo.MaintenaceDeviceBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaintenaceDao {


    public List<MaintenaceDeviceBean> queryMaintenaceByDeviceId(int deviceId);

    public void addMaintenace(MaintenaceBean maintenaceBean);

    public void addDeviceMaintenace(@Param("maintenaceId") int maintenaceId, @Param("deviceId") int deviceId);

    public void updateMaintenace(MaintenaceBean maintenaceBean);

    public List<Integer> queryDeviceIdsByMaintenaceId(@Param("maintenaceId") int maintenaceId);

    public void deleteDeviceMaintenace(@Param("maintenaceId") int maintenaceId, @Param("deviceId") int deviceId);

    public void updateDeviceMaintenace(@Param("maintenaceId") int maintenaceId, @Param("deviceId") int deviceId);

    public void deleteMaintenace(@Param("id") int id);
}

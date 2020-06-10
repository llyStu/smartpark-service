package com.vibe.service.maintenace;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.vibe.mapper.maintenace.MaintenaceDao;
import com.vibe.mapper.task.TaskDao;
import com.vibe.mapper.user.UserMapper;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.pojo.MaintenaceDeviceBean;
import com.vibe.pojo.MaintenaceDevicesBean;
import com.vibe.pojo.MaintenaceDevicesData;
import com.vibe.pojo.MaintenaceUiBean;
import com.vibe.pojo.PageResult;
import com.vibe.pojo.SimpleDeviceForUi;
import com.vibe.scheduledtasks.CommonTaskBean;
import com.vibe.util.MaintenaceTypeUtil;

@Service
public class MaintenaceServiceimpl implements MaintenaceService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MaintenaceDao maintenaceDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private AssetStore assetStore;


    @Override
    public PageResult<MaintenaceDevicesBean> queryMaintenaceByDeviceId(int deviceId) {
        List<MaintenaceDeviceBean> maintenaceDeviceBeans = maintenaceDao.queryMaintenaceByDeviceId(deviceId);
        List<MaintenaceDevicesBean> maintenaceDevicesBeans = new ArrayList<MaintenaceDevicesBean>();
        if (maintenaceDeviceBeans != null) {
            for (MaintenaceDeviceBean maintenaceDeviceBean : maintenaceDeviceBeans) {
                MaintenaceDevicesBean maintenaceDevicesBean = new MaintenaceDevicesBean();
                MaintenaceUiBean maintenaceBean = new MaintenaceUiBean();
                maintenaceBean.setId(maintenaceDeviceBean.getId());
                maintenaceBean.setMaintenance_content(maintenaceDeviceBean.getMaintenance_content());
                maintenaceBean.setMaintenance_person(maintenaceDeviceBean.getMaintenance_person());
                maintenaceBean.setMaintenance_time(maintenaceDeviceBean.getMaintenance_time());
                maintenaceBean.setMaintenance_type(maintenaceDeviceBean.getMaintenance_type());
                maintenaceBean.setPicture(maintenaceDeviceBean.getPicture());
                maintenaceBean.setRemark(maintenaceDeviceBean.getRemark());
                maintenaceBean.setMaintenance_person_str(userMapper.queryUserById(maintenaceDeviceBean.getMaintenance_person()).getName());
                maintenaceBean.setMaintenance_type_str(MaintenaceTypeUtil.getTypeStr(maintenaceDeviceBean.getMaintenance_type()));
                maintenaceDevicesBean.setMaintenaceBean(maintenaceBean);
                List<SimpleDeviceForUi> deviceForUis = new ArrayList<>();
                SimpleDeviceForUi deviceForUi = new SimpleDeviceForUi();
                deviceForUi.setDeviceId(maintenaceDeviceBean.getDevice_id());
                deviceForUi.setDeviceFullName(assetStore.getAssetFullName(assetStore.findAsset(maintenaceDeviceBean.getDevice_id()), AssetStore.LOCATION_SEPARATOR));
                deviceForUis.add(deviceForUi);
                maintenaceDevicesBean.setDeviceForUis(deviceForUis);
                maintenaceDevicesBeans.add(maintenaceDevicesBean);
            }
        }
        PageResult<MaintenaceDevicesBean> maintenaceData = new PageResult<MaintenaceDevicesBean>();
        maintenaceData.setData(maintenaceDevicesBeans);
        maintenaceData.setTotal(((Page<MaintenaceDeviceBean>) maintenaceDeviceBeans).getTotal());
        return maintenaceData;
    }

    /**
     * 新增设备维护记录
     */
    @Override
    public void addMaintenace(MaintenaceDevicesData maintenaceDevicesData) {
        maintenaceDao.addMaintenace(maintenaceDevicesData.getMaintenaceBean());
        if (maintenaceDevicesData.getDeviceIds() != null) {
            for (int deviceId : maintenaceDevicesData.getDeviceIds()) {
                maintenaceDao.addDeviceMaintenace(maintenaceDevicesData.getMaintenaceBean().getId(), deviceId);
            }
        }

    }

    @Override
    public void updateMaintenace(MaintenaceDevicesData maintenaceDevicesData) {
        maintenaceDao.updateMaintenace(maintenaceDevicesData.getMaintenaceBean());
        if (maintenaceDevicesData.getDeviceIds() != null) {
            List<Integer> oldDeviceIds = maintenaceDao.queryDeviceIdsByMaintenaceId(maintenaceDevicesData.getMaintenaceBean().getId());
            if (oldDeviceIds != null) {
                if (maintenaceDevicesData.getDeviceIds() != null) {
                    for (int oldDeviceId : oldDeviceIds) {
                        boolean deleteFlag = true;
                        for (int newDeviceId : maintenaceDevicesData.getDeviceIds()) {
                            if (newDeviceId == oldDeviceId) {
                                deleteFlag = false;
                                break;
                            }
                        }
                        if (deleteFlag) {
                            maintenaceDao.deleteDeviceMaintenace(maintenaceDevicesData.getMaintenaceBean().getId(),
                                    oldDeviceId);
                        }
                    }
                    for (int newDeviceId : maintenaceDevicesData.getDeviceIds()) {
                        boolean addFlag = true;
                        for (int oldDeviceId : oldDeviceIds) {
                            if (oldDeviceId == newDeviceId) {
                                addFlag = false;
                                break;
                            }
                        }
                        if (addFlag) {
                            maintenaceDao.addDeviceMaintenace(maintenaceDevicesData.getMaintenaceBean().getId(), newDeviceId);
                        }
                    }
                } else {
                    for (int oldDeviceId : oldDeviceIds) {
                        maintenaceDao.deleteDeviceMaintenace(maintenaceDevicesData.getMaintenaceBean().getId(), oldDeviceId);
                    }
                }
            } else {
                if (maintenaceDevicesData.getDeviceIds() != null) {
                    for (int deviceId : maintenaceDevicesData.getDeviceIds()) {
                        maintenaceDao.addDeviceMaintenace(maintenaceDevicesData.getMaintenaceBean().getId(), deviceId);
                    }
                }
            }
        }
    }

    /*
     * 删除记录
     */
    @Override
    public void deleteMaintenace(int id) {
        maintenaceDao.deleteMaintenace(id);
        CommonTaskBean commonTaskBeanTemp = new CommonTaskBean();
        commonTaskBeanTemp.setTaskId(id);
        commonTaskBeanTemp.setType(2);            //2代表设备维护任务，该定义在来源于数据库
        taskDao.deleteTask(commonTaskBeanTemp);
    }

}

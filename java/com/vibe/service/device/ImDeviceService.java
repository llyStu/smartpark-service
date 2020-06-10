package com.vibe.service.device;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.vibe.pojo.AssetVo;
import com.vibe.pojo.user.Department;
import com.vibe.pojo.user.User;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Space;

public interface ImDeviceService {
    public List<User> loadUser();

    public List<Department> loadDept();

    public List<AssetVo> loadSpace(AssetVo assetVo);

    //保存exl表中，新建部门信息
    public void saveDepttoDB(Department dept);

    public void saveUsertoDB(User user);

    public void saveSpacetoDB(Space space2);

    public void saveDevice(Device device);

    public void saveDeviceList(Workbook workbook) throws AssetException;
}

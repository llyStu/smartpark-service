package com.vibe.mapper.global;

import com.vibe.monitor.asset.Device;
import com.vibe.pojo.AssetVo;
import com.vibe.service.global.navigation.Navigat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NavigationMapper {
    public List<Navigat> queryCodeLocationByCode(@Param("menu") Integer menu);

    public List<AssetVo> querySpaceTreeData(@Param("id") int id);

    public List<Navigat> queryCodeLocationList();

    public List<Device> queryElevatorList(@Param("catalog") int catalog);
}

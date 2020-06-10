package com.vibe.service.energy;

import java.util.List;
import java.util.Map;

import com.vibe.monitor.asset.Probe;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.energy.Curves;

public interface EnergyRealTimeService {


    //实时监测-总
    List<Curves> getRealTimeTotal(CommonMonitorDataVo vo);

    //实时监测-建筑
    List<Map<String, Object>> getRealTimeBuilding(Integer parentId, CommonMonitorDataVo vo);

    //综合能耗
    Map<String, Object> getSynthesize(CommonMonitorDataVo vo);

    //工具
    String getNameByCatalog(Integer catalog, Integer code);

    String getUnitByCatalog(Integer catalog, Integer code);

    List<Integer> getCatalogIds(List<CommonSelectOption> workItems);

    List<Probe> getSpaceTotalEnergyProbe(int parentSpace, List<Integer> energyCatalogIdList);

}

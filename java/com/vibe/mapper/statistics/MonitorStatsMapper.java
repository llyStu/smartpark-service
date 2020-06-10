package com.vibe.mapper.statistics;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public abstract interface MonitorStatsMapper {
    public abstract List<Map<String, Object>> statsProbeCatalogCount(@Param("catalog") int catalog);

    public abstract List<Map<String, Object>> statsDeviceEnabledYears(@Param("siteId") int paramInt);

    public abstract List<Map<String, Object>> queryAllProbe(short siteId);

    public abstract List<Map<String, Object>> queryByType(@Param("name") String name, @Param("code_catalog") int code_catalog);

    //public abstract List<Map<String, Object>> queryByType(@Param("name") String name);

}

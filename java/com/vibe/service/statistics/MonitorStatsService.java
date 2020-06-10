package com.vibe.service.statistics;

import java.util.List;
import java.util.Map;

public interface MonitorStatsService {

	Map<String, List<Object>> getStateData();

	Map<String, List<Object>> getMonitorCount();

	Map<String, List<Object>> getDeviceEnabledYears();

	Map<String, List<Object>> queryAllProbe();
	
	Map<String,List<Object>> getInfoByMonitorState(String filter, int index, String name);

}

package com.vibe.mapper.statistics;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.vibe.pojo.MonitorData;

public interface MonitorDataMapper {

	
	List<MonitorData> loadRecently(
			@Param("tableName") String tableName,
			@Param("monitorId") int monitorId,
			@Param("startTime") LocalDateTime startTime,
			@Param("lastTime") LocalDateTime lastTime);
	List<MonitorData> loadRecently_hour(
			@Param("monitorId") int monitorId,
			@Param("startTime") String startTime,
			@Param("lastTime") String lastTime);
	List<MonitorData> loadRecentlyByYear(@Param("monitorId") int monitorId,
			@Param("startTime2") String startTime2, @Param("lastTime2") String lastTime2);

	List<MonitorData> loadRecentlyByDay(@Param("monitorId") int monitorId, @Param("startTime2") String startTime2,
			@Param("lastTime2") String lastTime2);

	List<MonitorData> loadRecentlyByMonth(@Param("monitorId") int monitorId, @Param("startTime2") String startTime2,
			@Param("lastTime2") String lastTime2);

	public abstract List<Map<String, Object>> queryByType(@Param("catalog") int catalog);
	
	String getRecentlyOneDay(
			@Param("monitorId") int monitorId,
			@Param("startTime") String startTime,
			@Param("lastTime") String lastTime);
	
	String getUnit(@Param("monitorId") int monitorId);
	//获取单个监测器自定义时间段监测值
	List<MonitorData> loadRecently_str(@Param("monitorId") int monitorId, 
			@Param("startTime") String startTime, 
			@Param("lastTime") String lastTime);

	List<MonitorData> queryIntMonitorValues(@Param("monitorId") int monitorId, @Param("startTime") String startTime,
			@Param("lastTime") String lastTime);
	List<MonitorData> queryBoolMonitorValues(@Param("monitorId") int monitorId, @Param("startTime") String startTime,
			@Param("lastTime") String lastTime);
	List<MonitorData> queryFloatMonitorValues(@Param("monitorId") int monitorId, @Param("startTime") String startTime,
			@Param("lastTime") String lastTime);

	List<Object> getEnvironmentAvgByCodeDay(@Param("code") Integer code, @Param("date") String date);

	List<Map<String , Object>> getMonitorCodeName();
}

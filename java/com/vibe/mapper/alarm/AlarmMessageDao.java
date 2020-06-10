package com.vibe.mapper.alarm;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.vibe.monitor.alarm.AlarmRule;
import com.vibe.pojo.AlarmMessage;

@Repository
public interface AlarmMessageDao {

	public List<AlarmMessage> queryAlarmMessageListByTime(@Param("pageNum") int pageNum,@Param("pageCount") int pageCount,@Param("start") String start,@Param("end") String end);

	public void handleAlarmMessage(@Param("id") int id);

	public AlarmRule getAlarmRuleByAssetId(@Param("id") int id);

	public void insertAlarmRule(@Param("assetId") int id,@Param("rule") int rule,@Param("way") int way,@Param("start") int start);
	public void updateAlarmRule(@Param("assetId") int id,@Param("rule") int rule,@Param("way") int way,@Param("start") int start);

	public int insertPersonAlarmLog(AlarmMessage alarmMessage);

	public int insertPersonAlarmMessage(@Param("logId") int logId,@Param("caption") String caption);

	public List<AlarmMessage> queryAllAlarmMessageListByTime(@Param("start") String start,@Param("end") String end, @Param("handled")int handled);

	public List<AlarmMessage> queryAlarmMessageListByAssetId(@Param("pageNum") int pageNum,@Param("pageCount") int pageCount,@Param("assetId") int assetId);

	public int alarmCount();

	public List<AlarmMessage> queryAlarmMessages(@Param("alarmMessage")AlarmMessage alarmMessage);
}

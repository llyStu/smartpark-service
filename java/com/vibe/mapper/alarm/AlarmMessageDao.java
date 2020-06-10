package com.vibe.mapper.alarm;

import com.vibe.monitor.alarm.AlarmRule;
import com.vibe.pojo.AlarmMessage;
import com.vibe.pojo.AvgNum;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmMessageDao {

    public List<AlarmMessage> queryAlarmMessageListByTime(@Param("pageNum") int pageNum, @Param("pageCount") int pageCount, @Param("start") String start, @Param("end") String end);

    public void handleAlarmMessage(@Param("id") int id);

    public AlarmRule getAlarmRuleByAssetId(@Param("id") int id);

    public void insertAlarmRule(@Param("assetId") int id, @Param("rule") int rule, @Param("way") int way, @Param("start") int start);

    public void updateAlarmRule(@Param("assetId") int id, @Param("rule") int rule, @Param("way") int way, @Param("start") int start);

    public int insertPersonAlarmLog(AlarmMessage alarmMessage);

    public int insertPersonAlarmMessage(@Param("logId") int logId, @Param("caption") String caption);

    public List<AlarmMessage> queryAllAlarmMessageListByTime(@Param("start") String start, @Param("end") String end, @Param("handled") int handled);

    public List<AlarmMessage> queryAlarmMessageListByAssetId(@Param("pageNum") int pageNum, @Param("pageCount") int pageCount, @Param("assetId") int assetId);

    public int alarmCount();

    List<AvgNum> getAlarmByTimeState(@Param("time") String time, @Param("state") Integer state);
}

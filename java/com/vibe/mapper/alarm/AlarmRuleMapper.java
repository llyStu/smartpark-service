package com.vibe.mapper.alarm;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.vibe.pojo.event.AlarmRuleMessage;
import com.vibe.pojo.event.EventRank;


@Repository
public interface AlarmRuleMapper {

	List<AlarmRuleMessage> queryAlarmRules(@Param("alarmRule")AlarmRuleMessage alarmRule);


	List<EventRank> queryEventRanks(@Param("rankId")Integer rankId);


	int updateAssetEventRank(@Param("ids")Integer[] ids, @Param("rankId")Integer rankId);


	int deleteEventRank(@Param("id")Integer id);


}

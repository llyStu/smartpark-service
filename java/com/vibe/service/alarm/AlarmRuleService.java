package com.vibe.service.alarm;


import java.util.List;

import com.vibe.pojo.event.AlarmRuleMessage;
import com.vibe.pojo.event.EventRank;
import com.vibe.pojo.event.RuleRankRelation;
import com.vibe.util.constant.ResponseModel;
import com.vibe.utils.EasyUIJson;

public interface AlarmRuleService {

    EasyUIJson queryAlarmRules(Integer page, Integer rows, AlarmRuleMessage alarmRule);

    List<EventRank> queryEventRanks(Integer rankId);

    ResponseModel<String> updateAssetEventRank(Integer[] ids, Integer rankId);

    ResponseModel<String> deleteEventRank(List<RuleRankRelation> ruleRankRelations);

}

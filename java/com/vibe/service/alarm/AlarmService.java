package com.vibe.service.alarm;

import java.util.List;

import com.vibe.pojo.AlarmMessage;
import com.vibe.pojo.InsertUpdateAlarmRuleData;
import com.vibe.pojo.InsertUpdateAlarmRuleItem;
import com.vibe.pojo.TreeAlarmData;
import com.vibe.util.constant.ResponseModel;
import com.vibe.utils.EasyUIJson;

public interface AlarmService {
    public EasyUIJson queryAlarmMessageListByTime(int pageNum, int pageCount, String start, String end, int handled);

    public void handleAlarmMessage(int id);

    public void insertAlarmRule(InsertUpdateAlarmRuleItem insertUpdateAlarmRuleItem);

    public void updateAlarmRule(InsertUpdateAlarmRuleItem insertUpdateAlarmRuleItem);

    List<TreeAlarmData> getTreeAlarmRule();

    void insertUpdateTreeAlarmRule(InsertUpdateAlarmRuleData insertUpdateAlarmRuleData);

    public void insertPersonAlarm(AlarmMessage alarmMessage);

    /*public void updatePersonAlarm(AlarmMessage alarmMessage);*/
    public List<AlarmMessage> queryAlarmMessageListByAssetId(int pageNum, int pageCount, int assetId);

    ResponseModel getAlarmByTime(String time);

    ResponseModel getAlarmByTimeState(String time, Integer state);
}

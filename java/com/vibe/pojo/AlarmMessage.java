package com.vibe.pojo;

import com.vibe.pojo.event.EventRank;

import lombok.Data;
@Data
public class AlarmMessage {
	private int id;
	private int handled;
	private String caption;
	private int auto;

	private int assetId;
	private int state;
	private String errorMessage;
	private String startTime;
	private String endTime;
	private String duration;
	private int alarmRuleId;

	private Integer warnId;
	private Integer warnRank;
	//事件等级
	private EventRank eventRank;
	//temporary
	private String system;
	private Integer deviceId;
	private String deviceName;
	private Integer monitorId;
	private String monitorName;
	private Integer catalogId;
	private String catalogName;

}

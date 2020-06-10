package com.vibe.pojo.event;

import lombok.Data;

@Data
public class AlarmRuleMessage {

	private Integer id;
	private String caption;
	private Integer catalog;
	private String catalogName;
	private String warnCond;// 组合报警条件
	
	private String system;
	private Integer parentId;//设备
	private Integer parentCatalogId;
	private String parentCatalogName;
	private String parentCaption;
	
	private String warnId;
	private String singleWarnCond;//拆分后的报警条件

	private EventRank eventRank;
	private String notify;//通知方式 预留
	private Integer removed;
}

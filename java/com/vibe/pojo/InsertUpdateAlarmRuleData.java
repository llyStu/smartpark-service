package com.vibe.pojo;

import java.util.List;

public class InsertUpdateAlarmRuleData {
	private List<InsertUpdateAlarmRuleItem> add;
	private List<InsertUpdateAlarmRuleItem> update;
	public List<InsertUpdateAlarmRuleItem> getAdd() {
		return add;
	}
	public void setAdd(List<InsertUpdateAlarmRuleItem> add) {
		this.add = add;
	}
	public List<InsertUpdateAlarmRuleItem> getUpdate() {
		return update;
	}
	public void setUpdate(List<InsertUpdateAlarmRuleItem> update) {
		this.update = update;
	}
	
	
}

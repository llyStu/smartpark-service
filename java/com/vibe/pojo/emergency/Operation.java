package com.vibe.pojo.emergency;

public class Operation {
	private Integer oid;
	private String event, level, actionId, target, data, dataField;
	private Action action;
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getTarget() {
		return target;
	}
	public Operation() {
		super();
	}
	public Operation(String actionId, String event, String level) {
		super();
		this.event = event;
		this.level = level;
		this.actionId = actionId;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
}

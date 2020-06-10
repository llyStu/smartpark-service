package com.vibe.pojo;

public class TimeControlTimePointBean {
	private int id;
	private int timeTaskId;
	private int scheduledTaskId;
	private int value;
	private String valueStr;
	private String unit;
	private String time;
	public int getTimeTaskId() {
		return timeTaskId;
	}
	public void setTimeTaskId(int timeTaskId) {
		this.timeTaskId = timeTaskId;
	}
	public int getScheduledTaskId() {
		return scheduledTaskId;
	}
	public void setScheduledTaskId(int scheduledTaskId) {
		this.scheduledTaskId = scheduledTaskId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getValueStr() {
		return valueStr;
	}
	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}

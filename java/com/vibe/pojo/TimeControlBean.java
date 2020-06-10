package com.vibe.pojo;

import java.util.List;

public class TimeControlBean {
	
	private int id;
	private String name;
	private int state;
	private int catlog;
	private List<Integer> controlIds;
	private List<TimeControlTimePointBean> timeControlTimePointBeans;
	private List<Integer> days;
	private String catlogName;
	private List<String> controlNames;
	public String getCatlogName() {
		return catlogName;
	}
	public void setCatlogName(String catlogName) {
		this.catlogName = catlogName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getCatlog() {
		return catlog;
	}
	public void setCatlog(int catlog) {
		this.catlog = catlog;
	}
	public List<Integer> getControlIds() {
		return controlIds;
	}
	public void setControlIds(List<Integer> controlIds) {
		this.controlIds = controlIds;
	}
	public List<TimeControlTimePointBean> getTimeControlTimePointBeans() {
		return timeControlTimePointBeans;
	}
	public void setTimeControlTimePointBeans(List<TimeControlTimePointBean> timeControlTimePointBeans) {
		this.timeControlTimePointBeans = timeControlTimePointBeans;
	}
	public List<Integer> getDays() {
		return days;
	}
	public void setDays(List<Integer> days) {
		this.days = days;
	}
	public List<String> getControlNames() {
		return controlNames;
	}
	public void setControlNames(List<String> controlNames) {
		this.controlNames = controlNames;
	}
	
	
}

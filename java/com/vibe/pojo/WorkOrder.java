package com.vibe.pojo;

import java.util.List;

/*
 * Work order class
 * */
public class WorkOrder extends SchedualPattern {
	
	private int id;
	private int num;//工单编号
	private int project;//项目名称
	private int type;//工单类型
	private int region;//巡检区域
	private int cycle;//巡检周期
	private int priority;//优先级
	private int receivePerson;//接单人
	private int state;//工单状态
	private String memo;//备注
	private List<CheckRoute> checkRoutes;//单个工单中的巡检路线列表
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getProject() {
		return project;
	}
	public void setProject(int project) {
		this.project = project;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getReceivePerson() {
		return receivePerson;
	}
	public void setReceivePerson(int receivePerson) {
		this.receivePerson = receivePerson;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<CheckRoute> getCheckRoutes() {
		return checkRoutes;
	}
	public void setCheckRoutes(List<CheckRoute> checkRoutes) {
		this.checkRoutes = checkRoutes;
	}
	
	
}

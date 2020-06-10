package com.vibe.pojo;

//列表查询时向界面分页显示的部分信息，点击之调到详情页面
public class CheckTemp {
	
	private Integer id;
	private String checkTime;
	private String checkPosition;
	private Integer responsiblePeople;
	private Integer checkTypeId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckPosition() {
		return checkPosition;
	}
	public void setCheckPosition(String checkPosition) {
		this.checkPosition = checkPosition;
	}
	public Integer getResponsiblePeople() {
		return responsiblePeople;
	}
	public void setResponsiblePeople(Integer responsiblePeople) {
		this.responsiblePeople = responsiblePeople;
	}
	public Integer getCheckTypeId() {
		return checkTypeId;
	}
	public void setCheckTypeId(Integer checkTypeId) {
		this.checkTypeId = checkTypeId;
	}
	
	
}

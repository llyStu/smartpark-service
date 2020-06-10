package com.vibe.pojo;

import java.util.List;

//F对页面显示详情（BuildingVo属性抽取）
public class BuildingDetail {

	private String name;//任务名称
	private Integer responsible_people;//负责人
	private String taskInterval;//任务周期
	private String resultDesc;//处理情况
	private Integer check_position;//巡查地点
	private List<String> pictures;//照片
	private Integer bId;//本体数据编号
	private Integer building_id;//本体名称
	private String evaluation;//评估
	private String protectStatus;//遗产要素保存状态
	private String statusDesc;//情况说明
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getResponsible_people() {
		return responsible_people;
	}
	public void setResponsible_people(Integer responsible_people) {
		this.responsible_people = responsible_people;
	}
	public String getTaskInterval() {
		return taskInterval;
	}
	public void setTaskInterval(String taskInterval) {
		this.taskInterval = taskInterval;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public Integer getCheck_position() {
		return check_position;
	}
	public void setCheck_position(Integer check_position) {
		this.check_position = check_position;
	}
	public List<String> getPictures() {
		return pictures;
	}
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	public Integer getbId() {
		return bId;
	}
	public void setbId(Integer bId) {
		this.bId = bId;
	}
	public Integer getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(Integer building_id) {
		this.building_id = building_id;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	public String getProtectStatus() {
		return protectStatus;
	}
	public void setProtectStatus(String protectStatus) {
		this.protectStatus = protectStatus;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	
}

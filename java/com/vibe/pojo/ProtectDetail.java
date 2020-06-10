package com.vibe.pojo;

import java.util.List;

//对页面显示详情（ProtectVo属性抽取）
public class ProtectDetail {

	private String name;//任务名称
	private Integer responsible_people;//负责人
	private String taskInterval;//任务周期
	private String resultDesc;//处理情况
	private Integer check_position;//巡查地点
	private List<String> pictures;//照片
	private Integer pId;//保护工程编号
	private Integer projectType;//保护工程分类
	private String projectDesc;//保护工程描述
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
	public Integer getPId() {
		return pId;
	}
	public void setPId(Integer pId) {
		this.pId = pId;
	}
	public Integer getProjectType() {
		return projectType;
	}
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	public String getProjectDesc() {
		return projectDesc;
	}
	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	
	
}

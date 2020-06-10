package com.vibe.pojo.emergency;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


public class EmergencyMessage {

	private Integer id;
	private String provenance;
	private String registrant;
	private String description;
	private Integer pid;
	private Integer etid;
	private Integer egid;
	private Integer euid;
	private Integer status;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	
	private String caption;
	private String name;
	private EmergencyUser emergencyUser;
	private EmergencyEventType emergencyEventType;
	private EmergencyEventGrade emergencyEventGrade;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public EmergencyEventGrade getEmergencyEventGrade() {
		return emergencyEventGrade;
	}
	public void setEmergencyEventGrade(EmergencyEventGrade emergencyEventGrade) {
		this.emergencyEventGrade = emergencyEventGrade;
	}
	public Integer getEuid() {
		return euid;
	}
	public void setEuid(Integer euid) {
		this.euid = euid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProvenance() {
		return provenance;
	}
	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}
	public String getRegistrant() {
		return registrant;
	}
	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getEtid() {
		return etid;
	}
	public void setEtid(Integer etid) {
		this.etid = etid;
	}
	public Integer getEgid() {
		return egid;
	}
	public void setEgid(Integer egid) {
		this.egid = egid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public EmergencyUser getEmergencyUser() {
		return emergencyUser;
	}
	public void setEmergencyUser(EmergencyUser emergencyUser) {
		this.emergencyUser = emergencyUser;
	}
	public EmergencyEventType getEmergencyEventType() {
		return emergencyEventType;
	}
	public void setEmergencyEventType(EmergencyEventType emergencyEventType) {
		this.emergencyEventType = emergencyEventType;
	}
	
	
	
}

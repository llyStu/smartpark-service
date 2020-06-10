package com.vibe.pojo.safety;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.vibe.pojo.AssetVo;

public class SafetyMessage {
	private String id;
	private String cardMark;
	private String cardName;
	private String department;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date incidentTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date stopTime;
	private String incidentType;
	private String incidentDescription;
	private String eventGrade;
	private AssetVo assetVo;
 	public String getCardMark() {
		return cardMark;
	}
	public void setCardMark(String cardMark) {
		this.cardMark = cardMark;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Date getIncidentTime() {
		return incidentTime;
	}
	public void setIncidentTime(Date incidentTime) {
		this.incidentTime = incidentTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	public String getIncidentType() {
		return incidentType;
	}
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	public String getIncidentDescription() {
		return incidentDescription;
	}
	public void setIncidentDescription(String incidentDescription) {
		this.incidentDescription = incidentDescription;
	}
	public String getEventGrade() {
		return eventGrade;
	}
	public void setEventGrade(String eventGrade) {
		this.eventGrade = eventGrade;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AssetVo getAssetVo() {
		return assetVo;
	}
	public void setAssetVo(AssetVo assetVo) {
		this.assetVo = assetVo;
	}
	
	
}

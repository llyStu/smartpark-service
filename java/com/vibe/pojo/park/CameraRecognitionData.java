package com.vibe.pojo.park;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CameraRecognitionData {
	private int id;
	private String cameraName;
	private String eventDetail;
	private String licencePlate;
	private String cameraId;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date time;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date stopTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCameraName() {
		return cameraName;
	}
	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	public String getEventDetail() {
		return eventDetail;
	}
	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}
	public String getLicencePlate() {
		return licencePlate;
	}
	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getCameraId() {
		return cameraId;
	}
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
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

	
	
	
}

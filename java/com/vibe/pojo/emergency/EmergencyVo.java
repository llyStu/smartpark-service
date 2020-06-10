package com.vibe.pojo.emergency;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class EmergencyVo {
	private Integer eid, type, level, progress;
	private String number, title, event, description, uname;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date begin_max;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date begin_min;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date enddate_max;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date enddate_min;
	private Integer pageNum = 1, pageSize = 10;
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Date getBegin_max() {
		return begin_max;
	}
	public void setBegin_max(Date begin_max) {
		this.begin_max = begin_max;
	}
	public Date getBegin_min() {
		return begin_min;
	}
	public void setBegin_min(Date begin_min) {
		this.begin_min = begin_min;
	}
	public Date getEnddate_max() {
		return enddate_max;
	}
	public void setEnddate_max(Date enddate_max) {
		this.enddate_max = enddate_max;
	}
	public Date getEnddate_min() {
		return enddate_min;
	}
	public void setEnddate_min(Date enddate_min) {
		this.enddate_min = enddate_min;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}

package com.vibe.pojo.auty;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ChangeShifts {
	private Integer id, submissionPeople, takeOverPeople;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date time;
	private String submissionPeopleName, takeOverPeopleName, memo;
	private int[] ids;
	
	private Integer pageNum, pageSize;
	public Integer getPageNum() {
		return pageNum == null ? 1 : pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize == null ? 10 : pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSubmissionPeople() {
		return submissionPeople;
	}
	public void setSubmissionPeople(Integer submissionPeople) {
		this.submissionPeople = submissionPeople;
	}
	public Integer getTakeOverPeople() {
		return takeOverPeople;
	}
	public void setTakeOverPeople(Integer takeOverPeople) {
		this.takeOverPeople = takeOverPeople;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getSubmissionPeopleName() {
		return submissionPeopleName;
	}
	public void setSubmissionPeopleName(String submissionPeopleName) {
		this.submissionPeopleName = submissionPeopleName;
	}
	public String getTakeOverPeopleName() {
		return takeOverPeopleName;
	}
	public void setTakeOverPeopleName(String takeOverPeopleName) {
		this.takeOverPeopleName = takeOverPeopleName;
	}
	public int[] getIds() {
		return ids;
	}
	public void setIds(int[] ids) {
		this.ids = ids;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}

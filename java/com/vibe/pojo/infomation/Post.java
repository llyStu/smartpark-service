package com.vibe.pojo.infomation;

import java.sql.Timestamp;

public class Post {
	private Integer pid;
	private Timestamp published;
	private String title;
	private String content;
	private Integer uid;
	private String user;
	private Integer level;
	private Integer views;
	private Integer state;
	@Override
	public String toString() {
		return "Post [pid=" + pid + ", published=" + published + ", title=" + title + ", content=" + content + ", uid="
				+ uid + ", level=" + level + ", views=" + views + ", state=" + state + "]";
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Timestamp getPublished() {
		return published;
	}
	public void setPublished(Timestamp published) {
		this.published = published;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
}

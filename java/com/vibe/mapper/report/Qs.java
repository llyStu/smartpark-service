package com.vibe.mapper.report;

import java.util.Date;

public class Qs {
	Integer id;
	Date time;
	String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public String getQs() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Qs [id=" + id + ", time=" + time + ", name=" + name + "]";
	}
}

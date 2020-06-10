package com.vibe.mapper.report;

public class QsVo {
	Integer id, id_min, id_max;
	String time, time_min, time_max;
	String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Qs [id=" + id + ", time=" + time + ", name=" + name + "]";
	}
	public Integer getId_min() {
		return id_min;
	}
	public void setId_min(Integer id_min) {
		this.id_min = id_min;
	}
	public Integer getId_max() {
		return id_max;
	}
	public void setId_max(Integer id_max) {
		this.id_max = id_max;
	}
	public String getTime_max() {
		return time_max;
	}
	public void setTime_max(String time_max) {
		this.time_max = time_max;
	}
	public String getTime_min() {
		return time_min;
	}
	public void setTime_min(String time_min) {
		this.time_min = time_min;
	}
}

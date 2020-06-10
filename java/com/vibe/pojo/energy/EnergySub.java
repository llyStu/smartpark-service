package com.vibe.pojo.energy;

import java.sql.Timestamp;

public class EnergySub {
	private Object xulie;
	private String name;//水电类别名称
	private Object value;//数值
	private String zhanbi; //比例

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZhanbi() {
		return zhanbi;
	}
	public void setZhanbi(String zhanbi) {
		this.zhanbi = zhanbi;
	}
	public Object getXulie() {
		return xulie;
	}
	public void setXulie(Object xulie) {
		this.xulie = xulie;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
}

package com.vibe.pojo;

import java.util.List;

public class LinkageLogBean {
	private int id;
	private String time;
	private String causeAsset;
	private String value;
	private List<LinkageLogEffectBean> effectBeans;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getCauseAsset() {
		return causeAsset;
	}


	public void setCauseAsset(String causeAsset) {
		this.causeAsset = causeAsset;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public List<LinkageLogEffectBean> getEffectBeans() {
		return effectBeans;
	}


	public void setEffectBeans(List<LinkageLogEffectBean> effectBeans) {
		this.effectBeans = effectBeans;
	}


	
}

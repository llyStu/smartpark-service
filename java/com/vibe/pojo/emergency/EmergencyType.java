package com.vibe.pojo.emergency;

public class EmergencyType {
	public EmergencyType(Integer id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	private Integer id;
	private String text;

	public EmergencyType() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

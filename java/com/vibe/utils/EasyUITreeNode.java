package com.vibe.utils;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6852673800313729130L;
	//id,该部门的id
	private Integer id;
	//树形菜单节点的名称
	private String text;
	//是否有子节点是值为open 否为closed
	private String state;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}

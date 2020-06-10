package com.vibe.pojo;

import java.util.ArrayList;
import java.util.List;

public class MenuBean {
	private int id;
	private int parent;
	private String name;
	private String caption;
	private Integer catalog;
	private String url;
	private int sequence;
	private String icon;
	private List<String> permisionList = new ArrayList<String>();
	
	
	
	public List<String> getPermisionList() {
		return permisionList;
	}
	public void setPermisionList(List<String> permisionList) {
		this.permisionList = permisionList;
	}
	public void addPermision(String name) {
		this.permisionList.add(name);
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getCatalog() {
		return catalog;
	}
	public void setCatalog(Integer catalog) {
		this.catalog = catalog;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	
	private List<MenuBean> children;
	public List<MenuBean> getChildren() {
		return children;
	}
	public void setChildren(List<MenuBean> children) {
		this.children = children;
	}
	
}

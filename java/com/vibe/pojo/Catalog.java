package com.vibe.pojo;

import java.util.List;

public class Catalog {

	private int type;
	private String name;
	private List<Integer> catalogId;
	
	
	public Catalog() {
		super();
	}
	
	public Catalog(int type, String name, List<Integer> catalogId) {
		super();
		this.type = type;
		this.name = name;
		this.catalogId = catalogId;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(List<Integer> catalogId) {
		this.catalogId = catalogId;
	}
	
	
}

package com.vibe.service.logAop;

public enum MethodType {
	
	ADD,
	EDIT,
	DELETE,
	UP,
	LOAD,
	QUERY;
	
	
	public String canonicalName() {
		return name().toLowerCase();
	}
	
}

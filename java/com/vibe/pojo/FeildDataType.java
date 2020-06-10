package com.vibe.pojo;

public class FeildDataType {

	private FeildDataType() {}
	

	
	private static final FeildDataType dataType = new FeildDataType();
	
	public static FeildDataType getFeildDataType(){
		return dataType;
	}
	
	

}

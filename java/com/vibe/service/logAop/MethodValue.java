package com.vibe.service.logAop;

import java.util.Arrays;

public  class MethodValue{
	
	private String name;
	private Object[] arguments;
	private Object returnValue;
	private String remark;
	private String option;
	private boolean ex;
	
	@Override
	public String toString() {
		return "MethodValue [name=" + name + ", arguments=" + Arrays.toString(arguments) + ", returnValue="
				+ returnValue + ", remark=" + remark + ", option=" + option + ", ex=" + ex + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Object[] getArguments() {
		return arguments;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public boolean isEx() {
		return ex;
	}
	public void setEx(boolean ex) {
		this.ex = ex;
	}
	public  boolean result(){
		
		if(returnValue == "500"){
			return false;
		}else{
			return true;
		}
	};
	public  String detail(){
		if(arguments != null){
			for (Object object : arguments) {
					System.out.println(object.toString()+"===");
			}
		}
	return "sss";
	};
	
	
	
}

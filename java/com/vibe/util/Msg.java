package com.vibe.util;

import java.util.HashMap;
import java.util.Map;

//用于记录分页信息
public class Msg {

	//状态码，100代表分页执行成功，200代表失败
	private int code;
	//分页执行情况的说明
	private String msg;
	//用于保存pageInfo
	private Map<String,Object> extend = new HashMap<String,Object>();
	
	//分页成功的方法
	public static Msg success(){
		Msg result = new Msg();
		result.setCode(0);
		result.setMsg("successfully");
		return result;
	}
	//分页失败的方法
		public static Msg fail(){
			Msg result = new Msg();
			result.setCode(200);
			result.setMsg("failed");
			return result;
		}
		//保存pageInfo
		public Msg add(String key,Object value){
			this.getExtend().put(key,value);
			return this;
		}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map<String, Object> getExtend() {
		return extend;
	}
	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	
	
}

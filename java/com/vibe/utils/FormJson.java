package com.vibe.utils;

import java.io.Serializable;

/**
 * 表单提交返回的json数据
 * @author FLex3
 *
 */
public class FormJson implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8208183207255180266L;
	private boolean success;
	private String message;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}

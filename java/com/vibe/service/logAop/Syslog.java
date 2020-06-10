package com.vibe.service.logAop;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Syslog implements Serializable{
	 /**
	 *  
	 */
	private static final long serialVersionUID = 7542091640602827711L;
	private Integer id;
    private Integer loginId;
    private String loginName;
     private String ipAddress;
     private String methodName;
     private String methodRemark;
     private String operatingContent;
     private String optDate;
     private String responseTime;
     private String result;
    //时间条件
     private String startTime;
     private String lastTime;
     
     
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodRemark() {
		return methodRemark;
	}
	public void setMethodRemark(String methodRemark) {
		this.methodRemark = methodRemark;
	}
	public String getOptDate() {
		if(null != optDate && optDate.endsWith(".0")){
			optDate = optDate.substring(0,optDate.length()-2);
		}
		return optDate;
	}
	public void setOptDate(String optDate) {
		this.optDate = optDate;
	}

	/*public LocalDateTime getOptDate() {
		return optDate;
	}

	public void setOptDate(String optDate) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(optDate,df);
		this.optDate = localDateTime;
	}*/

	public String getOperatingContent() {
		return operatingContent;
	}
	public void setOperatingContent(String operatingContent) {
		this.operatingContent = operatingContent;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Syslog [loginName=" + loginName + ", methodRemark=" + methodRemark + ", operatingContent="
				+ operatingContent + ", result=" + result + "]";
	}
	
	
	
	
	
}

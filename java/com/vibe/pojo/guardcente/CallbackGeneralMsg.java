package com.vibe.pojo.guardcente;


public class CallbackGeneralMsg {

	private String sessionId;
	private String interfaceCode;
	private Object data;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getInterfaceCode() {
		return interfaceCode;
	}
	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "AlarmGuardCenteMsg [sessionId=" + sessionId + ", interfaceCode=" + interfaceCode + ", data=" + data
				+ "]";
	}
	

	
	
	
	
}

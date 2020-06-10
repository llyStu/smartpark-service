package com.vibe.pojo.historydata;

public class HistoryDataCondition {
	private final int YEAR = 1;
	private final int MONTH = 2;
	private final int DAY = 3;
	@SuppressWarnings("unused")
	private final int HOUR = 4;
	
	private int deviceId;
	private String startTime; 
	private String lastTime;
	private int selectType;//年，月，日，时  1,2,3,4
	
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getStartTime() {
		if (null != startTime &&!"".equals(startTime)) {
			if (isYear())return startTime + "-00-00 00:00:00";
			if (isMonth())return startTime + "-00 00:00:00";
			if (isDay())return startTime + " 00:00:00";
		}
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getLastTime() {
		if(null != lastTime &&!"".equals(lastTime)){
			if(isYear())return lastTime+"-00-00 00:00:00";
			if(isMonth())return lastTime+"-00 00:00:00";
			if(isDay())return lastTime+" 00:00:00";
		}
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	
	public int getSelectType() {
		return selectType;
	}
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}
	
	public boolean isYear(){
		if(this.selectType == this.YEAR)return true;
		return false;
	}
	public boolean isMonth(){
		if(this.selectType == this.MONTH)return true;
		return false;
	}
	public boolean isDay(){
		if(this.selectType == this.DAY)return true;
		return false;
	}

	
}

package com.vibe.pojo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommonMonitorDataVo extends MonitorData {
	private final int YEAR = 1;
	private final int MONTH = 2;
	private final int DAY = 3;
	@SuppressWarnings("unused")
	private final int HOUR = 4;
	@SuppressWarnings("unused")
	private final int SINGLE = 0;
	@SuppressWarnings("unused")
	private final int RANGE = 1;
	private String startTime;
	private String lastTime;
	private Integer monitorId;
	private String caption;
	private String filterType;	
	private Integer catalog;
	private String redioValue;
	
	private int selectType;//年，月，日，时  1,2,3,4
	private Integer redioType;//单个，范围
	private int subNum = 13 ;
	private String tableName;
	
	private String floor; //楼层
	private String zhanbi; //比例
	private int xulie;
	

	
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getZhanbi() {
		return zhanbi;
	}

	public void setZhanbi(String zhanbi) {
		this.zhanbi = zhanbi;
	}

	public int getXulie() {
		return xulie;
	}

	public void setXulie(int xulie) {
		this.xulie = xulie;
	}

	public CommonMonitorDataVo() {
		// TODO Auto-generated constructor stub
	}
	
	public CommonMonitorDataVo(String startTime, String lastTime, Integer monitorId, String filterType) {
		super();
		this.startTime = startTime;
		this.lastTime = lastTime;
		this.monitorId = monitorId;
		this.filterType = filterType;
	}
	
	
	public String getRedioValue() {
		return redioValue;
	}

	public void setRedioValue(String redioValue) {
		this.redioValue = redioValue;
	}

	public String getUnit() {
		if (this.isDay())
			return "时";
		if (this.isMonth())
			return "日";
		if (this.isYear())
			return "月";
		return "";
	}
	public String getDateContrastFormat() {
			if(isMonth())
				return "dd";
			if(isDay())
				return "HH";
			if(isYear())
				return "MM";
		return "yyyy-MM-dd";
	}
	
	public String getDateFormat() {
		if(redioType != null && redioType == 1){
			if(isMonth())
				return "yyyy-MM";
			if(isDay())
				return "yyyy-MM-dd";
			if(isYear())
				return "yyyy";
		}else {
			if(isYear())
				return "yyyy-MM";
			if(isMonth())
				return "yyyy-MM-dd";
		}	
		return "yyyy-MM-dd hh:DD:ss";
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
	public int getSubNum() {
		return subNum;
	}

	public Integer getRedioType() {
		return redioType;
	}

	public void setRedioType(Integer redioType) {
		this.redioType = redioType;
	}

	public String getTableName() {
		setTableName();
		return tableName;
	}

	public void setTableName() {
		if(redioType != null && redioType == 1){
			//boolean idSingleYear = isYear() && redioType == 0;//单年
			//boolean idSingleMonth = isMonth() && redioType == 0;//单月
			//boolean idSingleDay = isDay() && redioType == 0;//单日
			//boolean idRangeYear = isYear() && redioType == 1;//范围年
			//boolean idRangeMonth = isMonth() && redioType == 1;//范围月
			//boolean idRangeDay = isDay() && redioType == 1;//范围日
			//boolean idRangeHour = selectType == 4 && redioType == 1;//范围时
			if(isMonth()){
				this.tableName =  DataTable.t_sample_float_month.toString();
				if(startTime.length()<10){
					this.startTime = startTime+"-01";
					this.lastTime = lastTime+"-01";
				}
				this.subNum = 7;}
			if(isDay()){
				this.tableName =  DataTable.t_sample_float_day.toString();
				if(startTime.length()<10){
					this.startTime = startTime+" 00:00:00";
					this.lastTime = lastTime+" 00:00:00";
				}
			    this.subNum = 10;}
			if(selectType == 4){
				this.tableName =  DataTable.t_sample_float_hour.toString();}
			if(isYear()){
				this.tableName =  DataTable.t_sample_float_year.toString();
				if(startTime.length()<10){
					this.startTime = startTime+"-01-01";
					this.lastTime = lastTime+"-01-01";
				}
				 this.subNum = 4;}
		}else{
			if(isYear()){
				this.tableName =  DataTable.t_sample_float_month.toString();
			    this.subNum = 4;
			}
			if(isMonth()){
				this.tableName =  DataTable.t_sample_float_day.toString();
				this.subNum = 7;
			}
			if(isDay()){
				this.tableName =  DataTable.t_sample_float_hour.toString();
				this.subNum = 10;
			}
		}
	}

	public int getSelectType() {
		return selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}	
	private Object waterValue;
	private Object gasValue;
	
	public Object getWaterValue() {
		return waterValue;
	}
	public void setWaterValue(Object waterValue) {
		this.waterValue = waterValue;
	}
	public Object getGasValue() {
		return gasValue;
	}
	public void setGasValue(Object gasValue) {
		this.gasValue = gasValue;
	}
	
	public Integer getCatalog() {
		return catalog;
	}

	public void setCatalog(Integer catalog) {
		this.catalog = catalog;
	}

	public Integer getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(Integer monitorId) {
		this.monitorId = monitorId;
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
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<String> getNowTimeAndLastTime() {
	List<String> dateList = new ArrayList<String>();
		LocalDate nowDate = LocalDate.now();
		if(selectType == YEAR){
			dateList.add(nowDate.getYear()+"");
			dateList.add(nowDate.minusYears(1).getYear()+"");
		}
		if(selectType == MONTH){
			dateList.add(nowDate.toString().substring(0,7));
			dateList.add(nowDate.minusMonths(1).toString().substring(0,7));
		}
		if(selectType == DAY){
			dateList.add(nowDate.toString());
			dateList.add(nowDate.minusDays(1).toString());
		}
		
		return dateList;
	}

	
	
}

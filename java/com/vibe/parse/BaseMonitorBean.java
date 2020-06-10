package com.vibe.parse;

public abstract class BaseMonitorBean extends BaseBean {
	private String parent;
	private String name;
	private String caption;
	private String catalog;
	
	private String io;
	private String source;
	private String alarm;
	private String timeInterval;
	private String transform;
	
	private String min;
	private String max;
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getIo() {
		return io;
	}
	public void setIo(String io) {
		this.io = io;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAlarm() {
		return alarm;
	}
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getTransform() {
		return transform;
	}
	public void setTransform(String transform) {
		this.transform = transform;
	}
	
	
	
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String tableName = null;
		if((getIo().equals(App.AI) || getIo().equals(App.DI))){
			tableName = App.PROBE_TABLE_NAME;
		}else {
			tableName = App.CONTROL_TABLE_NAME;
		}
		return "insert into " + tableName 
				+ "(id, parent,catalog, type, name,source, caption,time_interval, warn_cond,transform,min_value,max_value)values ("
				+ getId() + "," + getParent() + "," + getCatalog() + ",\"" + getMonitorType() + "\", \"" + getName() + "\" ,"
				+ (App.TEST?"null":getSource()) + ", \"" + getCaption() + "\", \"" + getTimeInterval() + "\" ,"
				+ (getAlarm() == null?getAlarm():("\""+getAlarm()+"\"")) + ", "
				+ (getTransform() == null?getTransform()+",":("\""+getTransform()+"\","))+getMin()+","+getMax() + ");\n";
	}
	
	public String getMonitorType() {
		// TODO Auto-generated method stub
		if (App.TEST) {
			if (getIo().equals(App.AI)) {
				return App.AI_TYPE_TEST;
			} else if (getIo().equals(App.AO)) {
				return App.AO_TYPE_TEST;
			} else if (getIo().equals(App.DI)) {
				return App.DI_TYPE_TEST;
			} else {
				return App.DO_TYPE_TEST;
			}
		} else {
			return getNotTestMonitorType();
		}
	}
	
	public String getNotTestMonitorType() {
		if (getIo().equals(App.AI)) {
			return getAiType();
		} else if (getIo().equals(App.AO)) {
			return getAoType();
		} else if (getIo().equals(App.DI)) {
			return getDiType();
		} else {
			return getDoType();
		}
	}
	
	public abstract String getAiType();
	public abstract String getAoType();
	public abstract String getDiType();
	public abstract String getDoType();
}

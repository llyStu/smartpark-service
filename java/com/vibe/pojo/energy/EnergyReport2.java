package com.vibe.pojo.energy;

public class EnergyReport2 {
	private String title, unit;
	private String[] xAxis;
	private Float[] values;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String[] getxAxis() {
		return xAxis;
	}
	public void setxAxis(String[] xAxis) {
		this.xAxis = xAxis;
	}
	public Float[] getValues() {
		return values;
	}
	public void setValues(Float[] values) {
		this.values = values;
	}
	
}
package com.vibe.pojo.energy;

public class EnergyReport {
	private String title, annotation;
	private String[] xAxis, yAxis;
	private Float[][] data;
	
	public EnergyReport(String[] xAxis, String[] yAxis, Float[][] data) {
		super();
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.data = data;
	}

	public EnergyReport() {
		super();
	}

	public String[] getxAxis() {
		return xAxis;
	}

	public void setxAxis(String[] xAxis) {
		this.xAxis = xAxis;
	}

	public String[] getyAxis() {
		return yAxis;
	}

	public void setyAxis(String[] yAxis) {
		this.yAxis = yAxis;
	}

	public Float[][] getData() {
		return data;
	}

	public void setData(Float[][] data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

}
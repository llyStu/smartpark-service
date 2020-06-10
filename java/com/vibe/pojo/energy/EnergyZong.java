package com.vibe.pojo.energy;

public class EnergyZong {
	private Double value;
	private String unit;
	public EnergyZong() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public EnergyZong(Double value, String unit) {
		super();
		this.value = value;
		this.unit = unit;
	}

}

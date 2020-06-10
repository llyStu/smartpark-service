package com.vibe.pojo.energy;
import java.sql.Timestamp;
public class EnergyCount {

	private Timestamp Time;
	
	private Double value;
    private String unit;

	

	

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	

	public EnergyCount(Timestamp time, Double value, String unit) {
		super();
		Time = time;
		this.value = value;
		this.unit = unit;
	}

	public EnergyCount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Timestamp getTime() {
		return Time;
	}

	public void setTime(Timestamp time) {
		Time = time;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}


	
	












}

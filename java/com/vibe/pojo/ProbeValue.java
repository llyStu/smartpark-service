package com.vibe.pojo;

import java.util.ArrayList;
import java.util.List;

public class ProbeValue {
	
	public String name;
	public List<Object> values = new ArrayList<Object>();
	public String unit;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
	public void addValue(Object value) {
		this.values.add(value);
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	

}

package com.vibe.service.energy;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.vibe.pojo.AssetVo;

public class Meter extends AssetVo{
	@NumberFormat(style=Style.NUMBER)
	private Integer parentMeter;
	@NumberFormat(style=Style.NUMBER) Integer meterType;
	private String meterTypes;
	private Integer grade;
	@NumberFormat(style=Style.NUMBER)
	private Integer itemizeType;
	private String itemizeCaption;
	private String itemizeExpression;
	private String startValue;
	private String endValue;
	private String redio;
	private Collection <Integer> parents;
	private int valueType;
	private List<Integer> catalogs = new ArrayList<>();
	
	public Meter(Integer id, int seqence) {
		super(id, seqence);
		// TODO Auto-generated constructor stub
	}
	
	public Meter(Integer id,Integer grade) {
		super(id);
		this.grade = grade;
	}

	public Meter(Integer probeId,Integer parentMeter, Integer grade) {
		super(probeId);
		this.parentMeter = parentMeter;
		this.grade = grade;
	}

	public Meter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Collection<Integer> getParents() {
		return parents;
	}
	public void setParents(Collection<Integer> parentIds) {
		this.parents = parentIds;
	}
	public String getItemizeCaption() {
		return itemizeCaption;
	}
	public void setItemizeCaption(String itemizeCaption) {
		this.itemizeCaption = itemizeCaption;
	}
	public String getRedio() {
		return redio;
	}
	public void setRedio(String redio) {
		this.redio = redio;
	}
	public String getItemizeExpression() {
		return itemizeExpression;
	}
	public void setItemizeExpression(String itemizeExpression) {
		this.itemizeExpression = itemizeExpression;
	}
	public Integer getItemizeType() {
		return itemizeType;
	}
	public void setItemizeType(Integer itemizeType) {
		this.itemizeType = itemizeType;
	}
	public String[] getMeterTypes() {
		
		if(meterTypes != null){
			String[] split = meterTypes.split(",");
			return split;
		}
		return null;
	}
	public void setMeterTypes(String meterTypes) {
		this.meterTypes = meterTypes;
	}
	public List<Integer> getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(List<Integer> catalogs) {
		this.catalogs = catalogs;
	}
	public Integer getParentMeter() {
		return parentMeter;
	}
	public void setParentMeter(Integer parentMeter) {
		this.parentMeter = parentMeter;
	}
	public Integer getMeterType() {
		return meterType;
	}
	public void setMeterType(Integer meterType) {
		this.meterType = meterType;
	}
	
	
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getStartValue() {
		return startValue;
	}
	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}
	public String getEndValue() {
		return endValue;
	}
	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}
	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	@Override
	public String toString() {
		return "Meter [parentMeter=" + parentMeter + ", meterType=" + meterType + ", meterTypes=" + meterTypes
				+ ", grade=" + grade + ", itemizeType=" + itemizeType + ", itemizeCaption=" + itemizeCaption
				+ ", itemizeExpression=" + itemizeExpression + ", startValue=" + startValue + ", endValue=" + endValue
				+ ", redio=" + redio + ", parents=" + parents + ", valueType=" + valueType + ", catalogs=" + catalogs
				+ "]";
	}

	/*@Override
	public String toString() {
		return super.toString()+"Meter [parentMeter=" + parentMeter + ", meterType=" + meterType + ", meterTypes=" + meterTypes
				+ ", grade=" + grade + ", itemizeType=" + itemizeType + ", itemizeCaption=" + itemizeCaption
				+ ", itemizeExpression=" + itemizeExpression + ", startValue=" + startValue + ", endValue=" + endValue
				+ ", redio=" + redio + ", catalogs=" + catalogs + "]";
	}*/
	
	
}

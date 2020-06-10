package com.vibe.pojo.energy;

public class MeterRelation {
	
    private int actualId;
    private int dummyId;
    private int meterKind;
    private float percentage;
    
	public MeterRelation(int actualId, int dummyId, int meterKind, float percentage) {
		super();
		this.actualId = actualId;
		this.dummyId = dummyId;
		this.meterKind = meterKind;
		this.percentage = percentage;
	}
	public MeterRelation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getActualId() {
		return actualId;
	}
	public void setActualId(int actualId) {
		this.actualId = actualId;
	}
	public int getDummyId() {
		return dummyId;
	}
	public void setDummyId(int dummyId) {
		this.dummyId = dummyId;
	}
	public int getMeterKind() {
		return meterKind;
	}
	public void setMeterKind(int meterKind) {
		this.meterKind = meterKind;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
    
}

package com.vibe.service.energy;

import com.vibe.service.classification.Code;

public class EnergyRatio {
	public  final float STANDARD_COAL_WATER= 0.257f;
	public  final float STANDARD_COAL_ElECTRICITY= 0.1229f;	
	public  final float STANDARD_COAL_GAS=1.33f;
	public  final float STANDARD_COAL_HOT=0.00955f;
	
	public  final float CARBON_WATER=0.91f;
	public  final float CARBON_ElECTRICITY=0.785f;
	public  final float CARBON_GAS=0.19f;
	public  final float CARBON_HOT=0.11f;
	
	public double translateCarbon(int catalog,double value){
		if(catalog == Code.ENERGY_WATER)
			return value*this.CARBON_WATER;
		if(catalog == Code.ENERGY_HOT)
			return value*this.CARBON_HOT;
		if(catalog == Code.ENERGY_ElECTRICITY)
			return value*this.CARBON_ElECTRICITY;
		if(catalog == Code.ENERGY_GAS)
			return value*this.CARBON_GAS;
		return value;
	}
	public double translateStandardCoal(int catalog,double value){
		if(catalog == Code.ENERGY_WATER)
			return value*this.STANDARD_COAL_WATER;
		if(catalog == Code.ENERGY_HOT)
			return value*this.STANDARD_COAL_HOT;
		if(catalog == Code.ENERGY_ElECTRICITY)
			return value*this.STANDARD_COAL_ElECTRICITY;
		if(catalog == Code.ENERGY_GAS)
			return value*this.STANDARD_COAL_GAS;
		return value;
	}
}

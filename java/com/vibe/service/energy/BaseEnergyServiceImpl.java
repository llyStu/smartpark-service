package com.vibe.service.energy;

import java.util.List;

import org.apache.http.util.TextUtils;


public abstract class BaseEnergyServiceImpl {

	public List<?> doSearchByTimeWork(String timeType,String time,String start,String end,EnergyServiceTime energyServiceTime) {
		if(!TextUtils.isEmpty(time)){
			return energyServiceTime.searchByTime(timeType,time);
		}else {
			return energyServiceTime.searchByStartEndTime(timeType,start,end);
		}
	}
	
}

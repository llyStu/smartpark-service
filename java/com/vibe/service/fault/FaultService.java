package com.vibe.service.fault;

import java.util.List;


import com.vibe.pojo.DailyCheck;
import com.vibe.pojo.TaskCount;


public interface FaultService {
	
	public void insertFault(DailyCheck dailyCheck);

	public void deleteFault(int parseInt);

	public DailyCheck queryFault(int id);

	public List<DailyCheck> queryFaultList();

	public void updateFault(DailyCheck dailyCheck);

	public void insertDeviceFault(DailyCheck dailyCheck);
	
	public void despatchTo(DailyCheck dailyCheck, int personId, String taskName, String taskDesc, String requestFinishTime);
	
	public List<DailyCheck> queryDespatchList(String username);
	
	public TaskCount findTaskCount(int personId);
	
}

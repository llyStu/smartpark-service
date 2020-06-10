package com.vibe.service.dailycheck;

import java.util.List;


import com.vibe.pojo.DailyCheck;


public interface DailyCheckService {
	
	public void insertDailyCheck(DailyCheck dailyCheck);

	public void deleteDailyCheck(int parseInt);

	public DailyCheck queryDailyCheck(int id);

	public List<DailyCheck> queryDailyCheckList();

	public void updateDailyCheck(DailyCheck dailyCheck);
	
}

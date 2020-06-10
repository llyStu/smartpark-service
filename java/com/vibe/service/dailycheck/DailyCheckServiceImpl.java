package com.vibe.service.dailycheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.dailycheck.DailyCheckDao;
import com.vibe.pojo.DailyCheck;

@Service
public class DailyCheckServiceImpl implements DailyCheckService {
	
	@Autowired
	private DailyCheckDao dailyCheckDao;
	
	public void insertDailyCheck(DailyCheck dailyCheck) {		

		dailyCheckDao.insertDailyCheck(dailyCheck);
		
	}

	public void deleteDailyCheck(int parseInt) {
		
		dailyCheckDao.deleteDailyCheck(parseInt);
	}

	public DailyCheck queryDailyCheck(int id) {
		
		DailyCheck d = dailyCheckDao.queryDailyCheck(id);
		//return dailyCheckDao.queryDailyCheck(id);
		return d;
	}

	@Override
	public List<DailyCheck> queryDailyCheckList() {
		
		return dailyCheckDao.queryDailyCheckList();
	}

	@Override
	public void updateDailyCheck(DailyCheck dailyCheck) {

		dailyCheckDao.updateDailyCheck(dailyCheck);
	}

}

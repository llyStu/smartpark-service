package com.vibe.service.fault;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.fault.OldFaultDao;
import com.vibe.pojo.DailyCheck;
import com.vibe.pojo.TaskCount;

@Service
public class OldFaultServiceImpl implements OldFaultService {

	@Autowired
	private OldFaultDao faultDao;

	public void insertFault(DailyCheck dailyCheck) {

		// 与设备表进行关联,添加设备故障记录
		faultDao.insertFaultDevice(dailyCheck);
		faultDao.insertFault(dailyCheck);
	}

	public void deleteFault(int parseInt) {

		faultDao.deleteFault(parseInt);
	}

	public DailyCheck queryFault(int id) {

		DailyCheck d = faultDao.queryFault(id);
		// return dailyCheckDao.queryDailyCheck(id);
		return d;
	}

	@Override
	public List<DailyCheck> queryFaultList() {

		return faultDao.queryFaultList();
	}

	@Override
	public void updateFault(DailyCheck dailyCheck) {

		faultDao.updateFault(dailyCheck);
	}

	@Override
	public void insertDeviceFault(DailyCheck dailyCheck) {
		faultDao.insertDeviceFault(dailyCheck);

	}

	@Override
	public void despatchTo(int id, int personId) {
		// TODO Auto-generated method stub
		faultDao.despatchTo(id, personId);
	}

	@Override
	public List<DailyCheck> queryDespatchList(String username) {
		// TODO Auto-generated method stub
		return faultDao.queryDespatchList(username);
	}

	@Override
	public TaskCount findTaskCount(int personId) {
		// TODO Auto-generated method stub
		TaskCount taskCount = new TaskCount();
		taskCount.setUnfinishTask(faultDao.queryUnFinishCount(personId));
		taskCount.setMonthTask(faultDao.queryMonthCount(personId));
		taskCount.setTodayTask(faultDao.queryTodayCount(personId));
		return taskCount;
	}

}

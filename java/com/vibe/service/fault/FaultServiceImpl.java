package com.vibe.service.fault;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.fault.FaultDao;
import com.vibe.pojo.DailyCheck;
import com.vibe.pojo.TaskCount;
import com.vibe.scheduledtasks.CommonTaskBean;
import com.vibe.service.task.TaskService;
import com.vibe.utils.time.TimeUtil;

@Service
public class FaultServiceImpl implements FaultService {

	@Autowired
	private FaultDao faultDao;
	@Autowired
	private TaskService taskService;

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
	public void despatchTo(DailyCheck dailyCheck, int personId, String taskName, String taskDesc, String requestFinishTime) {
		// TODO Auto-generated method stub
		CommonTaskBean commonTaskBean = new CommonTaskBean();
		commonTaskBean.setCreatePerson(dailyCheck.getPerson());
		commonTaskBean.setDetail(taskDesc);
		commonTaskBean.setExecutePerson(personId);
		commonTaskBean.setName(taskName);
		commonTaskBean.setType(4);
		commonTaskBean.setTaskId(dailyCheck.getId());
		commonTaskBean.setState(1);
		commonTaskBean.setStateTime(TimeUtil.getCurrentDateTime());
		commonTaskBean.setRequestFinishTime(requestFinishTime);
		taskService.insertTask(commonTaskBean);
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

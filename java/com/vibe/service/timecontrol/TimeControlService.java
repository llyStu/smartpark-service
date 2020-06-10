package com.vibe.service.timecontrol;

import java.util.List;

import com.vibe.pojo.TimeControlBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlLogBean;

public interface TimeControlService {

	List<TimeControlBean> queryTimeControlList();

	TimeControlBean insertTimeControl(TimeControlBean timeControlBean);


	void deleteTimeControl(int id);

	TimeControlBean updateTimeControl(TimeControlBean timeControlBean);

	List<TimeControlLogBean> queryTimeControlLog(int id);


	
}

package com.vibe.mapper.timecontrol;

import com.vibe.scheduledtasks.ScheduledTaskBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlDaoBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlLogBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlTimePointDaoBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeControlDao {
	
	List<TimeControlDaoBean> queryTimeControlList();
	
	List<TimeControlTimePointDaoBean> queryTimeControlTimePointList(@Param("id") int id);

	void deleteTimeControl(@Param("id") int id);

	void insertTimeControl(TimeControlDaoBean timeControlDaoBean);

	void insertTimeControlScheduledTask(ScheduledTaskBean scheduledTaskBean);
	
	ScheduledTaskBean queryTimeControlScheduledTask(@Param("id") int id);
	
	void insertTimeControlTimePoint(TimeControlTimePointDaoBean timeControlTimePointDaoBean);
	
	void updateTimeControl(TimeControlDaoBean timeControlDaoBean);
	
	void updateTimeControlTimePoint(TimeControlTimePointDaoBean timeControlTimePointDaoBean);
	
	void updateScheduledTask(ScheduledTaskBean scheduledTaskBean);

	List<TimeControlLogBean> queryTimeControlLog(@Param("id") int id);
	
}

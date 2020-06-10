package com.vibe.mapper.task;


import com.vibe.pojo.MaintenaceVo;
import com.vibe.pojo.Task;

import java.util.List;

/**
 * 增删改任务的接口
 * @author FLex3
 *
 */

public interface PageTaskMapper {
	public List<MaintenaceVo> queryMaintenaceList(List<Integer> taskIds);

	public List<Integer> queryTaskIds(Integer deviceId);

	public MaintenaceVo findTaskById(Integer taskId);
    //更新记录
	public void updateTask(Task task);
	//新增设备维护任务
	public void addTask(Task task);
   //维护维护信息详情
	public void addMaintenace(MaintenaceVo maintenaceVo);
    //维护与设备表的关系
	public void addTaskAndDevice(MaintenaceVo maintenace);
	/***
	 * 任务管理的赠删改查
	 * @param task
	 * @return
	 */
	public List<Task> queryTaskList(Task task);
	public Task queryTaskById(Integer id);
	public void deleteTaskById(Task taskById);
}

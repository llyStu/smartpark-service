package com.vibe.mapper.task;

import com.vibe.scheduledtasks.CommonTaskBean;
import com.vibe.scheduledtasks.ScheduledTaskBean;

import java.util.List;

public interface TaskDao {
    public List<ScheduledTaskBean> queryScheduledTasks(ScheduledTaskBean taskBean);

    public void insertScheduledTask(ScheduledTaskBean scheduledTaskBean);

    public void updateScheduledTask(ScheduledTaskBean scheduledTaskBean);

    public void deleteScheduledTask(int id);

    public List<CommonTaskBean> queryTasks(CommonTaskBean taskBean);

    public void deleteTask(CommonTaskBean taskBean);

    public void updateTask(CommonTaskBean task);

}

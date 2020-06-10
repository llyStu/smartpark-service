package com.vibe.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.task.TaskDao;
import com.vibe.monitor.db.CommonTaskDAO;
import com.vibe.scheduledtasks.CommonTaskBean;
import com.vibe.scheduledtasks.ScheduledTaskBean;
import com.vibe.scheduledtasks.ScheduledTaskManager;

@Service
public class TaskServiceimpl implements TaskService {

	@Autowired
	private TaskDao taskDao;
	@Autowired
	private CommonTaskDAO commonTaskDao;


	@Override
	public List<ScheduledTaskBean> queryScheduledTasks(ScheduledTaskBean taskBean) {
		// TODO Auto-generated method stub
		return taskDao.queryScheduledTasks(taskBean);
	}

	@Override
	public void updateScheduledTask(ScheduledTaskBean taskBean) {

		// TODO Auto-generated method stub
		ScheduledTaskBean scheduledTaskBeanTemp = new ScheduledTaskBean();
		scheduledTaskBeanTemp.setId(taskBean.getId());
		ScheduledTaskBean oldTaskBean = taskDao.queryScheduledTasks(scheduledTaskBeanTemp).get(0);
		taskDao.updateScheduledTask(taskBean);
		if (taskBean.getType() == oldTaskBean.getType()) {
			if (taskBean.getState() == 1 && oldTaskBean.getState() == 2) {
				try {
					ScheduledTaskManager.addJob(taskBean.getId() + "", Class.forName(taskBean.getClassName()),
							taskBean.getTimeExpression(), taskBean);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (taskBean.getState() == 2 && oldTaskBean.getState() == 1) {
				ScheduledTaskManager.removeJob(taskBean.getId() + "");
			}
			if (taskBean.getState() == 1 && oldTaskBean.getState() == 1
					&& !oldTaskBean.getTimeExpression().equals(taskBean.getTimeExpression())) {
				ScheduledTaskManager.modifyJobTime(taskBean.getId() + "", taskBean.getTimeExpression(),taskBean);
			}
		} else {
			if (taskBean.getState() == 1 && oldTaskBean.getState() == 1) {
				ScheduledTaskManager.removeJob(taskBean.getId() + "");
				try {
					ScheduledTaskManager.addJob(taskBean.getId() + "", Class.forName(taskBean.getClassName()),
							taskBean.getTimeExpression(), taskBean);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (taskBean.getState() == 1 && oldTaskBean.getState() == 2) {
				try {
					ScheduledTaskManager.addJob(taskBean.getId() + "", Class.forName(taskBean.getClassName()),
							taskBean.getTimeExpression(), taskBean);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (taskBean.getState() == 2 && oldTaskBean.getState() == 1) {
				ScheduledTaskManager.removeJob(taskBean.getId() + "");
			}
		}

	}

	@Override
	public void insertScheduledTask(ScheduledTaskBean taskBean) {
		// TODO Auto-generated method stub
		taskDao.insertScheduledTask(taskBean);
		if (taskBean.getState() == 1) {
			try {
				ScheduledTaskManager.addJob(taskBean.getId() + "", Class.forName(taskBean.getClassName()),
						taskBean.getTimeExpression(), taskBean);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteScheduledTask(int id) {
		// TODO Auto-generated method stub
		ScheduledTaskBean scheduledTaskBeanTemp = new ScheduledTaskBean();
		scheduledTaskBeanTemp.setId(id);
		ScheduledTaskBean oldTaskBean = taskDao.queryScheduledTasks(scheduledTaskBeanTemp).get(0);
		taskDao.deleteScheduledTask(id);
		if (oldTaskBean.getState() == 1) {
			ScheduledTaskManager.removeJob(id + "");
		}
	}

	@Override
	public List<CommonTaskBean> queryTasks(CommonTaskBean taskBean) {
		// TODO Auto-generated method stub
		List<CommonTaskBean> list =  taskDao.queryTasks(taskBean);
		for (CommonTaskBean commonTaskBean : list) {
			commonTaskBean.setRequestFinishTime(commonTaskBean.getRequestFinishTime().substring(0, commonTaskBean.getRequestFinishTime().length()-2));
		}
		return list;
	}

	@Override
	public void insertTask(CommonTaskBean taskBean) {
		// TODO Auto-generated method stub
		commonTaskDao.insertTask(taskBean);
	}

	@Override
	public void deleteTask(int id) {
		// TODO Auto-generated method stub
		CommonTaskBean commonTaskBeanTemp = new CommonTaskBean();
		commonTaskBeanTemp.setId(id);
		taskDao.deleteTask(commonTaskBeanTemp);
	}

	@Override
	public void updateTask(CommonTaskBean taskBean) {
		// TODO Auto-generated method stub
		taskDao.updateTask(taskBean);
	}

}

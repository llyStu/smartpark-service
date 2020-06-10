package com.vibe.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.task.SingleTaskDao;
import com.vibe.pojo.SingleTask;

@Service
public class SingleTaskServiceImpl implements SingleTaskService {

	@Autowired
	private SingleTaskDao singleTaskDao;
	
	@Override
	public void insert(SingleTask singleTask) {
		// TODO Auto-generated method stub
		singleTaskDao.insert(singleTask);
	}

	@Override
	public Integer queryMaxId() {
		// TODO Auto-generated method stub
		return singleTaskDao.queryMaxId();
	}

	@Override
	public List<SingleTask> queryForList(int routeId) {
		// TODO Auto-generated method stub
		return singleTaskDao.queryForList(routeId);
	}

}

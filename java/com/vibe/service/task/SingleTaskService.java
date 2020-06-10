package com.vibe.service.task;

import java.util.List;

import com.vibe.pojo.SingleTask;

public interface SingleTaskService {

	public abstract void insert(SingleTask singleTask);
	
	public abstract Integer queryMaxId();
	
	public abstract List<SingleTask> queryForList(int routeId);
}

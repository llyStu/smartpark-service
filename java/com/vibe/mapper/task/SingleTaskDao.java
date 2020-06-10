package com.vibe.mapper.task;

import com.vibe.pojo.SingleTask;

import java.util.List;

public interface SingleTaskDao {

    public abstract void insert(SingleTask singleTask);

    public abstract Integer queryMaxId();

    public abstract List<SingleTask> queryForList(int route_id);
}

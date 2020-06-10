package com.vibe.service.task;

import java.util.List;

import com.vibe.pojo.SingleProp;

public interface SinglePropService {

    public abstract List<SingleProp> queryForList(int taskId);

    public abstract void insert(SingleProp singleProp);

    public abstract void insertIntoIntervalTable(int taskId, int propId);

    public abstract int queryMaxId();
}

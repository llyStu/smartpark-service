package com.vibe.mapper.dailycheck;

import com.vibe.pojo.WorkOrder;

import java.util.List;
import java.util.Map;

/*
 * Interface to query mysql for work order
 * */
public interface WorkOrderDao {

    public abstract void insert(WorkOrder workOrder);

    public abstract void remove(int id);

    public abstract void update(WorkOrder workOrder);

    public abstract WorkOrder query(int id);

    //query method contains simple query with create time and query with blur conditions
    public abstract List<WorkOrder> queryForList(Map<String, Object> map);

    //Get counts of current work orders in different state
    public abstract List<Map<String, Integer>> getCurrentOrderQuantity();

}

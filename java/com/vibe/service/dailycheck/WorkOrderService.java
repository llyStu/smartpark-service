package com.vibe.service.dailycheck;

import java.util.List;
import java.util.Map;

import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.WorkOrder;

public interface WorkOrderService {

    public abstract void insert(WorkOrder workOrder);

    public abstract void remove(int id);

    public abstract void update(WorkOrder workOrder);

    public abstract WorkOrder query(int id);

    public abstract List<WorkOrder> queryForList(Map<String, Object> map);

    public abstract Map<String, Integer> getCurrentOrderQuantity();

    public abstract List<CommonSelectOption> getCheckSitesOrItems(int region);

    public abstract void addCheckSchedual(int orderId, String routeName, String siteNames, String siteSequences);

    public abstract Map<String, Integer> getCurrentOrderQuantityWithSate(String state);

    public abstract void updateSingleTask(String propsContent, int taskId);

}

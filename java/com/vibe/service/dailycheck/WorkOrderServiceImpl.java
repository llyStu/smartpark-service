package com.vibe.service.dailycheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.dailycheck.WorkOrderDao;
import com.vibe.pojo.CheckRoute;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.SingleProp;
import com.vibe.pojo.SingleTask;
import com.vibe.pojo.WorkOrder;
import com.vibe.service.task.SinglePropService;
import com.vibe.service.task.SingleTaskService;
import com.vibe.util.CatalogIds;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private SelectOptionDao selectOptionService;
    @Autowired
    private CheckRouteService checkRouteService;
    @Autowired
    private SingleTaskService singleTaskService;
    @Autowired
    private SinglePropService singlePropService;

    @Override
    public void insert(WorkOrder workOrder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(WorkOrder workOrder) {
        // TODO Auto-generated method stub

    }

    /*
     * Query order details by id
     * */
    @Override
    public WorkOrder query(int id) {
        // TODO Auto-generated method stub
        //query routes list by order id
        WorkOrder workOrder = workOrderDao.query(id);
        List<CheckRoute> checkRoutes = checkRouteService.queryForList(id);
        List<SingleTask> checkSites;
        List<SingleProp> props;
        for (CheckRoute route : checkRoutes) {
            //query sites list by route id , order by "sequence"
            checkSites = singleTaskService.queryForList(route.getId());
            for (SingleTask site : checkSites) {
                //quey props list by site id
                props = singlePropService.queryForList(site.getId());
                if (props != null) {
                    site.setProps(props);
                }
            }
            if (checkSites != null) {
                route.setCheckSites(checkSites);
            }
        }
        if (checkRoutes != null) {
            workOrder.setCheckRoutes(checkRoutes);
        }
        return workOrder;
    }

    @Override
    public List<WorkOrder> queryForList(Map<String, Object> map) {
        // TODO Auto-generated method stub
        List<WorkOrder> dbResult = workOrderDao.queryForList(map);
        return dbResult;
    }

    /*
     * Get counts of current work orders in different state
     * */
    @Override
    public Map<String, Integer> getCurrentOrderQuantity() {
        // TODO Auto-generated method stub
        List<Map<String, Integer>> dbResult = workOrderDao.getCurrentOrderQuantity();
        List<CommonSelectOption> selectOptionList = selectOptionService.anQuerySelectOptionList(CatalogIds.WORK_ORDER_STATE_CATALOG);
        Map<String, Integer> resultMap = null;
        if (dbResult == null || selectOptionList == null) {
            return resultMap;
        } else {
            resultMap = new HashMap<String, Integer>();
            for (CommonSelectOption item : selectOptionList) {
                String stateName = item.getName();
                int state = item.getId();
                //此处待改进，没必要每次都从头遍历集合
                for (Map<String, Integer> singleResult : dbResult) {
                    if (state == singleResult.get("state")) {
                        Number count = singleResult.get("count");
                        resultMap.put(stateName, count.intValue());
                    }
                }
            }
        }
        return resultMap;
    }

    /*
     * Get check sites list in specific check region
     * Return check regions list if param is 0
     * */
    @Override
    public List<CommonSelectOption> getCheckSitesOrItems(int region) {
        // TODO Auto-generated method stub
        List<CommonSelectOption> allSites = selectOptionService.anQuerySelectOptionList(CatalogIds.CHECK_DESTINATION_CATALOG);
        List<CommonSelectOption> result = new ArrayList<CommonSelectOption>();
        if (allSites != null && allSites.size() != 0) {
            for (CommonSelectOption item : allSites) {
                if (item.getParentId() == region) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    @Override
    public void addCheckSchedual(int orderId, String routeName, String siteNames, String siteSequences) {
        // TODO Auto-generated method stub
        //insert a record into table t_route
        CheckRoute checkRoute = new CheckRoute();
        checkRoute.setOrderId(orderId);
        checkRoute.setName(routeName);
        Integer routeId = checkRouteService.queryMaxId();
        checkRoute.setId(routeId != null ? ++routeId : 1);
        checkRouteService.insert(checkRoute);

        //insert a record into table t_single_task correspondingly
        //default formater of siteNames and siteSequences likes: "1,2,4,6,7"
        String[] sites = siteNames.split(",");
        String[] sequences = siteSequences.split(",");

        for (int i = 0; i < sites.length; i++) {
            Integer singleTaskId = singleTaskService.queryMaxId();
            SingleTask singleTask = new SingleTask();
            singleTask.setRouteId(routeId != null ? routeId : 1);
            singleTask.setId(singleTaskId != null ? singleTaskId + 1 : 1);
            singleTask.setSequence(Integer.parseInt(sequences[i]));
            singleTaskService.insert(singleTask);
        }
    }

    /*
     * Get counts of orders in different states
     * */
    @Override
    public Map<String, Integer> getCurrentOrderQuantityWithSate(String state) {
        // TODO Auto-generated method stub
        Map<String, Integer> result = new HashMap<String, Integer>();
        Map<String, Integer> stateData = this.getCurrentOrderQuantity();
        if (stateData.get(state) != null) {
            result.put(state, stateData.get(state));
        }
        return result;
    }

    /*
     * Update single task(site)
     * */
    @Override
    public void updateSingleTask(String propsContent, int taskId) {
        // TODO Auto-generated method stub
        if (propsContent != null) {
            //split values of props sent through HTTP request
            String[] propValues = propsContent.split(",");
            //query names of props by site id
            List<SingleProp> props = singlePropService.queryForList(taskId);

            int maxPropId = singlePropService.queryMaxId();

            //wrap name&value into a SingleProp object
            int index = 0;
            for (SingleProp item : props) {
                item.setValue(Float.parseFloat(propValues[index]));
                item.setId(++maxPropId);
                //insert property records into t_single_prop
                singlePropService.insert(item);
                //insert task&props records into t_task_props
                singlePropService.insertIntoIntervalTable(taskId, maxPropId);
            }
        }
    }

}

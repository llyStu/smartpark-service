package com.vibe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.WorkOrder;
import com.vibe.service.dailycheck.WorkOrderService;

/*
 * Controller for receiving request of work order type
 * */

@Controller
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @RequestMapping("/insert_work_order")
    public String insertWorkOrder(WorkOrder workOrder) {
        try {
            workOrderService.insert(workOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return "error_page";
        }
        return "order/list_work_order";
    }

    @RequestMapping("/get_current_order_count")
    @ResponseBody
    public Map<String, Integer> getCurrentOrderQuantity(@RequestParam(defaultValue = "all") String state) {
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        if (state.equals("all")) {
            resultMap = workOrderService.getCurrentOrderQuantity();
        } else {
            resultMap = workOrderService.getCurrentOrderQuantityWithSate(state);
        }
        return resultMap;
    }

    @RequestMapping("/list_current_order")
    @ResponseBody
    public List<WorkOrder> listWorkOrder() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<WorkOrder> result = workOrderService.queryForList(map);
        return result;
    }

    @RequestMapping("/list_regions_or_sites")
    @ResponseBody
    public List<CommonSelectOption> getCheckSites(@RequestParam(defaultValue = "0") int region) {
        return workOrderService.getCheckSitesOrItems(region);
    }

    /*
     * siteNames is combination of a series of selected site ids
     * siteSequences is also a combination param, indicating the check order of different sites.
     * */
    @RequestMapping("/add_check_schedual")
    public String addCheckSchedule(int orderId, String routeName, String siteNames, String siteSequences) {
        workOrderService.addCheckSchedual(orderId, routeName, siteNames, siteSequences);
        return "order/list_work_order";
    }

    @RequestMapping("/update_order")
    public String confirmSendOrder(WorkOrder workOrder) {
        workOrderService.update(workOrder);
        return "order/list_work_order";
    }

    @RequestMapping("/query_order_details")
    @ResponseBody
    public WorkOrder queryOrderById(int id) {
        return workOrderService.query(id);
    }

    /*
     * bind to save button beneathe the check site
     * params propsContent is concat values of different propss
     * */
    @RequestMapping("/update_single_task")
    public void updateSingleTask(String propsContent, int taskId) {
        workOrderService.updateSingleTask(propsContent, taskId);
    }

}

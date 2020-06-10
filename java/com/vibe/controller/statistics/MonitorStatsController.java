package com.vibe.controller.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.service.statistics.MonitorStatsService;
import com.vibe.util.POITool;

@Controller
public class MonitorStatsController {
    @Autowired
    private MonitorStatsService service;

    @RequestMapping(value = {"/statistics/monitor_states_chart"}, method = {RequestMethod.GET})
    public String getStateChart(ModelMap model) {
        return "statistics/monitor_stats_home";
    }

    @RequestMapping(value = {"/statistics/monitor_states_data"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, List<Object>> getStateData() {
        Map<String, List<Object>> dataMap = this.service.getStateData();
        return dataMap;
    }

    @RequestMapping(value = {"/statistics/monitor_stats_home"}, method = {RequestMethod.GET})
    public String getCatalogCount(ModelMap model) {
        return "statistics/monitor_stats_home";
    }

    @RequestMapping(value = {"/statistics/monitor_count"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, List<Object>> getCount() {
        Map<String, List<Object>> resultMap = this.service.getMonitorCount();
        return resultMap;
    }

    @RequestMapping(value = {"/statistics/device_enabled_year"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, List<Object>> getDeviceEnabledYears() {
        Map<String, List<Object>> resultMap = this.service.getDeviceEnabledYears();
        return resultMap;
    }

    @RequestMapping("/statistics/queryAllProbe")
    @ResponseBody
    public Map<String, List<Object>> queryAllProbe() {

        Map<String, List<Object>> resultMap = service.queryAllProbe();
        return resultMap;
    }

    @RequestMapping("/statistics/getMonitorDetails")
    @ResponseBody
    public Map<String, List<Object>> getInfoByMonitorState(String filter, int index, String name) {

        Map<String, List<Object>> resultMap = service.getInfoByMonitorState(filter, index, name);
        return resultMap;
    }

    @RequestMapping("/statistics/getMonitorDetailsExport")
    @ResponseBody
    public void getInfoByMonitorStateExport(HttpServletResponse response, String filter, int index, String name) {

        Map<String, List<Object>> resultMap = service.getInfoByMonitorState(filter, index, name);
        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        map.put("编号", resultMap.get("monitorIds"));
        map.put("名称", resultMap.get("captions"));
        Boolean be = POITool.mapToExcelMonitor(map, "监测器表", response);

    }

}

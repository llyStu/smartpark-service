package com.vibe.controller.energy.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.Data;
import com.vibe.service.energy.demo.EnergyDemo;

@Controller
public class EnergyDemoController {
    @Autowired
    private EnergyDemo energyDemo;

    //查询当天的能耗的条形图
    @RequestMapping("/demo/demo_list")
    public @ResponseBody
    Map<String, Object> queryEnergyDemoListByPage(CommonMonitorDataVo vo,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "6") Integer rows) {
        return energyDemo.queryEnergyDemoListByPage(vo, page, rows);
    }

    //查询近期24小时的用能情况,以及BIM染色数据
    @RequestMapping("/demo/sum")
    public @ResponseBody
    Map<String, Object> selectValue(Integer id, Integer monitorId) {
        return energyDemo.selectValue(id, monitorId);
    }

    //分项显示的饼图 flag是空间还是类型
    @RequestMapping("/demo/classify")
    public @ResponseBody
    Map<String, Object> getClassifyValue(Integer id, String flag) {
        //return energyDemo.getClassifyValue(monitorId);
        return energyDemo.getClassifyValue(id, flag);
    }

    @RequestMapping("/demo/data")
    public @ResponseBody
    Map<String, Data> getData() {
        Map<String, Data> map = new HashMap<String, Data>();

        Data dianti = new Data(52, 67372458, "catalog");
        map.put("lift", dianti);

        Data zaoming = new Data(51, 67372457, "catalog");
        map.put("light", zaoming);

        Data zong = new Data(34, 67372456, "space");
        map.put("floor", zong);

        Data f3 = new Data(67371009, 67372463, "space");
        map.put("floor1", f3);

        Data f4 = new Data(67371010, 67372464, "space");
        map.put("floor2", f4);

        Data f17 = new Data(67371011, 67372465, "space");
        map.put("floor3", f17);

        return map;
    }
}

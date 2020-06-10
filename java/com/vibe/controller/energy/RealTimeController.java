package com.vibe.controller.energy;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.energy.Curves;
import com.vibe.service.energy.EnergyRealTimeService;

@Controller
public class RealTimeController {

    @Autowired
    private EnergyRealTimeService service;

    @RequestMapping(value = "/energy/synthesize")
    public @ResponseBody
    Map<String, Object> getSynthesize(CommonMonitorDataVo vo) {

        Map<String, Object> synthesize = service.getSynthesize(vo);
        return synthesize;
    }

    @RequestMapping(value = "/real_time/total")
    public @ResponseBody
    List<Curves> getRealTimeTotal(CommonMonitorDataVo vo) {

        return service.getRealTimeTotal(vo);
    }

    @RequestMapping(value = "/real_time/building")
    public @ResponseBody
    List<Map<String, Object>> getRealTimeBuding(Integer parentId, CommonMonitorDataVo vo) {

        return service.getRealTimeBuilding(parentId, vo);
    }
}

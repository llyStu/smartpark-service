package com.vibe.controller.energy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.KeyValueBean;
import com.vibe.service.energy.EnergyService;

@Controller
public class WkEnergyController {
	@Autowired
    EnergyService energyService;
	@RequestMapping("/fenxiangduibi")
	@ResponseBody
	public List<KeyValueBean> fenxiangduibi(@RequestParam("timeType") String timeType,@RequestParam("time") String time,@RequestParam("start") String start,@RequestParam("end") String end){
		return energyService.fenxiangduibi(timeType,time, start, end);
	}
	
	@RequestMapping("/wangqitongji")
	@ResponseBody
	public List<KeyValueBean> wangqitongji(@RequestParam("timeType") String timeType,@RequestParam("time") String time,@RequestParam("start") String start,@RequestParam("end") String end){
		return energyService.fenxiangduibi(timeType,time, start, end);
	}
}

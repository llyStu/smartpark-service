package com.vibe.controller.energy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.Ratio;
import com.vibe.service.energy.EnergyStatisticsService;
import com.vibe.utils.EasyUIJson;

@Controller
public class EnergyController {
	
    @Autowired
    private EnergyStatisticsService service;
    
    /*
     * 两个时间段之间  电能，水、热能的监测值，分页列表数据
     * 参数：id  封装到vo中，表示parentId
     */
    @RequestMapping("/energy/energy_total_data")
    public  @ResponseBody EasyUIJson queryEnergyDataListByPage(CommonMonitorDataVo vo,
    		@RequestParam(defaultValue="1")Integer page, 
    		@RequestParam(defaultValue="10")Integer rows){
    	EasyUIJson listByPage = service.queryEnergyDataListByPage(vo, page, rows);
    	return listByPage;
    }
    //获取空间下的总电能数据，排序条形图
    @RequestMapping("/energy/energy_echart")
    public  @ResponseBody Map<String, Object> queryEnergyDataEcart(CommonMonitorDataVo vo){
    	Map<String, Object> map = service.queryEnergy(vo);
    	return map;
    }
    /*@RequestMapping("/energy/energy_static")
    public  @ResponseBody Map<String, Object> queryEnergyStaticData(CommonMonitorDataVo vo){
    	Map<String, Object> map = service.queryEnergyStaticMap(vo);
    	return map;
    }*/
    @RequestMapping("/energy/energy_kind")
    public  @ResponseBody List<CommonMonitorDataVo> queryEnergyKind(int parent, int catalog){
    	 List<CommonMonitorDataVo> list= service.queryEnergyKind(parent, catalog);
    	return list;
    }
    
    /*@RequestMapping("/energy/carbon")
    public  @ResponseBody List<EnergyData> queryEnergyCarbon(int parent,int catalog){
    	 List<EnergyData> list= service.energyCarbon(parent, catalog);
    	return list;
    }*/
   /* @RequestMapping(value="/energy/carbonDataList")
	public @ResponseBody Map<String,Object> getRecentLineValues(CommonMonitorDataVo vo, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows){
		Map<String,Object> lines = service.queryCarbonDataListByPage(vo, page, rows);
		return lines;
	}*/
    //分项能效比
    @RequestMapping(value="/energy/item_ratio")
	public @ResponseBody List<Ratio> getItemRatio(CommonMonitorDataVo vo){
		
		return null;
	}
	
}

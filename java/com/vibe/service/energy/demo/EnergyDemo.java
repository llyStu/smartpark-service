package com.vibe.service.energy.demo;

import java.util.Map;

import com.vibe.pojo.CommonMonitorDataVo;


public interface EnergyDemo {
		//三维客户端，左侧树形菜单
	
	
	    //当天每个时段的能耗
	 public Map<String, Object> queryEnergyDemoListByPage(CommonMonitorDataVo vo, Integer page, Integer rows);
	    //饼状图
	public Map<String, Object> getClassifyValue(Integer id, String flag);
	//三维模型染色
	Map<String,Object> selectValue(Integer id, Integer monitorId);
}

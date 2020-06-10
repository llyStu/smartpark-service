package com.vibe.mapper.energy.demo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnergyDemoMapper {

	public List<Integer> findProbeByCatalogs(List<Integer> list);
	
	public List<Integer> findProbeBySpace(
            @Param(value = "list") List<Integer> list,
            @Param(value = "catalogId") Integer catalogId);
	
	
}

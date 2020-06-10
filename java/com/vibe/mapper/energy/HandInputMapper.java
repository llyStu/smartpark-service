package com.vibe.mapper.energy;

import com.vibe.pojo.HandInputData;
import com.vibe.pojo.energy.ProbeMeter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandInputMapper {
	void addHandInputData(HandInputData data);
	void updateHandInput(HandInputData data);
	List<HandInputData> findHandInput(@Param("start") String start, @Param("end") String end);
	void deleteHandInput(String idStr);
	HandInputData findAHandInput(@Param("id") String id);
	List<ProbeMeter> getProbe(@Param("energyType") int energyType,
                              @Param("subitemType") int subitemType,
                              @Param("tp") int tp);
	HandInputData findHandInputById(@Param("id") Integer id);
}

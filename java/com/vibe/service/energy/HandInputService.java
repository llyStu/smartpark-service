package com.vibe.service.energy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vibe.pojo.HandInputData;
import com.vibe.pojo.HandInputProbe;
import com.vibe.pojo.energy.ProbeMeter;

public interface HandInputService {
	void handInputData(List<HandInputData> datas);
	void updateHandInput(HandInputData data);
	List<HandInputData> findHandInput(@Param("start") String start, @Param("end") String end);
	void deleteHandInput(String idStr);
	List<HandInputProbe> getHandInputProbe();
	List<ProbeMeter> getProbe(int energyType, int subitemType, int type);
	HandInputData findHandInputById(int id);
}

package com.vibe.mapper.energy;

import com.vibe.pojo.KeyValueBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyDao {
	List<KeyValueBean> fenxiangduibiTime(@Param("timeType") String timeType, @Param("time") String time);
	List<KeyValueBean> fenxiangduibiStartEndTime(@Param("timeType") String timeType, @Param("start") String start, @Param("end") String end);
}

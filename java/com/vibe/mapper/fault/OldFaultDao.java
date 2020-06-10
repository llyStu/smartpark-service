package com.vibe.mapper.fault;

import com.vibe.pojo.DailyCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OldFaultDao {

	public void insertFault(DailyCheck fault);

	public void deleteFault(int parseInt);

	public DailyCheck queryFault(int id);

	public List<DailyCheck> queryFaultList();

	public void updateFault(DailyCheck fault);

	public void insertDeviceFault(DailyCheck dailyCheck);

	public void insertFaultDevice(DailyCheck dailyCheck);
	
	public void despatchTo(@Param("id") int id, @Param("personId") int personId);
	public List<DailyCheck> queryDespatchList(@Param("username") String username);
	
	public int queryUnFinishCount(@Param("person") int personId);
	public int queryMonthCount(@Param("person") int personId);
	public int queryTodayCount(@Param("person") int personId);
}

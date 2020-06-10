package com.vibe.mapper.global;

import com.vibe.pojo.AlarmModule;
import com.vibe.pojo.HomeBean;
import com.vibe.pojo.HomeLayoutParameter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeDao {

	public List<HomeBean> getUserHomes(@Param("userId") int userId);
	
	public List<HomeBean> getHomes(@Param("isDefault") int isDefault);
	
	public HomeBean getHome(@Param("homeId") int homeId);
	
	public void deleteUserHomes(@Param("userId") int userId);
	
	public void addUserHomes(@Param("userId") int userId, @Param("userHomeId") int userHomeId);
	
	public List<HomeLayoutParameter> getHomeLayoutParameter(@Param("homeId") int homeId);
	
	public int getAssetCountByCodeId(@Param("codeId") int codeId);

	public int getEnergyAssetCountByCodeId(@Param("codeId") int codeId);

	public List<Integer> getAssetIdByCodeId(@Param("codeId") int codeId);

	public List<Integer> getEnergyAssetIdByCodeId(@Param("codeId") int codeId);

	public int getDoAlarmCount();
	
	public int getUnDoAlarmCount();
	
	public int getUnDoFaultCount();
	
	public int getDoFaultCount();
	
	public int getAlarmCountByCodeId(@Param("codeId") int codeId);
	
	public int getUndoTaskCount();
	
	public int getTotalTaskCount();
	
	public int getMonthDoTaskCount();
	
	public int getTodayDoTaskCount();
	
	public int getMonthTotalTaskCount();
	
	public int getTodayTotalTaskCount();

	int countDoAlarm(@Param("modules") String modules);

	int countAllAlarm(@Param("modules") String modules);

	List<AlarmModule> countAlarmByModules(@Param("modules") String modules);

	List<AlarmModule> countAlarmByIds(@Param("ids") String ids);

	int countAlarm();
}

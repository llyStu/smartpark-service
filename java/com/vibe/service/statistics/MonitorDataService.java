package com.vibe.service.statistics;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.vibe.monitor.asset.Asset;
import com.vibe.pojo.MonitorData;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.util.constant.ResponseModel;

public interface MonitorDataService {

	Map<String, Object> getRecentData(int monitorId, int count);
	 
	Map<String, Object> queryMonitorDataListByPage(CommonMonitorDataVo vo, Integer page, Integer rows);

	MonitorData getOneRecentData(int id);

	
	//相关性分析
	List<Map<String, Object>> queryMonitorComparedListByPage(Integer monitorId1, Integer monitorId2, String startTime,
			String lastTime, Integer page, Integer rows);
	//趋势对比
	public Map<String,Object>  queryMonitorComparedDataByPage(int monitorId1, int monitorId2,
			String startTime2, String lastTime2, Integer page, Integer rows);
	//统计对比
	Map<String, Object> getMonitorComparedDataByPage(String filterType, int monitorId1, int monitorId2, String startTime, String lastTime,
			Integer page, Integer rows);

	Map<String, Object> querySingleMonitorData(CommonMonitorDataVo vo, Integer page, Integer rows);

    Integer getTitleID(Integer parentId);
    
    List<Asset<?>> getAllSpace(Integer parentId);
    
   // Map<String,List<MonitorData>> selectTotalData(Integer parentId);
    //电力
   // List<EnergyData> energyDatac(int parentId,int catalogId);
    
    Map<String,List<Object>> getInfoByMonitorState(String filter, int index);
    
    // EnergyData getEnergyData(Asset<?> asset,Integer catalogId);
     
     //获取单位
     public String getUnit(int monitorId);

   //获取单个监测器自定义时间段数据
     Map<String, Object> queryMonitorData(CommonMonitorDataVo vo, Integer page, Integer rows);	

	List<MonitorData> queryFloatMonitorValues(int monitorId, String startTime, String lastTime);

	List<MonitorData> queryBoolMonitorValues(int monitorId, String startTime, String lastTime);

	List<MonitorData> queryIntMonitorValues(int monitorId, String startTime, String lastTime);

	/**
	 * 通过 监测器的 catalog 和 日期 获取监测器每小时平均值
	 * @param code 监测器类型 catalog
	 * @param date 查询日期
	 * @return
	 * @Author liujingeng
	 * @Date 2020/4/30
	 */
	List<Object> getEnvironmentAvgByCodeDay(Integer code, Date date);

	List<Map<String , Object>> getMonitorCodeName();
}

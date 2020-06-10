package com.vibe.service.energy;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.energy.EnergyCount;
import com.vibe.pojo.energy.EnergyCountOne;
import com.vibe.pojo.energy.EnergyType;
import com.vibe.pojo.energy.RankEnergy;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.TreeNode;

public interface EnergyStatisticsService {
	
	 EasyUIJson queryEnergyDataListByPage(CommonMonitorDataVo vo, Integer page, Integer rows);
	 List<CommonMonitorDataVo> queryEnegyEchart(CommonMonitorDataVo vo);
	 List<CommonMonitorDataVo> getEnegyData(List<CommonMonitorDataVo> vos);
	 Map<String, Object> queryEnergy(CommonMonitorDataVo vo);
	 List<CommonMonitorDataVo> queryEnergyKind(int parent, int catalog);
	 //List<EnergyData> energyCarbon(Integer parentId, Integer catalogId);
	 //Map<String, Object> queryCarbonDataListByPage(CommonMonitorDataVo vo, Integer page, Integer rows);
	// EnergyData getEnergyCarbon(Asset<?> asset, Probe probe);
	//Map<String, Object> queryEnergyStaticMap(CommonMonitorDataVo vo);

	/**
	 * 历史数据
	 * @param vo
	 * @param idList
	 * @return 一个时间段内，详细每个时间的数据集合
	 */
	Map<String, Object> getEnergyByBudings(CommonMonitorDataVo vo, List<Integer> idList);
	/*
	 * 返回建筑一个时间段总的能耗
	 */
	List<CommonMonitorDataVo> getEnergyByBudingsTotal(CommonMonitorDataVo vo, List<Integer> idList);
	List<TreeNode> getRedioData(CommonMonitorDataVo vo);
	List<Map<String, Object>> getHistoryData(CommonMonitorDataVo vo);
	List<TreeNode> getDeviceData(int catalog, int sapceId);
	
////    分时能耗
//	Map<String,Object>getTimeData(CommonMonitorDataVo vo,int ids);
////	分时能耗排序分页
//	Map<String,Object>getTimeDataSeq(CommonMonitorDataVo vo,int ids,int seq,int page,int size);
////	分时能耗导出
//	List<EnergyCount> getTimeDataEx (CommonMonitorDataVo vo,int ids,int seq);
    Map<String, Object> getHistoryTimeData(CommonMonitorDataVo vo, int ids);

	PageInfo<EnergyCount> getHistoryTimeSeqShi(CommonMonitorDataVo vo, int ids, int seq, int page, int size);
	PageInfo<EnergyCount> getHistoryTimeSeqValue(CommonMonitorDataVo vo, int ids, int seq, int page, int size);
	List<EnergyCount> getHistoryTimeSeqShiEx(CommonMonitorDataVo vo, int ids, int seq);
	List<EnergyCount> getHistoryTimeSeqValueEx(CommonMonitorDataVo vo, int ids, int seq);
	Map<String, Object> getHistoryTimeDataTwo(CommonMonitorDataVo vo, int ids);
	Map<String, Object> getHistoryTimeDataOne(CommonMonitorDataVo vo, int ids);
	PageInfo<EnergyCountOne> getHistoryTimeSeqValueXiang(CommonMonitorDataVo vo, int ids, int seq, int page, int size);
	Double queryEnergyZongLiang(CommonMonitorDataVo vo, int ids);
	Double queryEnergyZongLiangDuan(CommonMonitorDataVo vo, int ids);
	List<EnergyCountOne> getHistoryTimeSeqValueExXiang(CommonMonitorDataVo vo, int ids, int seq);
	Map<String, Object>getEnergyRank(CommonMonitorDataVo vo);
//	能源排名-分项展示
	Map<String, Object>getEnergyFenXiang(CommonMonitorDataVo vo);
//	能源的分类
	List<EnergyCountOne> queryEnergyType();

	Map<String, Object> getEnergyElecAndWater(CommonMonitorDataVo vo,String areaStr,String personStr);

	Map<String, Object> getEnergyElecAndWaterRank(CommonMonitorDataVo vo, int page, int size);

	List<RankEnergy> getEnergyAllRank(CommonMonitorDataVo vo, String rankType, String rank);
	
	List<RankEnergy> getEnergyElecAndWaterRankExcle(CommonMonitorDataVo vo);
	
	List<EnergyType> getTypeList();
	
	//List<Object> getEnergyElecAndWaterRankExcel(CommonMonitorDataVo vo);
	Map<String,Object> energyProportion();
}

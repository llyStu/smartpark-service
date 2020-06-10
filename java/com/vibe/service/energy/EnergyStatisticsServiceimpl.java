package com.vibe.service.energy;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vibe.util.ScaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ibm.icu.text.NumberFormat;
import com.vibe.mapper.energy.EnergyCountMapper;
import com.vibe.mapper.energy.EnergyStatisticsMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Probe;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.energy.Biao;
import com.vibe.pojo.energy.Energy;
import com.vibe.pojo.energy.EnergyCount;
import com.vibe.pojo.energy.EnergyCountOne;
import com.vibe.pojo.energy.EnergyType;
import com.vibe.pojo.energy.EnergyZong;
import com.vibe.pojo.energy.RankEnergy;
import com.vibe.service.classification.Code;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.service.statistics.MonitorDataService;
import com.vibe.util.ListSortUtil;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.PropertiesUtil;
import com.vibe.utils.TreeNode;

@Service
public class EnergyStatisticsServiceimpl implements EnergyStatisticsService {

	@Autowired
	private EnergyStatisticsMapper mapper;

	@Autowired
	private MonitorDataService monitorDataService;

	@Autowired
	private EnergyRealTimeService energyRealTimeService;

	@Autowired
	private SelectOptionService selelctOption;

	@Autowired
	private AssetStore assetStore;

	@Autowired
	private EnergyMeterService energyMeterService;
	@Autowired
	private EnergyCountMapper energyCountMapper;

	@Autowired
	private Environment environment;
	/*
	 * 设备对比-设备数据 (non-Javadoc)
	 * 
	 * @see com.vibe.service.energy.EnergyStatisticsService#getDeviceData(int,
	 * int)
	 */
	@Override
	public List<TreeNode> getDeviceData(int catalog, int sapceId) {

		return null;
	}

	/*
	 * 历史对比 (non-Javadoc)
	 * 
	 * @see
	 * com.vibe.service.energy.EnergyStatisticsService#getHistoryData(com.vibe.
	 * pojo.CommonMonitorDataVo)
	 */
	@Override
	public List<Map<String, Object>> getHistoryData(CommonMonitorDataVo vo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> totalMap = getHistoryTotal(vo);
		dataList.add(totalMap);
		Map<String, Object> redioMap = getHistoryRedio(vo);
		dataList.add(redioMap);
		return dataList;
	}

	private Map<String, Object> getHistoryRedio(CommonMonitorDataVo vo) {
		Map<String, Object> redioMap = new HashMap<>();
		List<Object> xAxis = new LinkedList<Object>();
		Integer parentId = monitorDataService.getTitleID(0);
		List<CommonSelectOption> workItems = selelctOption.querySelectOptionListOther(vo.getCatalog(), Code.ENERGY);
		List<Probe> probes = energyRealTimeService.getSpaceTotalEnergyProbe(parentId,
				energyRealTimeService.getCatalogIds(workItems));
		for (CommonSelectOption item : workItems) {
			xAxis.add(item.getName());
		}
		List<String> asList = Arrays.asList(vo.getStartTime(), vo.getLastTime());
		int i = 1;
		for (String nowTime : asList) {
			List<Object> redioValues = getRedioValues(vo, probes, nowTime);
			redioMap.put("value" + i, redioValues);
			i++;
		}
		redioMap.put("yunit", energyRealTimeService.getUnitByCatalog((int) vo.getCatalog(), Code.ENERGY));
		redioMap.put("xunit", "分项类别");
		redioMap.put("xAxis", xAxis);
		redioMap.put("flag", asList);
		return redioMap;
	}

	private List<Object> getRedioValues(CommonMonitorDataVo vo, List<Probe> probes, String nowTime) {
		List<Object> values = new LinkedList<Object>();
		for (Probe probe : probes) {
			Double itemValue = mapper.queryRealTimeTotal(probe.getId(), vo.getTableName(), vo.getSubNum(), nowTime);
			values.add(itemValue == null ? "_" :itemValue );
		}
		return values;
	}

	/*
	 * 
	 */
	private Map<String, Object> getHistoryTotal(CommonMonitorDataVo vo) {
		Map<String, Object> totalMap = new HashMap<>();
		Integer probeId = getTotalEnergyProbeId(vo.getCatalog());
		String tableName = vo.getTableName();
		int subNum = vo.getSubNum();
		List<Object> xAxis = new ArrayList<Object>();
		List<String> asList = Arrays.asList(vo.getStartTime(), vo.getLastTime());
		int i = 1;
		for (String nowTime : asList) {
			List<CommonMonitorDataVo> data = mapper.queryRealTime(probeId, tableName, subNum, nowTime);
			List<Object> value1 = getValuesList(vo, xAxis, data,vo.getDateContrastFormat());
			totalMap.put("value" + i, value1);
			i++;
		}
		totalMap.put("yunit", energyRealTimeService.getUnitByCatalog((int) vo.getCatalog(), Code.ENERGY));
		totalMap.put("xunit", vo.getUnit());
		totalMap.put("xAxis", xAxis);
		totalMap.put("flag", asList);
		return totalMap;
	}

	private List<Object> getValuesList(CommonMonitorDataVo vo, List<Object> xAxis,
                                       List<CommonMonitorDataVo> monitorDatas, String dataFormat) {
		List<Object> values = new LinkedList<Object>();
		for (CommonMonitorDataVo data : monitorDatas) {
			values.add(data.getValue() == null ? "_" : data.getValue());
			SimpleDateFormat dateFormatter = new SimpleDateFormat(dataFormat);
			String xValue = dateFormatter.format(data.getMoment());
			if (xAxis != null && !xAxis.contains(xValue)) {
				xAxis.add(xValue);
			}
		}
		return values;
	}

	// 分项数据
	@Override
	public List<TreeNode> getRedioData(CommonMonitorDataVo vo) {
		List<TreeNode> itemsTree = selelctOption.getItemsTree(vo.getCatalog(), Code.ENERGY);
		CommonSelectOption option = selelctOption.getSelectOption(vo.getCatalog(), Code.ENERGY);
		Double totalEnergy = getTotalEnergy(vo);
		TreeNode treeNode = new TreeNode();
		treeNode.setId(option.getId());
		treeNode.setText(option.getName());
		treeNode.setNodes(itemsTree);
		List<TreeNode> arryList = new ArrayList<TreeNode>();
		arryList.add(treeNode);
		loadTreeEnergy(arryList, vo, totalEnergy);
		return arryList;
	}

	public Double getTotalEnergy(CommonMonitorDataVo vo) {
		Integer probeId = getTotalEnergyProbeId(vo.getCatalog());
		if (probeId == null) {
			return null;
		}
		Object value = mapper.queryValueByTime(probeId, vo.getStartTime(), vo.getLastTime(), vo.getTableName(),vo.getSubNum());
		if (value == null) {
			return null;
		}
		return (Double) value;
	}

	public void loadTreeEnergy(List<TreeNode> itemsTree, CommonMonitorDataVo vo, Double total) {
		if (itemsTree != null && itemsTree.size() > 0) {
			for (TreeNode treeNode : itemsTree) {
				Integer probeId = getTotalEnergyProbeId(treeNode.getId());
				if (probeId != null) {
					Object value = mapper.queryValueByTime(probeId, vo.getStartTime(), vo.getLastTime(),
							vo.getTableName(),vo.getSubNum());
					
					if (value != null) {
						treeNode.setValue(value);
						if (total != null && total != 0) {
							double redio = (Double) value / total * 100;
							treeNode.setRedio(redio+"");
						}
					}
					/*System.out.println("kaishi=" + probeId + "     " + vo.getStartTime() + "     " + vo.getLastTime()
						+ "     " + vo.getTableName());
					System.out.println(value + "-----" + treeNode.getRedio());*/
				}
				loadTreeEnergy(treeNode.getNodes(), vo, total);
			}
		}

	}

	private Integer getTotalEnergyProbeId(Integer catalogId) {
		if (catalogId == null) {
			return null;
		}
		Integer parentId = monitorDataService.getTitleID(0);
		Integer probeId = getProbeIdByparentAndCatlog(parentId, catalogId);
		return probeId;
	}

	private Integer getProbeIdByparentAndCatlog(Integer parentId, Integer catalog) {
		Asset<?> asset = assetStore.findAsset(parentId);
		if (asset != null && asset.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) asset;
			Collection<Asset<?>> children = parent.children();
			for (Asset<?> child : children) {
				if (child instanceof Probe) {
					Probe probe = (Probe) child;
					Integer itemize = energyMeterService.queryItemize(probe.getId());
					if (itemize != null && itemize == (int) catalog) {
						return probe.getId();
					}
				}
			}
		}
		return null;
	}
	@Override
	public List<CommonMonitorDataVo> getEnergyByBudingsTotal(CommonMonitorDataVo vo, List<Integer> idList) {
		List<CommonMonitorDataVo> dataList = new ArrayList<CommonMonitorDataVo>();
		for (Integer spaceId : idList) {
			Asset<?> asset = assetStore.findAsset(spaceId);
			queryEnergyData(dataList, vo, asset);
		}
		Double total = 0d;
		for (CommonMonitorDataVo commonMonitorDataVo : dataList) {
			Object value = commonMonitorDataVo.getValue();
			if(value != null && !value.equals("")){
				Double dvalue = (Double)value;
				total = total+dvalue;
			}
		}
		for (CommonMonitorDataVo monitorValue : dataList) {
			Object value = monitorValue.getValue();
			if(value != null && !value.equals("") && total != 0){
				Double dvalue = (Double)value;
				double formatDoubleTwo = dvalue/total*100;
				monitorValue.setRedioValue(String.format("%.1f", formatDoubleTwo));
			}
		}
		return dataList;
	}
	// 历史数据建筑对比
	@Override
	public Map<String, Object> getEnergyByBudings(CommonMonitorDataVo vo, List<Integer> idList) {
		Map<String, Object> totalMap = new HashMap<>();
		List<Object> values = new LinkedList<Object>();
		List<Object> xValues = new LinkedList<Object>();
		List<Object> flags = new LinkedList<Object>();
		for (Integer spaceId : idList) {
			Asset<?> asset = assetStore.findAsset(spaceId);
			List<CommonMonitorDataVo> data = queryBudingEnergyData(vo, asset);
			List<Object> value = getValuesList(vo, xValues, data,vo.getDateFormat());
			values.add(value);
		}
		totalMap.put("yunit", energyRealTimeService.getUnitByCatalog((int) vo.getCatalog(), Code.ENERGY));
		totalMap.put("xunit", vo.getUnit());
		totalMap.put("xAxis", xValues);
		totalMap.put("yAxis", values);
		totalMap.put("flag", flags);
		return totalMap;
	}
	public List<CommonMonitorDataVo> queryBudingEnergyData(CommonMonitorDataVo vo, Asset<?> asset) {
		List<CommonMonitorDataVo> dataList = new ArrayList<CommonMonitorDataVo>();
		if (asset != null && asset.isCompound()) {
			CompoundAsset<?> parent2 = (CompoundAsset<?>) asset;
			Collection<Asset<?>> children2 = parent2.children();
			for (Asset<?> asset2 : children2) {
				if (asset2 != null && asset2 instanceof Probe) {
					Probe probe = (Probe) asset2;
					int probeId = probe.getId();
					Integer itemize = energyMeterService.queryItemize(probeId);
					if (itemize != null && itemize == vo.getCatalog()) {
						Integer redioType = vo.getRedioType();
						String tableName = vo.getTableName();
						String startTime = vo.getStartTime();
						String lastTime = vo.getLastTime();
						int subNum = vo.getSubNum();
						if(redioType == null){

						}else if(redioType == 0){
							dataList = mapper.queryRealTime(probeId, tableName, subNum, startTime);
						}else if(redioType == 1){
							dataList = mapper.queryValueList(probeId, startTime, lastTime, tableName, subNum);
						}
						
					}
				}
			}
		}
		return dataList;
	}

	@Override
	public EasyUIJson queryEnergyDataListByPage(CommonMonitorDataVo vo, Integer page, Integer rows) {
		Integer id = 0;
		if (vo != null && vo.getId() != null) {
			id = vo.getId();
		}
		PageHelper.startPage(page, rows);
		List<CommonMonitorDataVo> totalList = setEnegyTotalList(id, vo);
		PageInfo<CommonMonitorDataVo> pageInfo = new PageInfo<CommonMonitorDataVo>(totalList);
		EasyUIJson uiJson = new EasyUIJson();
		uiJson.setTotal(pageInfo.getTotal());
		uiJson.setRows(totalList);

		return uiJson;
	}

	/*
	 * 查询能耗统计分类数据 参数：catalog ,(startTime,lastTime,id)
	 */
	@Override
	public Map<String, Object> queryEnergy(CommonMonitorDataVo vo) {
		List<CommonMonitorDataVo> queryEnegyEchart = queryEnegyEchart(vo);
		List<CommonMonitorDataVo> queryEnegy = getEnegyData(queryEnegyEchart);
		Map<String, Object> map = setGraphMap(queryEnegy,vo);
		return map;
	}

	private Map<String, Object> setGraphMap(List<CommonMonitorDataVo> list, CommonMonitorDataVo vo) {

		List<Object> xAxis = new LinkedList<Object>();
		List<Object> values = new LinkedList<Object>();
		if (list != null) {
			for (CommonMonitorDataVo monitorData : list) {
				Object value = monitorData.getValue();
				Double dvalue = (Double) value;
				if (dvalue != null) {
					String doubleValue = dvalue+"";
					values.add(doubleValue);
				}
				xAxis.add(monitorData.getName());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xAxis", xAxis);
		map.put("values", values);
		return map;
	}

	public List<CommonMonitorDataVo> getEnegyData(List<CommonMonitorDataVo> vos) {

		Collections.sort(vos, new Comparator<CommonMonitorDataVo>() {

			@Override
			public int compare(CommonMonitorDataVo o1, CommonMonitorDataVo o2) {
				if ((double) o1.getValue() > (double) o2.getValue()) {
					return -1;
				}
				if ((double) o1.getValue() == (double) o2.getValue()) {
					return 0;
				}
				return 1;
			}

		});
		return vos;
	}

	public void queryEnergyData(List<CommonMonitorDataVo> dataList, CommonMonitorDataVo vo, Asset<?> asset) {

		if (asset != null && asset.isCompound()) {
			CompoundAsset<?> parent2 = (CompoundAsset<?>) asset;
			Collection<Asset<?>> children2 = parent2.children();

			for (Asset<?> asset2 : children2) {
				if (asset2 != null && asset2 instanceof Probe) {
					Probe probe = (Probe) asset2;
					Integer itemize = energyMeterService.queryItemize(probe.getId());
					String tableName = vo.getTableName();
					String startTime = vo.getStartTime();
					String lastTime = vo.getLastTime();
					int subNum = vo.getSubNum();
					if (itemize != null && itemize == vo.getCatalog()) {
						CommonMonitorDataVo monitorData = new CommonMonitorDataVo();
						monitorData.setName(asset.getCaption());
						
						Object value = mapper.queryValueByTime(probe.getId(), startTime, lastTime,tableName,subNum);
						System.out.println( value+"   "+probe.getId() +"   "+ startTime  +"   "+ lastTime  +"   "+ vo.getTableName());
						if (value != null) {
							monitorData.setValue(value);
						} else {
							monitorData.setValue("");
						}
						dataList.add(monitorData);
					}
				}
			}
		}

	}

	public Asset<?> getTotalBudingSpace(CommonMonitorDataVo vo) {
		Integer id = 0;
		if (vo != null && vo.getId() != null) {
			id = vo.getId();
		}
		Integer parentId = monitorDataService.getTitleID(id);
		return assetStore.findAsset(parentId);
	}

	@Override
	public List<CommonMonitorDataVo> queryEnegyEchart(CommonMonitorDataVo vo) {

		List<CommonMonitorDataVo> dataList = new ArrayList<CommonMonitorDataVo>();
		Asset<?> root = getTotalBudingSpace(vo);
		// 获取空间下监测器为总能耗的值
		if (root != null && root.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) root;
			Collection<Asset<?>> children = parent.children();
			for (Asset<?> asset : children) {
				queryEnergyData(dataList, vo, asset);
			}
		}
		return dataList;
	}

	// 根据能源的类型，查询
	private List<CommonMonitorDataVo> setEnegyTotalList(Integer id, CommonMonitorDataVo vo) {

		List<CommonMonitorDataVo> dataList = new ArrayList<CommonMonitorDataVo>();

		List<Asset<?>> allSpace = monitorDataService.getAllSpace(id);
		// 获取空间下监测器为总能耗的值
		if (allSpace != null) {
			for (Asset<?> root : allSpace) {
				if (root != null && root.isCompound()) {
					CompoundAsset<?> parent = (CompoundAsset<?>) root;
					Collection<Asset<?>> children = parent.children();
					CommonMonitorDataVo monitorData = new CommonMonitorDataVo();
					monitorData.setName(root.getCaption());

					for (Asset<?> asset : children) {

						if (asset != null && asset instanceof Probe) {
							Probe probe = (Probe) asset;
							monitorData.setMonitorId(probe.getId());
							monitorData.setMoment(new Date());
							String startTime = vo.getStartTime();
							String lastTime = vo.getLastTime();
							Object value = mapper.queryValueByTime(probe.getId(), startTime, lastTime,
									"t_sample_float",vo.getSubNum());
							String doubleValue = "";
							if (value != null) {
								Double dvalue = (Double) value;
								doubleValue = dvalue+"";
								//doubleValue = new UseCode().getValue(dvalue,(short)Code.PROBE,probe.getMonitorKind());
							}
							int catalogId = vo.getCatalog() == null ? Code.ENERGY_ElECTRICITY : vo.getCatalog();
							Integer itemize = energyMeterService.queryItemize(probe.getId());
							if (itemize != null) {
								if (itemize == catalogId) {
									monitorData.setValue(doubleValue);
									break;
								} else if (itemize == 37) {
									monitorData.setWaterValue(doubleValue);
									break;
								} else if (itemize == 38) {
									monitorData.setGasValue(doubleValue);
									break;
								} else {
									monitorData.setValue("");
									monitorData.setWaterValue("");
									monitorData.setGasValue("");
								}
							}
						}

					}
					dataList.add(monitorData);
				}
			}
		}
		return dataList;
	}

	/* 根据建筑id,获取能源分类数据 **/
	@Override
	public List<CommonMonitorDataVo> queryEnergyKind(int parentId, int catalog) {
		ArrayList<CommonMonitorDataVo> arrayList = new ArrayList<CommonMonitorDataVo>();
		Asset<?> root = assetStore.findAsset(parentId);
		if (root != null && root.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) root;
			Collection<Asset<?>> children = parent.children();
			for (Asset<?> asset : children) {
				List<CommonMonitorDataVo> vo = mapper.queryKindValue(asset.getId(), catalog);
				CommonMonitorDataVo monitorDataVo = null;
				if (vo != null && vo.size() > 0) {
					monitorDataVo = vo.get(0);
				}
				arrayList.add(monitorDataVo);
			}
		}
		return arrayList;
	}
//	
	@Override
	public Map<String, Object> getHistoryTimeData(CommonMonitorDataVo vo, int ids) {
			int catalog = vo.getCatalog();
			String startTimeOne = vo.getStartTime();
			String startTime = startTimeOne+"%";
			String lastTime = vo.getLastTime();
			String tableName = vo.getTableName();
			int redioType = vo.getRedioType();
			List<Float> avg = new ArrayList<Float>();
			List<Date> shijian = new ArrayList<Date>();
			Date time  = null;
			Float value = null;
			String danwei = null;
			Map<String, Object> map = new HashMap<>();
//			if(redioType==0){
//				
//				List<EnergyCount> ec = mapper.queryFenShiEnergyTwo(ids, startTime, tableName, catalog);
//				for (EnergyCount energyCount : ec) {
//					value=energyCount.getValue();
//					avg.add(value);
//					time = energyCount.getTime();
//					shijian.add(time);
//					danwei = energyCount.getUnit();
//				}
//				
//				map.put("values",avg );
//				map.put("xAxis", shijian);
//				map.put("time", "时间");
//				map.put("unit", danwei);
//			}else{
//				List<EnergyCount> ec = mapper.queryFenShiEnergy(ids, startTimeOne, lastTime, tableName, catalog);
//				for (EnergyCount energyCount : ec) {
//					value=energyCount.getValue();
//					avg.add(value);
//					time = energyCount.getTime();
//					shijian.add(time);
//					danwei = energyCount.getUnit();
//				}
//				
//				map.put("values",avg );
//				map.put("xAxis", shijian);
//				map.put("time", "时间");
//				map.put("unit", danwei);
//			}
			return map;
	}

	@Override
	public PageInfo<EnergyCount> getHistoryTimeSeqShi(CommonMonitorDataVo vo, int ids, int seq, int page, int size) {
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType ==0){
		PageHelper.startPage(page, size);
		List<EnergyCount> ec = mapper.queryFenShiEnergySeqShi(ids, startTime, tableName, catalog);
		PageInfo<EnergyCount> pageInfo = new PageInfo<EnergyCount>(ec);
		return pageInfo;
		}else{
			PageHelper.startPage(page, size);
			List<EnergyCount> ec = mapper.queryFenShiEnergySeqShiOne(ids, startTime1,lastTime, tableName, catalog);
			PageInfo<EnergyCount> pageInfo = new PageInfo<EnergyCount>(ec);
			return pageInfo;
		}
	}

	@Override
	public PageInfo<EnergyCount> getHistoryTimeSeqValue(CommonMonitorDataVo vo, int ids, int seq, int page, int size) {
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType == 0){
		PageHelper.startPage(page, size);
		List<EnergyCount> ec = mapper.queryFenShiEnergySeqValue(ids, startTime, tableName, catalog);
		PageInfo<EnergyCount> pageInfo = new PageInfo<EnergyCount>(ec);
		return pageInfo;
		}else{
			PageHelper.startPage(page, size);
			List<EnergyCount> ec = mapper.queryFenShiEnergySeqValueOne(ids, startTime1, lastTime,tableName, catalog);
			PageInfo<EnergyCount> pageInfo = new PageInfo<EnergyCount>(ec);
			return pageInfo;
		}
	}

	@Override
	public List<EnergyCount> getHistoryTimeSeqShiEx(CommonMonitorDataVo vo, int ids, int seq) {
		List<EnergyCount> ec = new ArrayList<EnergyCount>();
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType==0){
			ec = mapper.queryFenShiEnergySeqShi(ids, startTime, tableName, catalog);
			}else{
			 ec = mapper.queryFenShiEnergySeqShiOne(ids, startTime1, lastTime, tableName, catalog);
			}
		return ec;
	}

	@Override
	public List<EnergyCount> getHistoryTimeSeqValueEx(CommonMonitorDataVo vo, int ids, int seq) {
		List<EnergyCount> ec = new ArrayList<EnergyCount>();
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType==0){
		ec = mapper.queryFenShiEnergySeqValue(ids, startTime, tableName, catalog);
		}else{
		 ec = mapper.queryFenShiEnergySeqValueOne(ids, startTime1, lastTime, tableName, catalog);
		}
		return ec;
	}

	@Override
	public Map<String, Object> getHistoryTimeDataTwo(CommonMonitorDataVo vo, int ids) {

		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType==1 && vo.getSelectType()==2) {
			lastTime = lastTime+"-01";
			startTime1 = startTime1+"-01";
		}
		List<Double> avg = new ArrayList<Double>();
		List<String> nameList = new ArrayList<String>();
		List<String> baifenbi = new ArrayList<String>();
		String name  = null;
		Double value = 0.0d;
		Double shuzhi = null;
		String danwei = null;
		String ming  = null;
		Double sumValue = 0.0d;
		Map<String, Object> map = new HashMap<>();
		if(redioType==0){
			List<EnergyCountOne> ec = mapper.queryFenXiangEnergy(ids, startTime, tableName, catalog);
			int count  = ec.size();
			for (EnergyCountOne energyCountOne : ec) {
				value=energyCountOne.getAvg();
				sumValue =(double)(sumValue + value);
				avg.add(value);
				danwei = energyCountOne.getUnit();
				name = energyCountOne.getName();
				nameList.add(name);
			}
//			Double valueZong  = (double)(mapper.queryEnergyZongLiang(ids, startTime, tableName, catalog));
			for(int i =0 ;i<avg.size();i++){
				shuzhi  = (double)avg.get(i);
				Double ee = (double)(shuzhi/sumValue*100);
				String t = String.format("%.1f", ee);
				String tt = t+"%";
				baifenbi.add(tt);
			}
			map.put("unit", danwei);
			map.put("value",avg );
			map.put("categoryName", nameList);
			map.put("percentage", baifenbi);
		}else{
			List<EnergyCountOne> ec = mapper.queryFenXiangEnergyDuan(ids, startTime1, lastTime,tableName , catalog);
			for (EnergyCountOne energyCountOne : ec) {
				value=energyCountOne.getAvg();
				sumValue =sumValue + value;
				avg.add(value);
				danwei = energyCountOne.getUnit();
				name = energyCountOne.getName();
				nameList.add(name);
			}
//			Double valueZong1  = (double)mapper.queryEnergyZongLiangDuan(ids, startTime1, lastTime,tableName,  catalog);
			for(int i =0 ;i<avg.size();i++){
				shuzhi  = (double) avg.get(i);
				Double ee =(double) (shuzhi/sumValue*100);
				String t = String.format("%.1f", ee);
				String tt = t+"%";
				baifenbi.add(tt);
			}
			map.put("unit", danwei);
			map.put("value",avg );
			map.put("categoryName", nameList);
			map.put("percentage", baifenbi);
			
		}
		return map;

	
	}

	@Override
	public Map<String, Object> getHistoryTimeDataOne(CommonMonitorDataVo vo, int ids) {
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		List<Double> avg = new ArrayList<Double>();
		List<String> nameList = new ArrayList<String>();
		String name  = null;
		Double value = null;
		String danwei = null;
		Map<String, Object> map = new HashMap<>();
		if(redioType==0){
			List<EnergyCountOne> ec = mapper.queryFenXiangEnergy(ids, startTime, tableName, catalog);
			for (EnergyCountOne energyCountOne : ec) {
				value=energyCountOne.getAvg();
				avg.add(value);
				danwei = energyCountOne.getUnit();
				name = energyCountOne.getName();
				nameList.add(name);
			}
			map.put("value",avg );
			map.put("categoryName", nameList);
			map.put("unit", danwei);
		}else{
			List<EnergyCountOne> ec = mapper.queryFenXiangEnergyDuan(ids, startTime1, lastTime, tableName, catalog);
			
			for (EnergyCountOne energyCountOne : ec) {
				value=energyCountOne.getAvg();
				avg.add(value);
				danwei = energyCountOne.getUnit();
				name = energyCountOne.getName();
				nameList.add(name);
			}
			map.put("value",avg );
			map.put("categoryName", nameList);
			map.put("unit", danwei);
		}
		return map;
	
	}

	@Override
	public PageInfo<EnergyCountOne> getHistoryTimeSeqValueXiang(CommonMonitorDataVo vo, int ids, int seq, int page, int size) {

		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType==1 && vo.getSelectType()==2) {
			lastTime = lastTime+"-01";
			startTime1 = startTime1+"-01";
		}
		
		
		if(redioType ==0){
		PageHelper.startPage(page, size);
		List<EnergyCountOne> ec = mapper.queryFenXiangEnergySeqValue(ids, startTime, tableName, catalog);
		PageInfo<EnergyCountOne> pageInfo = new PageInfo<EnergyCountOne>(ec);
		return pageInfo;
		}else{
			PageHelper.startPage(page, size);
			List<EnergyCountOne> ec = mapper.queryFenXiangEnergySeqValueDuan(ids, startTime1, lastTime,tableName, catalog);
			PageInfo<EnergyCountOne> pageInfo = new PageInfo<EnergyCountOne>(ec);
			return pageInfo;
		}
	
	}

	@Override
	public List<EnergyCountOne> getHistoryTimeSeqValueExXiang(CommonMonitorDataVo vo, int ids, int seq) {

		List<EnergyCountOne> ec = new ArrayList<EnergyCountOne>();
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
		int redioType = vo.getRedioType();
		if(redioType==0){
		ec = mapper.queryFenXiangEnergySeqValue(ids, startTime, tableName, catalog);
		}else{
		 ec = mapper.queryFenXiangEnergySeqValueDuan(ids, startTime1, lastTime, tableName, catalog);
		}
		return ec;
		
	
	}

	@Override
	public Double queryEnergyZongLiang(CommonMonitorDataVo vo, int ids) {
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
		String startTime = startTime1+"%";
//		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
//		int redioType = vo.getRedioType();
		return mapper.queryEnergyZongLiang(ids, startTime, tableName, catalog);
		
	}

	@Override
	public Double queryEnergyZongLiangDuan(CommonMonitorDataVo vo, int ids) {
		int catalog = vo.getCatalog();
		String startTime1 = vo.getStartTime();
//		String startTime = startTime1+"%";
		String lastTime = vo.getLastTime();
		String tableName = vo.getTableName();
//		int redioType = vo.getRedioType();
		return mapper.queryEnergyZongLiangDuan(ids, startTime1, lastTime, tableName, catalog);
		
	}
	@Override
	public Map<String, Object> getEnergyRank(CommonMonitorDataVo vo) {
		int catalog = vo.getCatalog();
		int redioType = vo.getRedioType();
		String lastTime = vo.getLastTime();
		String startTime= vo.getStartTime();
		String tableName = vo.getTableName();
		String startTime1 = startTime+"%";
		Double avg = 0.0d;
		String danwei = null;
		List<String> unitList = new ArrayList<String>();
		List<Double> avgList = new ArrayList<Double>();
		Map<String, Object> map = new HashMap<>();
		List<Integer> lightList= mapper.queryEnergyZongId(catalog);
//		 ez = new ArrayList<EnergyZong>();
//		 EnergyZongDuan =  mapper.queryEnergyZongDuan(lightList, startTime, lastTime, tableName);
		if(redioType == 0){
			List<EnergyZong> ez = mapper.queryEnergyZong(lightList, startTime1, tableName);
			for (EnergyZong energyZong2 : ez) {
				if(danwei==null){
				danwei = null;
				avg    = null;
				unitList.add(danwei);
				avgList.add(avg);
				}else{
					danwei = energyZong2.getUnit();
					avg    = energyZong2.getValue();
					unitList.add(danwei);
					avgList.add(avg);
				}
			}
		}else{
			List<EnergyZong> EnergyZongDuan = mapper.queryEnergyZongDuan(lightList, startTime, lastTime, tableName);
			for (EnergyZong energyZongDuan2 : EnergyZongDuan) {
			danwei = energyZongDuan2.getUnit();
			avg    = energyZongDuan2.getValue();
			unitList.add(danwei);
			avgList.add(avg);
			}
		}
		map.put("values", avgList);
		map.put("unit", danwei);
		return map;
	}

	@Override
	public Map<String, Object> getEnergyFenXiang(CommonMonitorDataVo vo) {
		int catalog = vo.getCatalog();
		int redioType = vo.getRedioType();
		String lastTime = vo.getLastTime();
		String startTime= vo.getStartTime();
		String tableName = vo.getTableName();
		String startTime1 = startTime+"%";
		Double avg = null;
		String danwei = null;
		String name = null;
		Double sum = null;
		Double sumDuan = null;
		List<String> nameList = new ArrayList<String>();
		List<Double> avgList = new ArrayList<Double>();
		Map<String, Object> map = new HashMap<>();
		List<Integer> lightList= mapper.queryEnergyZongId(catalog);
//		List<EnergyCountOne> ec = mapper.queryEnergyZongFenXiang(lightList, startTime1, tableName);
		List<EnergyZong> EnergyZongDuan =  mapper.queryEnergyZongDuan(lightList, startTime, lastTime, tableName);
		sumDuan = EnergyZongDuan.get(0).getValue();
		List<String> baifenbi = new ArrayList<String>();
		List<EnergyZong> EnergyZong =  mapper.queryEnergyZong(lightList, startTime1, tableName);
		sum = EnergyZong.get(0).getValue();
		if(redioType == 0){
		List<EnergyCountOne> ec = mapper.queryEnergyZongFenXiang(lightList, startTime1, tableName);
		
			for (EnergyCountOne energyCountOne : ec) {
				avg = energyCountOne.getAvg();
				Double ee = (double)(avg/sum*100);
				String percentage = String.format("%.1f", ee);
				String tt = percentage+"%";
				baifenbi.add(tt);
				avgList.add(avg);
				name = energyCountOne.getName();
				nameList.add(name);
				danwei = energyCountOne.getUnit();
				
			}
		}else{
			List<EnergyCountOne> eco = mapper.queryEnergyZongFenXiangDuan(lightList, startTime, lastTime, tableName);
			for (EnergyCountOne energyCountOne : eco) {
				avg = energyCountOne.getAvg();
				Double ee = (double)(avg/sumDuan*100);
				String percentage = String.format("%.1f", ee);
				String tt = percentage+"%";
				baifenbi.add(tt);
				avgList.add(avg);
				name = energyCountOne.getName();
				nameList.add(name);
				danwei = energyCountOne.getUnit();
			}
		}
		map.put("values", avgList);
		map.put("categoryName", nameList);
		map.put("unit", danwei);
		map.put("percentage", baifenbi);
		map.put("total", nameList.size());
		return map;
	}

	@Override
	public List<EnergyCountOne> queryEnergyType() {
//		int catalog = vo.getCatalog();
		List<Integer> lightList = mapper.queryEnergyTypeId();
		return mapper.queryEnergyType(lightList);
	}

	@Override
	public Map<String, Object> getEnergyElecAndWater(CommonMonitorDataVo vo,String areaStr,String personStr) {
		Map<String ,Object> map = new HashMap<>();
		//int catalog = vo.getCatalog();
		int redioType = vo.getRedioType();
		String lastTime = vo.getLastTime();
		String startTime= vo.getStartTime();
		String tableName = vo.getTableName();
		if(redioType==1 && vo.getSelectType()==2) {  //范围 月份 拼接
			lastTime = lastTime+"-01";
			startTime = startTime+"-01";
		}
		if(redioType==0 && vo.getSelectType()==3) {  //查询天的总量直接从天表查
			tableName= "t_sample_float_day";
		}

		//水电一起查 总量  水是37  电是 34
		double elecSum = 0.0;
		double waterSum = 0.0;
		List<Energy> all = mapper.getEnergySumAll(redioType,startTime,lastTime,tableName);
		if(!all.isEmpty() && all != null) {
			for(Energy lall : all) {
				if(lall != null) {
					if(lall.getNameId() == 34) {
						elecSum = Double.parseDouble(lall.getValue().toString());
					}
					if(lall.getNameId() == 37 || lall.getNameId() == 50) {
						waterSum = Double.parseDouble(lall.getValue().toString());
					}
				}
			}
		}
		//查询元饼图的占比
		List<Integer> elec= mapper.queryEnergyZongId(34);//查询水电子项
		List<Integer> water= mapper.queryEnergyZongId(37);//查询水电子项
		List<Energy> elecList = new ArrayList<>();
		List<Energy> waterList = new ArrayList<>();
		if(!elec.isEmpty() && elec != null) {//查询各子类电和
			for(Integer id : elec) {
				Energy elecAvg = mapper.queryEnergyAllElec(id,redioType, startTime, lastTime, tableName);//时间段总和
				//计算百分比
				if(elecAvg != null) {
					double result = Double.parseDouble(elecAvg.getValue().toString())/elecSum;
					elecAvg.setZhanbi(ScaleUtil.getRate(result));
					elecList.add(elecAvg);	
				}
			}
		}
		if(!water.isEmpty() && water != null) {//查询各子类水和
			for(Integer id : water) {
				Energy waterAvg = mapper.queryEnergyAllWater(id,redioType, startTime, lastTime, tableName);
				//计算百分比
				if(waterAvg!=null){
					double result = Double.parseDouble(waterAvg.getValue().toString())/elecSum;
					waterAvg.setZhanbi(ScaleUtil.getRate(result));
					waterList.add(waterAvg);
				}
			}
		}
//		String areaStr = environment.getProperty("area");// PropertiesUtil.getPropertiesValue("area");
//		String personStr = environment.getProperty("person");// PropertiesUtil.getPropertiesValue("person");
		int person = Integer.parseInt(personStr);
		double area = Double.parseDouble(areaStr);
		double elecAvgPerson = elecSum/person;
		double elecArea = elecSum/area;
		double waterAvgPerson = waterSum/person;
		double waterArea = waterSum/area;

		if (elecList.size() == 0) {
			elecList.add(new Energy("综合用电", "千瓦", 0.0, "0"));
		}



		//封装map
		map.put("person", person);
		map.put("area", area);
		map.put("elecAvgPerson", ScaleUtil.getRate(elecAvgPerson));
		map.put("elecArea", ScaleUtil.getRate(elecArea));
		map.put("waterAvgPerson", ScaleUtil.getRate(waterAvgPerson));
		map.put("waterArea", ScaleUtil.getRate(waterArea));
		map.put("elecSum", ScaleUtil.getRate(elecSum));
		map.put("waterSum", ScaleUtil.getRate(waterSum));
		map.put("elecList", elecList);
		map.put("waterList", waterList);
		
		return map;
	}

	@Override
	public Map<String, Object> getEnergyElecAndWaterRank(CommonMonitorDataVo vo, int page, int size) {
		int redioType = vo.getRedioType();
		String lastTime = vo.getLastTime();
		String startTime= vo.getStartTime();
		String tableName = vo.getTableName();
		if(redioType==1 && vo.getSelectType()==2) {  //范围 月份 拼接
			lastTime = lastTime+"-01";
			startTime = startTime+"-01";
		}
		double elecSum = 0.0;
		double waterSum = 0.0;
		double elecAndWaterSum = 0.0;
		int parentId = 0;
		//根据楼层去查询楼层数据
		
		List<Object> cloumnList = new ArrayList<>();
		List<Energy> floorId = energyCountMapper.getEnergyA3Floorid();//获取A3楼所有层数
		if(!floorId.isEmpty() && floorId != null) {//电
			for(Energy id : floorId) {
				List<Object> rowList = new ArrayList<>();
				int xulie=1;
				int spaceId = id.getFloorId();
				parentId = 34;
				List<EnergyCountOne> floorElecList =  mapper.queryEnergyBySpaceId(parentId,spaceId,redioType, startTime, lastTime, tableName);
				//和
				if(!floorElecList.isEmpty() && floorElecList != null) {
					for(int i = 0;i<floorElecList.size();i++) {
						elecSum = elecSum + floorElecList.get(i).getAvg();//每层楼的总电和
					}
					rowList.add(floorElecList.get(0).getCaption());
					rowList.add(elecSum);
					rowList.add(floorElecList);
				}
				parentId = 37;
				List<EnergyCountOne> floorWaterList =  mapper.queryEnergyBySpaceId(parentId,spaceId,redioType, startTime, lastTime, tableName);
				//和
				if(!floorWaterList.isEmpty() && floorWaterList != null) {
					for(int i = 0;i<floorWaterList.size();i++) {
						waterSum = waterSum + floorWaterList.get(i).getAvg();//每层楼的总电和
					}
//					rowList.add(waterSum);
					rowList.add(floorWaterList);
				}
				elecAndWaterSum = elecSum + waterSum;
				if(rowList.size()>0) {
					rowList.add(1, elecAndWaterSum);
					rowList.add(0, xulie);
					rowList.add(4,waterSum);
				}
				xulie++;
				cloumnList.add(rowList);
			}
		}
	
		//分页
		Map<String,Object> map = new HashMap<>();
		int total=0;
		if(cloumnList != null && !cloumnList.isEmpty()) {
			total = cloumnList.size();
			int pagesize = size; //每页条数 
			int currentPage= page;//第几页 传参page
			int totalcount = cloumnList.size();
	        int pagecount = 0; //总页数
	        List<Object> subList;
	        int m = totalcount % pagesize;
	        if (m > 0) {
	            pagecount = totalcount / pagesize + 1;
	        } else {
	            pagecount = totalcount / pagesize;
	        }
	        if (m == 0) {
	            subList = cloumnList.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
	        } else {
	            if (currentPage == pagecount) {
	                subList = cloumnList.subList((currentPage - 1) * pagesize, totalcount);
	            } else {
	                subList = cloumnList.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
	            }
	        }
	        map.put("total", cloumnList.size());
	        map.put("value", subList);
	        }
		  return map;
	}
	
	@Override
	public List<RankEnergy> getEnergyElecAndWaterRankExcle(CommonMonitorDataVo vo) {
		int redioType = vo.getRedioType();
		String lastTime = vo.getLastTime();
		String startTime= vo.getStartTime();
		String tableName = vo.getTableName();
		if(redioType==1 && vo.getSelectType()==2) {  //范围 月份 拼接
			lastTime = lastTime+"-01";
			startTime = startTime+"-01";
		}
		double elecSum = 0.0;
		double waterSum = 0.0;
		double elecAndWaterSum = 0.0;
		int parentId = 0;
		//根据楼层去查询楼层数据
		DecimalFormat df = new DecimalFormat("#.00");
		//List<List<Object>> cloumnList = new ArrayList<>();
		List<RankEnergy> rowList = new ArrayList<>();
		List<Energy> floorId = energyCountMapper.getEnergyA3Floorid();//获取A3楼所有层数
		if(!floorId.isEmpty() && floorId != null) {//电
			int xulie=1;
			double value=0.0;
			for(Energy id : floorId) {
				//List<RankEnergy> rowList = new ArrayList<>();
				RankEnergy rankEnergy = new RankEnergy();
				int spaceId = id.getFloorId();
				parentId = 34;
				List<EnergyCountOne> floorElecList =  mapper.queryEnergyBySpaceId(parentId,spaceId,redioType, startTime, lastTime, tableName);
				//和
				if(!floorElecList.isEmpty() && floorElecList != null) {
					for(int i = 0;i<floorElecList.size();i++) {
						elecSum = elecSum + floorElecList.get(i).getAvg();//每层楼的总电和
						int elec = floorElecList.get(i).getId();
						value = Double.parseDouble(df.format(floorElecList.get(i).getAvg()));
						switch(elec){
							case 44:rankEnergy.setKtElec(value);
							        break;
							case 45:rankEnergy.setZmElec(value);
									break;
							case 46:rankEnergy.setZhElec(value);
									break;
							case 47:rankEnergy.setDlElec(value);
									break;
							case 48:rankEnergy.setTsElec(value);
									break;
						}
						rankEnergy.setElecSum(Double.parseDouble(df.format(elecSum)));
					}
				}
				parentId = 37;
				List<EnergyCountOne> floorWaterList =  mapper.queryEnergyBySpaceId(parentId,spaceId,redioType, startTime, lastTime, tableName);
				//和
				if(!floorWaterList.isEmpty() && floorWaterList != null) {
					for(int i = 0;i<floorWaterList.size();i++) {
						waterSum = waterSum + floorWaterList.get(i).getAvg();//每层楼的总电和
						int water = floorWaterList.get(i).getId();
						value = Double.parseDouble(df.format(floorWaterList.get(i).getAvg()));
						switch(water){
						case 50:rankEnergy.setShWater(value);
						break;
						case 51:rankEnergy.setWsWater(value);
						break;
					}
						rankEnergy.setWaterSum(Double.parseDouble(df.format(waterSum)));
					}
				}
				elecAndWaterSum = elecSum + waterSum;
				rankEnergy.setElecAndWaterSum(Double.parseDouble(df.format(elecAndWaterSum)));
				//rankEnergy.setXulie(xulie);
				rankEnergy.setFloor(id.getFloor());
				//xulie++;
				rowList.add(rankEnergy);
			}
		}
		
		  return rowList;
	}

	@Override
	public List<RankEnergy> getEnergyAllRank(CommonMonitorDataVo vo, String rankType, String rank) {
		int redioType = vo.getRedioType();
		String lastTime = vo.getLastTime();
		String startTime= vo.getStartTime();
		String tableName = vo.getTableName();
		if(redioType==1 && vo.getSelectType()==2) {  //范围 月份 拼接
			lastTime = lastTime+"-01";
			startTime = startTime+"-01";
		}
		Double elecAndWaterSum = 0.0;
		int parentId = 0;
		//根据楼层去查询楼层数据
//		DecimalFormat df = new DecimalFormat("#####0.00");
		//List<List<Object>> cloumnList = new ArrayList<>();
		List<RankEnergy> rowList = new ArrayList<>();
		List<Energy> floorId = energyCountMapper.getEnergyA3Floorid();//获取A3楼所有层数
		if(!floorId.isEmpty() && floorId != null) {//电
			Object value=0.00;
			for(Energy id : floorId) {
				Double elecSum = 0.0;
				Double waterSum = 0.0;
				//List<RankEnergy> rowList = new ArrayList<>();
				RankEnergy rankEnergy = new RankEnergy();
				int spaceId = id.getFloorId();
				parentId = 34;
				List<EnergyCountOne> floorElecList =  mapper.queryEnergyBySpaceId(parentId,spaceId,redioType, startTime, lastTime, tableName);
				//和
				if(!floorElecList.isEmpty() && floorElecList != null) {
					for(int i = 0;i<floorElecList.size();i++) {
						elecSum = elecSum + floorElecList.get(i).getAvg();//每层楼的总电和
						int elec = floorElecList.get(i).getId();
						value = ScaleUtil.getRate(floorElecList.get(i).getAvg());
						switch(elec){
							case 44:rankEnergy.setKtElec(value);
							        break;
							case 45:rankEnergy.setZmElec(value);
									break;
							case 46:rankEnergy.setZhElec(value);
									break;
							case 47:rankEnergy.setDlElec(value);
									break;
							case 48:rankEnergy.setTsElec(value);
									break;
						}
						rankEnergy.setElecSum(ScaleUtil.getRate(elecSum));
					}
				}
				parentId = 37;
				List<EnergyCountOne> floorWaterList =  mapper.queryEnergyBySpaceId(parentId,spaceId,redioType, startTime, lastTime, tableName);
				//和
				if(!floorWaterList.isEmpty() && floorWaterList != null) {
					for(int i = 0;i<floorWaterList.size();i++) {
						waterSum = waterSum + floorWaterList.get(i).getAvg();//每层楼的总电和
						int water = floorWaterList.get(i).getId();
						value = ScaleUtil.getRate(floorWaterList.get(i).getAvg());
						switch(water){
						case 50:rankEnergy.setShWater(value);
						break;
						case 51:rankEnergy.setWsWater(value);
						break;
					}
						rankEnergy.setWaterSum(ScaleUtil.getRate(waterSum));
					}
				}
				elecAndWaterSum = elecSum + waterSum;
				rankEnergy.setElecAndWaterSum(ScaleUtil.getRate(elecAndWaterSum));
//				String a = df.format(elecAndWaterSum);
				rankEnergy.setFloor(id.getFloor());
				rowList.add(rankEnergy);
			}
		}
		List<RankEnergy> list = ListSortUtil.sort(rowList, rankType, rank);
		return list;
	}

	@Override
	public List<EnergyType> getTypeList() {
		List<EnergyType> typeList = new ArrayList<>();
		List<EnergyType> list =  mapper.getTypeList();
		int id;
		if(list != null && !list.isEmpty()) {
			for(EnergyType type : list) {
				id=type.getId();
				switch (id) {
				case 34:
					EnergyType energyType = new EnergyType();
					energyType.setId(type.getId());
					energyType.setName(type.getName());
					List<EnergyType> elecList = new ArrayList<>();
					    for(EnergyType elec : list){
					    	if(elec.getParent() == 34) {
					    		elecList.add(elec);
					    	}
					    }
					 energyType.setEnergyType(elecList); 
					 typeList.add(energyType);
					 break;
				case 37:
					EnergyType energyType2 = new EnergyType();
					energyType2.setId(type.getId());
					energyType2.setName(type.getName());
					List<EnergyType> waterList = new ArrayList<>();
					    for(EnergyType water : list){
					    	if(water.getParent() == 37) {
					    		waterList.add(water);
					    	}
					    }
				    energyType2.setEnergyType(waterList); 
					typeList.add(energyType2);
					break;
				default:
					break;
				}
			}
		}
		
		
		return typeList;
	}

	@Override
	public Map<String, Object> energyProportion() {
		List<Map<String, Object>> elec = mapper.elecProportion();
		List<Map<String, Object>> water = mapper.waterProportion();
		Map<String, Object> map = new HashMap();
		map.put("elec",elec);
		map.put("water",water);
		return map;
	}


}

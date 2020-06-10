package com.vibe.service.energy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vibe.mapper.energy.EnergyStatisticsMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Probe;
import com.vibe.monitor.asset.Space;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.energy.Curves;
import com.vibe.service.classification.Code;
import com.vibe.service.classification.SelectOptionService;

@Service
public class EnergyRealTimeServiceimpl implements EnergyRealTimeService {

	@Autowired
	private EnergyStatisticsMapper mapper;

	@Autowired
	private SelectOptionService selelctOption;

	@Autowired
	private AssetStore assetStore;

	@Autowired
	private EnergyMeterService energyMeterService;

	// 综合能耗
	@Override
	public Map<String, Object> getSynthesize(CommonMonitorDataVo vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> catalogIds = selelctOption.getEnergyCatalogIds(Code.ROOT_ID);
		List<Probe> probes = getSpaceTotalEnergyProbe(1, catalogIds);
		List<List<Object>> totalUnitData = getTotalUnitData(probes, vo);
		map.put("data", totalUnitData);
		Map<String, Object> dataMap = getCatalogData(probes, vo);
		map.put("curve", dataMap);

		return map;
	}

	private Map<String, Object> getCatalogData(List<Probe> probes, CommonMonitorDataVo vo) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Object> xName = new ArrayList<Object>();

		List<Object> values = new ArrayList<Object>();
		List<Double> corbonList = new ArrayList<>();
		int ringCount = 0;
		for (Probe probe : probes) {
			Integer itemize = energyMeterService.queryItemize(probe.getId());
			if (itemize != null) {
				xName.add(getNameByCatalog((int) itemize, Code.ENERGY));
			}
			List<Object> synthesizeCurve = getSynthesizeCurve(probe, map, vo, corbonList, ringCount);
			values.add(synthesizeCurve);
			ringCount++;
		}
		values.add(corbonList);
		xName.add("CO2排放量");
		map.put("values", values);
		map.put("xunit", vo.getUnit());
		map.put("xname", xName);

		return map;
	}

	private List<Object> getSynthesizeCurve(Probe probe, Map<String, Object> map, CommonMonitorDataVo vo,
			List<Double> corbonList, int ringCount) {
		List<Object> dataList = new ArrayList<>();
		List<CommonMonitorDataVo> monitorDatas = mapper.queryRealTime(probe.getId(), vo.getTableName(), vo.getSubNum(),
				vo.getNowTimeAndLastTime().get(0));
		List<Object> xAxis = new LinkedList<Object>();
		for (int i = 0; i < monitorDatas.size(); i++) {
			CommonMonitorDataVo data = monitorDatas.get(i);
			dataList.add(data.getValue() == null ? 0 : String.format("%.2f", data.getValue()));
			SimpleDateFormat dateFormatter = new SimpleDateFormat(vo.getDateFormat());
			xAxis.add(dateFormatter.format(data.getMoment()));
			getTotalCorbon(probe, corbonList, ringCount, monitorDatas, i, data);
		}
		map.put("xAxis", xAxis);
		return dataList;
	}

	private void getTotalCorbon(Probe probe, List<Double> corbonList, int ringCount,
                                List<CommonMonitorDataVo> monitorDatas, int i, CommonMonitorDataVo data) {
		EnergyRatio energyRatio = new EnergyRatio();
		Integer itemize = energyMeterService.queryItemize(probe.getId());
		double carbon = 0d;
		if (itemize != null) {
			carbon = energyRatio.translateCarbon(itemize, (double) data.getValue());
		}
		if (ringCount >= 1) {
			if (corbonList.size() >= monitorDatas.size()) {
				Double catalogValue = corbonList.get(i) + carbon;
				corbonList.set(i, catalogValue);
			} else {
				Double catalogValue = corbonList.get(0) + carbon;
				corbonList.set(i, catalogValue);
			}
		} else {
			corbonList.add(carbon);
		}
	}

	private List<List<Object>> getTotalUnitData(List<Probe> probes, CommonMonitorDataVo vo) {
		List<List<Object>> list = new ArrayList<List<Object>>();
		List<Object> originList = new ArrayList<>();
		List<Object> ceList = new ArrayList<>();
		List<Object> carbonList = new ArrayList<>();
		for (Probe probe : probes) {
			vo.getTableName();
			Double value = getEnergyCatalogTotal(probe, vo.getTableName(), vo.getNowTimeAndLastTime().get(0),
					vo.getSubNum());
			originList.add(value == null ? 0 : String.format("%.2f", value));
			EnergyRatio energyRatio = new EnergyRatio();
			Integer itemize = energyMeterService.queryItemize(probe.getId());
			if (value != null && itemize != null) {
				double standardCoal = energyRatio.translateStandardCoal(itemize, value);
				ceList.add(String.format("%.2f", standardCoal));
				double carbon = energyRatio.translateCarbon(itemize, value);
				carbonList.add(String.format("%.2f", carbon));
			}
		}
		list.add(originList);
		list.add(ceList);
		list.add(carbonList);
		return list;
	}

	/* 实时监控-总 */
	@Override
	public List<Curves> getRealTimeTotal(CommonMonitorDataVo vo) {
		List<Curves> curves = new ArrayList<Curves>();
		List<String> nowTimeAndLastTime = vo.getNowTimeAndLastTime();

		List<Probe> spaceTotalEnergyprobe = getSpaceTotalEnergyProbe(1,
				selelctOption.getEnergyCatalogIds(Code.ROOT_ID));
		for (Probe probe : spaceTotalEnergyprobe) {
			Curves curve = new Curves();
			curve.setCurve(getCurvesMap(probe, nowTimeAndLastTime, vo));
			curve.setTotal(getEnergyCatalogTotal(probe, vo.getTableName(), nowTimeAndLastTime.get(0), vo.getSubNum()));
			Integer itemize = energyMeterService.queryItemize(probe.getId());
			if (itemize != null) {
				curve.setType(itemize);
			}
			curves.add(curve);
		}
		return curves;
	}

	private Map<String, Object> getCurvesMap(Probe probe, List<String> nowTimeAndLastTime, CommonMonitorDataVo vo) {
		Map<String, Object> map = new HashMap<>();
		for (int i = nowTimeAndLastTime.size()-1; i >= 0 ; i--) {
			setCurveMap(map, i, probe, nowTimeAndLastTime, vo);
		}
		return map;
	}

	public void setCurveMap(Map<String, Object> map, int flagNum, Probe probe, List<String> nowTimeAndLastTime,
			CommonMonitorDataVo vo) {

		List<CommonMonitorDataVo> monitorDatas = mapper.queryRealTime(probe.getId(), vo.getTableName(), vo.getSubNum(),
				nowTimeAndLastTime.get(flagNum));
		String x = "xAxis";
		String y = "values" + (flagNum + 1);
		setXYData(probe, map, vo, monitorDatas, x, y);
	}

	private void setXYData(Probe probe, Map<String, Object> map, CommonMonitorDataVo vo,
                           List<CommonMonitorDataVo> monitorDatas, String x, String y) {
		List<Object> xAxis = new LinkedList<Object>();
		List<Object> values = new LinkedList<Object>();
		for (CommonMonitorDataVo data : monitorDatas) {
			values.add(data.getValue() == null ? "_" : data.getValue());
			SimpleDateFormat dateFormatter = new SimpleDateFormat(vo.getDateContrastFormat());
			xAxis.add(dateFormatter.format(data.getMoment()));
		}
		Integer itemize = energyMeterService.queryItemize(probe.getId());
		if (itemize != null) {
			map.put("yunit", getUnitByCatalog((int) itemize, Code.ENERGY));
		}
		map.put("xunit", vo.getUnit());
		map.put(x, xAxis);
		map.put(y, values);
	}

	// 查时表返回近期总的
	private Double getEnergyCatalogTotal(Probe probe, String tableName, String nowTime, int subNum) {
		return mapper.queryRealTimeTotal(probe.getId(), tableName, subNum, nowTime);
	}

	// 获取该空间下总分类型能耗的监测器
	@Override
	public List<Probe> getSpaceTotalEnergyProbe(int parentSpace, List<Integer> energyCatalogIdList) {
		List<Probe> energyProbelist = new ArrayList<Probe>();
		Asset<?> root = assetStore.findAsset(parentSpace);
		if (root.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) root;
			Collection<Asset<?>> childrens = parent.children();
			for (Asset<?> asset : childrens) {
				if (asset instanceof Probe) {
					Probe probe = (Probe) asset;
					Integer itemize = energyMeterService.queryItemize(probe.getId());
					if (energyCatalogIdList != null && itemize != null && energyCatalogIdList.contains((int) itemize)) {
						energyProbelist.add(probe);
					}
				}
			}
		}
		return energyProbelist;
	}

	// 获取空间下子能源的probe,参数 空间id与能源的类型。
	public Map<Integer, Object> getCaildEnergyProbe(int parentSpace, List<Integer> energyCatalogIdList) {
		Map<Integer, Object> spaceEnergymap = new HashMap<Integer, Object>();
		Asset<?> root = assetStore.findAsset(parentSpace);
		if (root.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) root;
			Collection<Asset<?>> childrens = parent.children();
			for (Asset<?> asset : childrens) {
				if (asset instanceof Space) {
					List<Probe> probes = getSpaceTotalEnergyProbe(asset.getId(), energyCatalogIdList);
					spaceEnergymap.put(asset.getId(), probes);
				}
			}
		}
		return spaceEnergymap;
	}

	@Override
	public List<Map<String, Object>> getRealTimeBuilding(Integer parentId, CommonMonitorDataVo vo) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		if (isChildIncludeSpace(parentId)) {
			Map<String, Object> rankMap = buildingRankingData(parentId, vo);
			mapList.add(rankMap);
		} else {
			Map<String, Object> totalMap = buildingTotalData(parentId, vo);
			mapList.add(totalMap);
			Map<String, Object> ItemMap = buildingItemData(parentId, vo);
			mapList.add(ItemMap);

		}
		return mapList;
	}

	private Map<String, Object> buildingItemData(Integer parentId, CommonMonitorDataVo vo) {
		Map<String, Object> map = new HashMap<>();
		List<Object> xAxis = new LinkedList<Object>();

		List<CommonSelectOption> workItems = selelctOption.querySelectOptionListOther(vo.getCatalog(), Code.ENERGY);
		List<Probe> probes = getSpaceTotalEnergyProbe(parentId, getCatalogIds(workItems));
		for (CommonSelectOption item : workItems) {
			xAxis.add(item.getName());
		}
		List<Object> values = new LinkedList<Object>();
		for (Probe probe : probes) {
			Double itemValue = mapper.queryRealTimeTotal(probe.getId(), vo.getTableName(), vo.getSubNum(),
					vo.getNowTimeAndLastTime().get(0));
			values.add(itemValue == null ? "_" : itemValue);
		}
		map.put("values", values);

		map.put("yunit", getUnitByCatalog((int) vo.getCatalog(), Code.ENERGY));
		map.put("xunit", "分项类别");
		map.put("xAxis", xAxis);

		return map;
	}

	@Override
	public List<Integer> getCatalogIds(List<CommonSelectOption> workItems) {
		List<Integer> idList = new ArrayList<Integer>();
		if (workItems != null && workItems.size() > 0) {
			for (CommonSelectOption item : workItems) {
				idList.add(item.getId());
			}
		}
		return idList;
	}

	private Map<String, Object> buildingTotalData(Integer parentId, CommonMonitorDataVo vo) {
		Map<String, Object> map = new HashMap<>();
		List<Probe> probes = getSpaceTotalEnergyProbe(parentId, Arrays.asList(vo.getCatalog()));
		List<CommonMonitorDataVo> monitorDatas = null;
		if (probes != null && probes.size() > 0)
			monitorDatas = mapper.queryRealTime(probes.get(0).getId(), vo.getTableName(), vo.getSubNum(),
					vo.getNowTimeAndLastTime().get(0));
		if (monitorDatas != null)
			setXYData(probes.get(0), map, vo, monitorDatas, "xAxis", "values");
		return map;
	}

	@Override
	public String getUnitByCatalog(Integer catalog, Integer code) {
		CommonSelectOption selectOption = selelctOption.getSelectOption(catalog, code);
		if (selectOption != null)
			return selectOption.getUnit();
		return "";
	}

	@Override
	public String getNameByCatalog(Integer catalog, Integer code) {
		CommonSelectOption selectOption = selelctOption.getSelectOption(catalog, code);
		if (selectOption != null)
			return selectOption.getName();
		return "";
	}

	private Map<String, Object> buildingRankingData(int parentId, CommonMonitorDataVo vo) {
		Map<String, Object> map = new HashMap<>();
		List<Object> xAxis = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		Map<Integer, Object> caildEnergyProbe = getCaildEnergyProbe(parentId, Arrays.asList(vo.getCatalog()));
		Set<Integer> keySet = caildEnergyProbe.keySet();
		for (Integer key : keySet) {
			List<Probe> energyProbe = (List<Probe>) caildEnergyProbe.get(key);
			if (energyProbe.size() > 0) {
				xAxis.add(assetStore.findAsset(key).getCaption());
				for (Probe probe : energyProbe) {
					Integer itemize = energyMeterService.queryItemize(probe.getId());
					if (itemize != null && Arrays.asList(vo.getCatalog()).contains(itemize)) {
					}
					Double energyCatalogTotal = getEnergyCatalogTotal(probe, vo.getTableName(),
							vo.getNowTimeAndLastTime().get(0), vo.getSubNum());
					if (energyCatalogTotal != null) {
						values.add(energyCatalogTotal);
					} else {
						xAxis.remove(assetStore.findAsset(key).getCaption());
					}
				}
			}
		}
		map.put("yunit", "空间");
		map.put("xunit", getUnitByCatalog(vo.getCatalog(), Code.ENERGY));
		map.put("xAxis", xAxis);
		map.put("values", values);
		return map;
	}

	public boolean isChildIncludeSpace(int parentId) {
		Asset<?> root = assetStore.findAsset(parentId);
		if (root.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) root;
			Collection<Asset<?>> childrens = parent.children();
			for (Asset<?> asset : childrens) {
				if (asset instanceof Space) {
					return true;
				}
			}
		}
		return false;
	}

}

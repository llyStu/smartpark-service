package com.vibe.service.statistics;

import com.vibe.pojo.NamedValue;
import com.vibe.utils.type.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vibe.common.Application;
import com.vibe.common.code.CodeCatalog;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.code.CodeItem;
import com.vibe.common.code.MonitorCodes;
import com.vibe.mapper.statistics.MonitorStatsMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetState;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.AssetTypeManager;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.asset.Probe;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.monitor.asset.type.ProbeType;

@Service
public class MonitorStatsServiceImpl implements MonitorStatsService {
    @Autowired
    AssetStore store;
    @Autowired
    CodeDictManager codeManager;
    @Autowired
    MonitorStatsMapper dao;
    @Autowired
    Application application;

    public Map<String, List<Object>> getStateData() {
        List<Object> legend = new LinkedList<Object>();
        CodeCatalog stateCodes = this.codeManager.getLocalDict().getCatalog((short) MonitorCodes.MONITOR_STATE);
        assert (stateCodes != null);
        for (CodeItem item : stateCodes.getItems()) {
            legend.add(item.getName());
        }

        Map<Short, Integer> stateMap = new TreeMap<Short, Integer>();
        //initialize all states count to zero.
        for (AssetState s : AssetState.values())
            stateMap.put((short) s.ordinal(), 0);
        short state;
        for (Asset<?> asset : this.store.getAssets()) {
            if (asset.isMonitor()) {
                if (!"AmountOfChangeFloatProbe".equals(asset.getType().getName()) && !"SumFloatProbe".equals(asset.getType().getName())) {
                    state = (short) ((Asset<?>) asset).getState().ordinal();
                    if (!stateMap.containsKey(Short.valueOf(state))) {
                        stateMap.put(Short.valueOf(state), Integer.valueOf(0));
                    } else {
                        int n = ((Integer) stateMap.get(Short.valueOf(state))).intValue();
                        stateMap.replace(Short.valueOf(state), Integer.valueOf(++n));
                    }
                }
            }
        }

        List<Object> values = new LinkedList<Object>();
        for (Map.Entry<Short, Integer> entry : stateMap.entrySet()) {
            short stateId = entry.getKey();
            String stateName = stateCodes.getItemName(stateId);
            NamedValue nv = new NamedValue(stateName, entry.getValue());
            values.add(nv);
        }

        Map<String, List<Object>> chartDataMap = new HashMap<String, List<Object>>();
        chartDataMap.put("legend", legend);
        chartDataMap.put("values", values);
        return chartDataMap;
    }

    public Map<String, List<Object>> getMonitorCount() {
        List<Map<String, Object>> dbResult = this.dao.statsProbeCatalogCount(MonitorCodes.MONITOR_KIND);

        List<Object> xAxis = new LinkedList<Object>();
        List<Object> values = new LinkedList<Object>();
        if (dbResult != null && dbResult.size() > 0) {
            for (Map<String, Object> fields : dbResult) {
                //暂时没有单位又不想出错，这么写
                if (fields.get("kind") == null) {
                    xAxis.add("Null");
                    long count = ((Long) fields.get("num")).longValue();
                    values.add(Integer.valueOf((int) count));
                    continue;
                }
                short kind = (short) ((Integer) fields.get("kind")).intValue();
                CodeCatalog monitorCatalog = this.codeManager.getLocalDict()
                        .getCatalog((short) MonitorCodes.MONITOR_KIND);
                if (monitorCatalog != null) {
                    final CodeItem code = monitorCatalog.getItem(kind);
                    xAxis.add(code == null ? "Unknown" : code.getName());
                } else {
                    xAxis.add("Unknown");
                }
                long count = ((Long) fields.get("num")).longValue();
                values.add(Integer.valueOf((int) count));
            }
        }
        Map<String, List<Object>> resultMap = new HashMap<String, List<Object>>();
        resultMap.put("xAxis", xAxis);
        resultMap.put("values", values);
        return resultMap;
    }

    public Map<String, List<Object>> getDeviceEnabledYears() {
        List<Map<String, Object>> dbResult = this.dao.statsDeviceEnabledYears(this.application.getSiteId());
        System.out.println("siteId==" + this.application.getSiteId());
        List<Object> xAxis = new LinkedList<Object>();
        List<Object> values = new LinkedList<Object>();
        for (Map<String, Object> fields : dbResult) {
            int year = ((Integer) fields.get("eyear")).intValue();
            xAxis.add(Integer.valueOf(year));
            long num = ((Long) fields.get("num")).longValue();
            values.add(Integer.valueOf((int) num));
        }
        Map<String, List<Object>> resultMap = new HashMap<String, List<Object>>();
        resultMap.put("xAxis", xAxis);
        resultMap.put("values", values);
        return resultMap;
    }

    @Override
    public Map<String, List<Object>> queryAllProbe() {

        List<Object> monitorIds = new ArrayList<Object>();
        List<Object> captions = new ArrayList<Object>();
        //List<Map<String, Object>> maps = dao.queryAllProbe();
        List<Map<String, Object>> maps = dao.queryAllProbe(this.application.getSiteId());
        for (Map<String, Object> map : maps) {
            monitorIds.add((Integer) map.get("monitorId"));
            captions.add((String) map.get("caption"));
        }
        AssetTypeManager<AssetType> assetTypes = store.getAssetTypes(AssetKind.PROBE);
        for (AssetType assetType : assetTypes) {
            ProbeType type = (ProbeType) assetType;
            Collection<Asset<ProbeType>> assets = store.getAssets(type);
            for (Asset<ProbeType> asset : assets) {
                Probe probe = (Probe) (Monitor) asset;
                if (probe.getType().getDataType().getName().equals("FLOAT")) {
                    monitorIds.add(probe.getId());
                    captions.add(probe.getCaption());
                }
            }
        }
        Map<String, List<Object>> result = new HashMap<String, List<Object>>();
        result.put("monitorIds", monitorIds);
        result.put("captions", captions);
        return result;
    }

    //不同状态的设备详情、不同类型监测器的详情
    public Map<String, List<Object>> getInfoByMonitorState(String filter, int index, String name) {

        int code_catalog = (short) MonitorCodes.MONITOR_KIND;

        List<Object> monitorIds = new ArrayList<Object>();
        List<Object> captions = new ArrayList<Object>();
        List<Asset<?>> asset_states = new ArrayList<Asset<?>>();
        for (Asset<?> asset : this.store.getAssets()) {
            if (asset.isMonitor()) {
                //if((asset.getId()>>16) == this.application.getSiteId()){
                asset_states.add(asset);
                //}
            }
        }
        if ("state".equals(filter)) {

            for (Asset<?> item : asset_states) {
                if (item.getState().ordinal() == (index)) {
                    monitorIds.add(item.getId());
                    captions.add(item.getCaption());
                }
            }
        } else {
            List<Map<String, Object>> maps = dao.queryByType(name, code_catalog);
            //List<Map<String, Object>> maps = dao.queryByType(name);
            for (Map<String, Object> map : maps) {
                monitorIds.add((Integer) map.get("monitorId"));
                captions.add((String) map.get("caption"));
            }
        }
        Map<String, List<Object>> result = new HashMap<String, List<Object>>();
        result.put("monitorIds", monitorIds);
        result.put("captions", captions);
        return result;
    }
}

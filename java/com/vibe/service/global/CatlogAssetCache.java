package com.vibe.service.global;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vibe.common.code.CascadeCodeItem;
import com.vibe.common.code.CodeDict;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.code.CodeItem;
import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.pojo.XiaofangCount;

public class CatlogAssetCache {

    private CodeDictManager codeDictManager;

    private AssetStore assetStore;

    private SelectOptionDao selectOptionDao;

    private static CatlogAssetCache catlogAssetCache;

    private Map<Integer, List<Asset<?>>> deviceCatlogAssetMap;
    private Map<Integer, List<Asset<?>>> monitorCatlogAssetMap;
    private Map<String, Integer> codeNameIdMap;
    private Map<Short, XiaofangCount> xiaofangCountMap;

    private CatlogAssetCache(CodeDictManager codeDictManager, AssetStore assetStore, SelectOptionDao selectOptionDao) {
        this.codeDictManager = codeDictManager;
        this.assetStore = assetStore;
        this.selectOptionDao = selectOptionDao;
        load();
    }

    public Map<Integer, List<Asset<?>>> getDeviceCatlogAssetMap() {
        return deviceCatlogAssetMap;
    }

    public Map<String, Integer> getCodeNameId() {
        return codeNameIdMap;
    }

    public Map<Integer, List<Asset<?>>> getMonitorCatlogAssetMap() {
        return monitorCatlogAssetMap;
    }

    public Map<Short, XiaofangCount> getXiaofangCountMap() {
        return xiaofangCountMap;
    }

    private void load() {
        xiaofangCountMap = new HashMap<>();
        deviceCatlogAssetMap = new HashMap<>();
        monitorCatlogAssetMap = new HashMap<>();
        codeNameIdMap = new HashMap<>();
        Integer xiaofangCode = selectOptionDao.getSystemItem("消防");
        if (xiaofangCode != null) {
            CascadeCodeItem cascadeCodeItem = (CascadeCodeItem) getCodeItem((short) 2001, (short) (int) xiaofangCode);
            Collection<CascadeCodeItem> cascadeCodeItems = cascadeCodeItem.getChildren();
            if (cascadeCodeItems != null) {
                for (CascadeCodeItem cascadeCode : cascadeCodeItem.getChildren()) {
                    XiaofangCount xiaofangCount = new XiaofangCount();
                    xiaofangCount.setCaption(cascadeCode.getName());
                    xiaofangCount.setCount(0);
                    xiaofangCountMap.put(cascadeCode.getId(), xiaofangCount);
                }
            }
        }
        Collection<Asset<?>> assets = null;
        List<Integer> deviceCodes = selectOptionDao.getIdByCatlog(2002);
        List<Integer> monitorCodes = selectOptionDao.getIdByCatlog(2001);
        for (Integer integer : deviceCodes) {
            deviceCatlogAssetMap.put(integer, new ArrayList<>());
        }
        for (Integer integer : monitorCodes) {
            monitorCatlogAssetMap.put(integer, new ArrayList<>());
        }
        for (Integer integer : deviceCodes) {
            CodeItem item = getCodeItem((short) 2002, (short) (int) integer);
            codeNameIdMap.put(item.getName(), integer);
        }

        assets = assetStore.getSubAssets(Device.class);
        for (Asset<?> asset : assets) {
            Device device = (Device) asset;
            CodeItem codeItem = getCodeItem((short) 2002, device.getCatalog());
            if (codeItem != null
                    && deviceCatlogAssetMap.get((int) codeItem.getId()) != null) {
                CascadeCodeItem cascadeCodeItem = (CascadeCodeItem) codeItem;
                deviceCatlogAssetMap.get((int) cascadeCodeItem.getId()).add(asset);
                deviceCatlogAssetMap.get((int) cascadeCodeItem.getParent().getId()).add(asset);
            }
        }

        assets = assetStore.getSubAssets(Monitor.class);
        for (Asset<?> asset : assets) {
            Monitor<?> monitor = (Monitor<?>) asset;
            CodeItem codeItem = getCodeItem((short) 2001, monitor.getMonitorKind());
            if (codeItem != null
                    && monitorCatlogAssetMap.get(codeItem.getId()) != null) {
                CascadeCodeItem cascadeCodeItem = (CascadeCodeItem) codeItem;
                monitorCatlogAssetMap.get(cascadeCodeItem.getId()).add(asset);
                monitorCatlogAssetMap.get(cascadeCodeItem.getParent().getId()).add(asset);
            }
            XiaofangCount temp = xiaofangCountMap.get(monitor.getMonitorKind());
            if (temp != null) {
                temp.setCount(temp.getCount() + 1);
            }
        }

    }

    private CodeItem getCodeItem(short i, short integer) {
        return codeDictManager.getLocalDict().getItem(i, (short) (int) integer);
    }

    public static CatlogAssetCache getCatlogAssetCache(CodeDictManager codeDictManager, AssetStore assetStore,
                                                       SelectOptionDao selectOptionDao) {
        if (catlogAssetCache == null) {
            catlogAssetCache = new CatlogAssetCache(codeDictManager, assetStore, selectOptionDao);
        }
        return catlogAssetCache;
    }

}

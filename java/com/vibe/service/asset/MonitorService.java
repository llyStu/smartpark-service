package com.vibe.service.asset;

import java.util.List;
import java.util.Map;

import com.vibe.pojo.ProbeValue;
import com.vibe.pojo.historydata.HistoryDataCondition;
import com.vibe.util.constant.ResponseModel;
import com.vibe.utils.TreeNode;

public interface MonitorService {

    Map<String, Object> getAssetByKind(String kind, Integer id);

    List<TreeNode> initMonitorTree(Integer catalog, Integer spaceId, Integer type);

    List<TreeNode> initMonitorAllTree(List<Integer> catalogs, Integer spaceId);

    List<TreeNode> buildAssetList(Integer workId, Integer spaceId, Integer type, Integer depth, String searchStr);

    List<TreeNode> buildAssetListPage(List<Integer> catalogList, Integer spaceId, Integer type, Integer depth, String searchStr);

    Map<String, Object> initPage(Integer catalog, String catalogIds, Integer spaceId, Integer type, Integer depth, Integer page, Integer rows, String caption, String searchStr);

    List<ProbeValue> getProbeValue(String ids);

    Map<String, Object> probeHistoryValue(HistoryDataCondition hdc, Integer page, Integer rows);

    List<Object> getEnvironmentAvgByCode(Integer code);

    List<Map<String, Object>> getMonitorCodeName();
}

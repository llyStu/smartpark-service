package com.vibe.service.asset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.data.UseCode;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Control;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.asset.Space;
import com.vibe.monitor.asset.type.MonitorType;
import com.vibe.monitor.drivers.video.WebCamera;
import com.vibe.monitor.util.UnitInterface;
import com.vibe.monitor.util.UnitUtil;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.MonitorData;
import com.vibe.pojo.ProbeValue;
import com.vibe.pojo.historydata.HistoryDataCondition;
import com.vibe.service.classification.Code;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.service.statistics.MonitorDataService;
import com.vibe.util.RegixCut;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.TreeNode;
import com.vibe.utils.time.TimeUtil;
import com.vibe.utils.type.DataType;

@Service
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    private MonitorDataService monitorDataService;
    @Autowired
    private AssetService assetService;
    @Autowired
    CodeDictManager codeDictManager;
    @Autowired
    private AssetStore assetStore;
    @Autowired
    private SelectOptionService selectOptionService;

    @Autowired
    AssetBinding assetBinding;

    @Override
    public Map<String, Object> getAssetByKind(String kind, Integer monitorId) {
        if (monitorId == null) {
            System.out.println("monitor is " + monitorId + " not find");
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 根据类型判断是否需要监测曲线
        if (!AssetKind.SERVICE.toString().equals(kind)) {
            Map<String, Object> recentData = monitorDataService.getRecentData(monitorId, 12);
            result.put("graph", recentData);
        }
        Asset<?> root = assetStore.findAsset(monitorId);
        AssetVo asset = assetService.assetDteail(root, kind);
        result.put("data", asset);

        return result;
    }

    @Override
    public List<TreeNode> initMonitorTree(Integer workId, Integer spaceId, Integer type) {
        List<Integer> catalogs = selectOptionService.getCatalogs(workId);
        return initMonitorAllTree(catalogs, spaceId);

    }

    @Override
    public List<TreeNode> initMonitorAllTree(List<Integer> catalogs, Integer spaceId) {
        Space root = (Space) assetStore.findAsset(spaceId);
        Collection<Asset<?>> childrens = root.children();

        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        for (Asset<?> child : childrens) {
            TreeNode newnode = null;
            if (child != null) {
                newnode = printMonitorAllTree(child, catalogs, spaceId);
            }
            if (newnode != null) {
                nodeList.add(newnode);
            }
        }
        return nodeList;
    }

    public TreeNode printMonitorAllTree(final Asset<?> root, List<Integer> catalogs, Integer spaceId) {
        TreeNode treeNode = new TreeNode();
        treeNode = isMonotorAndDevice(root, catalogs, treeNode);
        if (root.isCompound()) {
            CompoundAsset<?> parent = (CompoundAsset<?>) root;
            List<TreeNode> list = new ArrayList<TreeNode>();
            Collection<Asset<?>> children = parent.children();
            if (children != null && children.size() > 0) {
                for (Asset<?> child : parent.children()) {
                    if (child != null) {
                        TreeNode node2 = printMonitorAllTree(child, catalogs, spaceId);
                        if (node2 != null) {
                            list.add(node2);
                        }
                    }
                }
                if (list != null && list.size() > 0 && treeNode != null) {
                    treeNode.setNodes(list);
                }
            }
        } else {

            treeNode = isMonotorAndDevice(root, catalogs, treeNode);
        }
        return treeNode;
    }

    private Integer addTreeNode(final Asset<?> root, List<Integer> catalogs, List<TreeNode> assetList,
                                Integer desiredAssetKind, Integer grade, String searchStr) {

        TreeNode treeNode = new TreeNode();
        treeNode.setBind(assetBinding.bind(root));
        if (root.getKind() == AssetKind.DEVICE) {
            Device device = (Device) root;
            int catalog = device.getCatalog();
            if (catalogs.size() == 0 || catalogs.contains(catalog)) {
                treeNode.setCatalog(catalog);
                treeNode.setCatalogName(
                        codeDictManager.getLocalDict().getItemName((short) Code.DEVICE, (short) catalog));
                setNodesBasicProbe(root, treeNode);
                if (desiredAssetKind == null || desiredAssetKind == 2002) {
                    if (searchStr == null || treeNode.getText().contains(searchStr)) {
                        treeNode.setHealthIndex(device.getHealthIndex());
                        assetList.add(treeNode);
                    }
                }
            }
        } else if (root.getKind() == AssetKind.SERVICE) {
            com.vibe.monitor.asset.MonitorService monitorService = (com.vibe.monitor.asset.MonitorService) root;
            int catalog = monitorService.getCatalog();
            if (catalogs.size() == 0 || catalogs.contains(catalog)) {
                treeNode.setCatalog(catalog);
                setNodesBasicProbe(root, treeNode);
                if (desiredAssetKind == null || desiredAssetKind == 3) {
                    if (searchStr == null || treeNode.getText().contains(searchStr)) {
                        assetList.add(treeNode);
                    }
                }
            }
        } else if (root.isMonitor()) {
            Monitor<?> mo = (Monitor<?>) root;
            if (!"AmountOfChangeFloatProbe".equals(mo.getType().getName()) && !"SumFloatProbe".equals(mo.getType().getName())) {
                int catalog = mo.getMonitorKind();
                if (catalogs.size() == 0 || catalogs.contains(catalog)) {
                    treeNode.setCatalog(catalog);
                    treeNode.setCatalogName(
                            codeDictManager.getLocalDict().getItemName((short) Code.PROBE, (short) catalog));
                    String unit = mo.getUnit();
                    treeNode.setUnit(unit);
                    Object value = mo.getValue();
                    treeNode.setValue(value);
                    if (mo.getLastDetectTimeMs() != 0) {
                        treeNode.setTime(TimeUtil.date2String(new Date(mo.getLastDetectTimeMs())));
                    }
                    treeNode.setErrorMsg(mo.getRuntimeDesc());
                    if (value != null) {
                        treeNode.setValue(value.toString());
                        if (value instanceof Float) {
                            if (root instanceof Control) {
                                treeNode.setMonitorType("floatcontrol");
                            }
                            // treeNode.setMonitorType("floatcontrol");
                            if (unit == null || !unit.contains("|")) {
                                String number = new UseCode(codeDictManager).getValue(value, (short) Code.PROBE,
                                        mo.getMonitorKind());
                                treeNode.setValue(number);
                                treeNode.setValueStr(number + (unit == null ? "" : unit));
                            } else {
                                UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {

                                    @Override
                                    public void parseResult(String result) {
                                        // TODO Auto-generated method stub
                                        treeNode.setValueStr(result);
                                    }

                                });
                            }
                        } else {
                            treeNode.setValueStr(value.toString());
                            if (root instanceof Control) {
                                treeNode.setMonitorType("boolcontrol");
                            }

                            UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {

                                @Override
                                public void parseResult(String result) {
                                    // TODO Auto-generated method stub
                                    treeNode.setValueStr(result);
                                }

                            });
                        }
                    }
                    setNodesBasicProbe(root, treeNode);
                    if (desiredAssetKind == null || desiredAssetKind == 2001) {
                        if (searchStr == null || treeNode.getText().contains(searchStr)) {
                            assetList.add(treeNode);
                        }
                    }
                }
            }
        }

        if (root.getKind() == AssetKind.DEVICE || root.isMonitor()) {
            grade++;
            treeNode.setGrade(grade);
        }
        return grade;
    }

    // monitor_init的方法
    @Override
    public List<TreeNode> buildAssetList(Integer assetCatalog, Integer rootId, Integer assetKind, Integer depthLimit,
                                         String searchStr) {

        int nodeGrade = 0;
        Asset<?> asset = assetStore.findAsset(rootId);
        List<Integer> catalogList = selectOptionService.getCatalogs(assetCatalog);// 以前的分类方式
        catalogList.add(assetCatalog);
        List<TreeNode> assetList = new ArrayList<TreeNode>();
        if (asset.isCompound()) {
            CompoundAsset<?> parent = (CompoundAsset<?>) asset;
            int currentDepth = 0;
            for (Asset<?> child : parent.children()) {
                buildAssetList(child, catalogList, assetList, assetKind, nodeGrade, currentDepth, depthLimit,
                        searchStr);
            }
        }
        return assetList;
    }

    public Integer buildAssetList(final Asset<?> root, List<Integer> catalogs, List<TreeNode> assetList, Integer type,
                                  Integer nodeGrade, Integer currentDepth, Integer depthLimit, String searchStr) {
        currentDepth++;
        Integer maxDepth = currentDepth;
        Integer newNodeGrade = addTreeNode(root, catalogs, assetList, type, nodeGrade, searchStr);
        if (root.isCompound()) {
            CompoundAsset<?> parent = (CompoundAsset<?>) root;
            for (Asset<?> child : parent.children()) {
                if (depthLimit == null) {
                    buildAssetList(child, catalogs, assetList, type, newNodeGrade, currentDepth, depthLimit, searchStr);
                } else if (maxDepth < depthLimit) {
                    Integer childrenDepth = buildAssetList(child, catalogs, assetList, type, newNodeGrade, currentDepth,
                            depthLimit, searchStr);
                    if (maxDepth < childrenDepth)
                        maxDepth = childrenDepth;
                }
            }
        }
        return maxDepth;
    }

    private TreeNode isMonotorAndDevice(final Asset<?> root, List<Integer> catalogs, TreeNode treeNode) {

        if (root instanceof Device) {
            Device device = (Device) root;

            int catalog = device.getCatalog();
            if (catalogs.contains(catalog)) {
                treeNode.setCatalog(catalog);
                setNodesBasicProbe(root, treeNode);
            } else {
                treeNode = null;
            }
        } else if (root instanceof Monitor) {
            Monitor<?> mo = (Monitor<?>) root;
            treeNode.setTime(TimeUtil.date2String(new Date(mo.getLastDetectTimeMs())));
            int catalog = mo.getMonitorKind();
            if (catalogs.contains(catalog)) {
                treeNode.setCatalog(catalog);

                String unit = mo.getUnit();
                treeNode.setUnit(unit);
                Object value = mo.getValue();
                treeNode.setValue(value);
                treeNode.setValueStr(value == null ? null : value.toString());
                if (value != null) {
                    if (value instanceof Float && ((unit == null) ? true : (!unit.contains("|")))) {
                        // System.out.println("oldvalue=="+value);
                        String number = new UseCode(codeDictManager).getValue(value, (short) Code.PROBE,
                                mo.getMonitorKind());
                        // System.out.println("newValue=="+number);
                        treeNode.setValueStr(number + (unit == null ? "" : unit));
                    } else {
                        /*
                         * if (!TextUtils.isEmpty(unit)) { if
                         * (unit.contains("|")) { String[] unitArr =
                         * unit.split("|"); if ("true".equals(value.toString()))
                         * { treeNode.setValueStr(unitArr[unitArr.length - 1]);
                         * } else if ("false".equals(value.toString())) {
                         * treeNode.setValueStr(unitArr[0]); } } }
                         */

                        TreeNode tn = treeNode;
                        UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {

                            @Override
                            public void parseResult(String result) {
                                // TODO Auto-generated method stub
                                tn.setValueStr(result);
                            }

                        });
                    }
                }
                setNodesBasicProbe(root, treeNode);
            } else {
                treeNode = null;
            }
        } else {
            setNodesBasicProbe(root, treeNode);
        }
        return treeNode;
    }

    private static final String PROBE_STR = "监测器";
    private static final String CONTROL_STR = "控制器";
    private static final String DEVICE_STR = "设备";

    private void setNodesBasicProbe(final Asset<?> root, TreeNode treeNode) {
        if (root instanceof WebCamera) {
            WebCamera webCamera = (WebCamera) root;
            treeNode.setUsername(webCamera.getUserName());
            treeNode.setPassword(webCamera.getPassword());
            treeNode.setHost(webCamera.getHost());
            treeNode.setPort(webCamera.getPort());
            treeNode.setRtspUrlPattern(webCamera.getRtspUrlPattern());
        }
        treeNode.setId(root.getId());
        treeNode.setKind(root.getKind().ordinal());
        treeNode.setStatus(root.getState().ordinal());
        treeNode.setText(root.getCaption());
        treeNode.setKindStr(getKindStr(root));
        treeNode.setStatusStr(getStatusStr(root));
        treeNode.setName(root.getName());

        treeNode.setModelName(root.getModelName());
        treeNode.setModelAngle(root.getModelAngle());
        treeNode.setModelCoordinate(root.getModelCoordinate());
        treeNode.setModelSize(root.getModelSize());
        String assetFullName = assetStore.getAssetFullName(root, ">", "name");
        treeNode.setFullName(assetFullName);
    }

    private String getKindStr(Asset<?> asset) {
        String kindStr = null;
        switch (asset.getKind()) {
            case SPACE:
                kindStr = "空间";
                break;
            case PROBE:
                kindStr = PROBE_STR;
                break;
            case CONTROL:
                kindStr = CONTROL_STR;
                break;
            case DEVICE:
                kindStr = DEVICE_STR;
                break;
            case SERVICE:
                kindStr = "服务";
                break;
            default:
                break;
        }
        return kindStr;
    }

    private String getStatusStr(Asset<?> asset) {
        String statusStr = null;
        switch (asset.getState()) {
            case WARNING:
                statusStr = "警告";
                break;
            case ERROR:
                statusStr = "错误";
                break;
            case NORMAL:
                statusStr = "正常";
                break;
            case UNKNOWN:
                statusStr = "未知";
                break;
            default:
                break;
        }
        return statusStr;
    }

    @Override
    public Map<String, Object> initPage(Integer catalog, String catalogIds, Integer spaceId, Integer kind,
                                        Integer depth, Integer page, Integer rows, String caption, String searchStr) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Set<Integer> gradOne = new HashSet<Integer>();
        Set<Integer> gradOther = new HashSet<Integer>();
        List<Integer> catalogList = getCatalogList(catalog, catalogIds, kind);
        List<TreeNode> buildAssetList = buildAssetListPage(catalogList, spaceId, kind, depth, searchStr);
        List<TreeNode> assetList = likeFindCaption(buildAssetList, caption);
        for (TreeNode treeNode : assetList) {
            if (treeNode.getGrade() == 1) {
                gradOne.add(treeNode.getCatalog());
            } else {
                gradOther.add(treeNode.getCatalog());
            }
        }
        gradOther.addAll(gradOne);
        Set<CommonSelectOption> gradOneOption = setCommonSelectOptionSet(gradOne, kind);
        Set<CommonSelectOption> gradTotalOption = setCommonSelectOptionSet(gradOther, kind);
        EasyUIJson listPage = assetListPage(rows, page, assetList);
        map.put("one", gradOneOption);
        map.put("total", gradTotalOption);
        map.put("list", listPage);
        return map;
    }

    private List<Integer> getCatalogList(Integer catalog, String catalogIds, Integer kind) {
        List<Integer> catalogList = new ArrayList<Integer>();
        if (catalog != 0 && catalog != -1) {
            catalogList = selectOptionService.getSelectIdList(catalog, kind);
            catalogList.add(catalog);
        }
        if (catalogIds != null) {
            catalogList = RegixCut.regixCutStringToList(catalogIds, ",");
        }
        return catalogList;
    }

    private List<TreeNode> likeFindCaption(List<TreeNode> buildAssetList, String caption) {

        List<TreeNode> list = new ArrayList<TreeNode>();
        if (caption != null) {
            for (TreeNode treeNode : buildAssetList) {
                String text = treeNode.getText();
                if (text.contains(caption)) {
                    list.add(treeNode);
                }
            }
            return list;
        }
        return buildAssetList;
    }

    private Set<CommonSelectOption> setCommonSelectOptionSet(Set<Integer> catalogIds, Integer catalog) {
        Set<CommonSelectOption> optionSet = new HashSet<CommonSelectOption>();
        if (!catalogIds.isEmpty()) {
            for (Integer catalogId : catalogIds) {
                CommonSelectOption selectOption = selectOptionService.getSelectOption(catalogId, catalog);
                optionSet.add(selectOption);
            }
        }
        return optionSet;
    }

    @Override
    public List<TreeNode> buildAssetListPage(List<Integer> catalogList, Integer rootId, Integer assetKind,
                                             Integer depthLimit, String searchStr) {
        int nodeGrade = 0;
        Asset<?> asset = assetStore.findAsset(rootId);
        List<TreeNode> assetList = new ArrayList<TreeNode>();
        if (asset.isCompound()) {
            CompoundAsset<?> parent = (CompoundAsset<?>) asset;
            int currentDepth = 0;
            for (Asset<?> child : parent.children()) {
                buildAssetList(child, catalogList, assetList, assetKind, nodeGrade, currentDepth, depthLimit,
                        searchStr);
            }
        }
        return assetList;
    }

    public EasyUIJson assetListPage(Integer rows, Integer page, List<TreeNode> buildAssetList) {
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal((long) buildAssetList.size());
        uiJson.setRows(buildAssetList.subList((page - 1) * rows,
                page * rows > buildAssetList.size() ? buildAssetList.size() : page * rows));
        return uiJson;
    }

    /*
     * 根据设备ids逐一查询设备下多个监测器的值
     */
    @Override
    public List<ProbeValue> getProbeValue(String ids) {
        List<List<TreeNode>> monitorList = new ArrayList<>();
        if (ids != null) {
            String[] deviceIds = ids.split(",");
            for (String deviceId : deviceIds) {
                List<TreeNode> monitors = getMonitors(Integer.parseInt(deviceId));
                monitorList.add(monitors);
            }
        }
        Map<String, String> map = getCaptionSet(monitorList);
        List<ProbeValue> list = new ArrayList<>();
        for (String caption : map.keySet()) {
            ProbeValue probeValue = new ProbeValue();
            List<Object> values = gatValues(monitorList, caption, probeValue);
            probeValue.setValues(values);
            probeValue.setName(caption);
            probeValue.setUnit(map.get(caption));
            list.add(probeValue);
        }
        return list;
    }

    public List<TreeNode> getMonitors(Integer deviceId) {
        List<TreeNode> assetList = new ArrayList<>();
        Asset<?> asset = assetStore.findAsset(deviceId);
        buildAssetList(asset, new ArrayList<>(), assetList, 2001, 0, 0, null, null);
        return assetList;
    }

    ;

    private Map<String, String> getCaptionSet(List<List<TreeNode>> monitors) {
        Map<String, String> catalogMap = new HashMap<String, String>();
        if (monitors.size() > 0) {
            for (List<TreeNode> treeNodeList : monitors) {
                for (TreeNode treeNode : treeNodeList) {
                    boolean isprobe = AssetKind.PROBE.ordinal() == treeNode.getKind();
                    if (isprobe) {
                        catalogMap.put(treeNode.getCatalogName(), treeNode.getUnit());
                    }
                }
            }
        }
        return catalogMap;
    }

    private List<Object> gatValues(List<List<TreeNode>> monitors, String caption, ProbeValue probeValue) {
        List<Object> values = new ArrayList<Object>();
        for (List<TreeNode> treeNodeList : monitors) {
            boolean idExit = false;
            for (TreeNode monitorNode : treeNodeList) {
                boolean isprobe = AssetKind.PROBE.ordinal() == monitorNode.getKind();
                if (isprobe) {
                    if (caption.equals(monitorNode.getCatalogName())) {
                        values.add(monitorNode);
                        idExit = true;
                    }
                }
            }
            if (!idExit) {
                TreeNode blankNode = new TreeNode();
                blankNode.setValue("");
                values.add(blankNode);
            }
        }
        return values;
    }

    @Override
    public Map<String, Object> probeHistoryValue(HistoryDataCondition hdc, Integer page, Integer rows) {
        List<List<MonitorData>> contents = new ArrayList<>();//每列数据集合
        ArrayList<Object> headers = new ArrayList<Object>();//创建表头集合
        Device device = (Device) assetStore.findAsset(hdc.getDeviceId());
        headers.add(device.getCaption());//设备名称

        int maxCount = 0;
        List<TreeNode> monitors = getMonitors(hdc.getDeviceId());
        List<String> units = new ArrayList<String>();//单位集合
        for (int i = 0; i < monitors.size(); i++) {
            TreeNode treeNode = monitors.get(i);
            headers.add(treeNode.getCatalogName());
            units.add(treeNode.getUnit());
            switch (getProbeTypeIndex(treeNode)) {
                case 0:// int t_sample_int
                    List<MonitorData> intValues = monitorDataService.queryIntMonitorValues(treeNode.getId(), hdc.getStartTime(), hdc.getLastTime());
                    if (intValues.size() > maxCount) maxCount = intValues.size();
                    contents.add(intValues);
                    break;
                case 1:// float t_sample_float
                    List<MonitorData> floatValues = monitorDataService.queryFloatMonitorValues(treeNode.getId(), hdc.getStartTime(), hdc.getLastTime());
                    if (floatValues.size() > maxCount) maxCount = floatValues.size();
                    contents.add(floatValues);
                    break;
                case 2:// boolean t_sample_boolean
                    List<MonitorData> boolValues = monitorDataService.queryBoolMonitorValues(treeNode.getId(), hdc.getStartTime(), hdc.getLastTime());
                    if (boolValues.size() > maxCount) maxCount = boolValues.size();
                    contents.add(boolValues);
                    break;
                case 3:// string
                    break;
                default:// not have
            }
        }
        final int lineSize = headers.size();
        Map<String, LinkedList<Object>> dataRows = getDataRows(lineSize, contents, maxCount, units);//将列数据转换行数据集合
        LinkedList<Object> tempRow = listFillNull(lineSize, new LinkedList<Object>());//缺省值

        List<ArrayList<Object>> formDatas = new ArrayList<ArrayList<Object>>();//表单数据
        for (Entry<String, LinkedList<Object>> dataRow : dataRows.entrySet()) {
            //System.out.println("Key = " + data.getKey() + ", Value = " + data.getValue());
            for (int i = 0; i < dataRow.getValue().size(); i++) {
                if (null != dataRow.getValue().get(i)) {
                    tempRow.set(i, dataRow.getValue().get(i));
                }
            }
            formDatas.add(new ArrayList<>(tempRow));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", headers);
        if (null != page && null != rows) {
            map.put("value", formDatas.subList((page - 1) * rows,
                    page * rows > formDatas.size() ? formDatas.size() : page * rows));
            map.put("total", (long) formDatas.size());
        } else {
            map.put("value", formDatas);
            map.put("fileName", assetStore.getAssetFullName(device, AssetStore.LOCATION_SEPARATOR));
        }
        return map;
    }

    @Override
    public List<Object> getEnvironmentAvgByCode(Integer code) {
        return (List<Object>) monitorDataService.getEnvironmentAvgByCodeDay(code, new Date());
    }

    @Override
    public List<Map<String, Object>> getMonitorCodeName() {
        return monitorDataService.getMonitorCodeName();
    }

    private Map<String, LinkedList<Object>> getDataRows(int length, List<List<MonitorData>> contents, int maxCount, List<String> units) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, LinkedList<Object>> datas = new TreeMap<>();
        String firstRowTime = null;
        for (int i = 0; i < maxCount; i++) {
            for (int j = 0; j < contents.size(); j++) {
                List<MonitorData> monitorDatas = contents.get(j);
                if (!monitorDatas.isEmpty() && monitorDatas.size() > i) {
                    MonitorData monitorData = monitorDatas.get(i);
                    String time = format.format(monitorData.getMoment());
                    LinkedList<Object> everyOneData = datas.get(time);
                    if (null == everyOneData) {
                        everyOneData = listFillNull(length, new LinkedList<Object>());
                    }
                    if (null != monitorData.getValue() && contents.size() == units.size()) {
                        monitorData = onValueAddUnit(units.get(j), monitorData);
                    }
                    if (0 == i) {
                        if (null == firstRowTime) {
                            firstRowTime = time;
                            everyOneData.set(0, time);
                        } else {
                            time = firstRowTime;
                            everyOneData = datas.get(time);
                        }
                        everyOneData.set(j + 1, monitorData.getValue());
                    } else if (i <= monitorDatas.size()) {
                        if (null == everyOneData.get(j)) {
                            everyOneData.set(0, time);
                            everyOneData.set(j + 1, monitorData.getValue());
                        } else {
                            everyOneData.set(j + 1, monitorData.getValue());
                        }
                    }
                    datas.put(time, everyOneData);
                }
            }

        }
        return datas;
    }

    private LinkedList<Object> listFillNull(int length, LinkedList<Object> list) {
        for (int k = 0; k < length; k++) {
            list.add(null);
        }
        return list;
    }

    private final static String TYPE_DOUBLE_PATH = "java.math.BigDecimal";

    private MonitorData onValueAddUnit(String unit, MonitorData monitorData) {
        Object value = monitorData.getValue();
        if (TYPE_DOUBLE_PATH.equals(value.getClass().getName()) && ((unit == null) ? true : (!unit.contains("|")))) {
            monitorData.setValue(value + (unit == null ? "" : unit));
            return monitorData;
        } else {
            UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {
                @Override
                public void parseResult(String result) {
                    // TODO Auto-generated method stub
                    monitorData.setValue(result);
                }
            });
            return monitorData;
        }

    }

    private int getProbeTypeIndex(TreeNode treeNode) {
        Asset<?> findAsset = assetStore.findAsset(treeNode.getId());
        MonitorType type = (MonitorType) findAsset.getType();
        DataType dataType = type.getDataType();
        int index = dataType.getIndex();
        return index;
    }

}

package com.vibe.service.global;

import com.vibe.common.code.CodeCatalog;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.code.CodeItem;
import com.vibe.common.code.MonitorCodes;
import com.vibe.common.data.UseCode;
import com.vibe.mapper.alarm.AlarmMessageDao;
import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.global.HomeDao;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetState;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.drivers.generalprobe.ExecuteExpressionMethod;
import com.vibe.pojo.*;
import com.vibe.service.asset.AssetService;
import com.vibe.service.asset.MonitorService;
import com.vibe.service.classification.Code;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.service.energy.EnergyCountService;
import com.vibe.service.energy.EnergyStatisticsService;
import com.vibe.util.RegixCut;
import com.vibe.utils.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeServiceImpl implements HomeService {

    private static final int DEFAULT_HOME = 0;
    private static final int ALL_HOME = 1;

    @Autowired
    private HomeDao homeDao;

    @Autowired
    AssetStore store;
    @Autowired
    CodeDictManager codeManager;

    @Autowired
    private AlarmMessageDao alarmMessageDao;

    @Autowired
    private AssetStore assetStore;

    @Autowired
    private EnergyStatisticsService energyService;

    @Autowired
    private EnergyCountService energyCountService;

    @Autowired
    private AssetService assetService;
    @Autowired
    private SelectOptionDao selectOptionDao;
    @Autowired
    private CodeDictManager codeDictManager;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private SelectOptionService selectOptionService;

    @Override
    public List<HomeBean> getUserHomes(int userId) {
        // TODO Auto-generated method stub
        List<HomeBean> homeBeans = homeDao.getUserHomes(userId);
        if (homeBeans == null || homeBeans.isEmpty()) {
            List<HomeBean> defaultHomeBeans = homeDao.getHomes(DEFAULT_HOME);
            return defaultHomeBeans;
        }
        return homeBeans;
    }

    @Override
    public void updateHomes(int userId, List<Integer> userHomes) {
        // TODO Auto-generated method stub
        homeDao.deleteUserHomes(userId);
        for (Integer userHomeId : userHomes) {
            homeDao.addUserHomes(userId, userHomeId);
        }
    }

    @Override
    public List<HomeBean> getHomes() {
        // TODO Auto-generated method stub
        return homeDao.getHomes(ALL_HOME);
    }

    private static String PARAMETER_VALUE_SPLIT = "\\|";
    private static String PARAMETER_VALUE_SPLIT_STR = "|";
    private static String PARAMETER_VALUE_ADD_STR = "\\+";
    private static String PARAMETER_VALUE_ADD_SPLIT_STR = "+";
    private static String PARAMETER_VALUE_SPLIT_STR_STATUS = "|status";
    private static String PARAMETER_VALUE_SPLIT_STR_ENERGY = "energy";

    @Override
    public String homeInterfaceMonitor(int homeId) {
        // TODO Auto-generated method stub
        List<HomeLayoutParameter> homeLayoutParameters = homeDao.getHomeLayoutParameter(homeId);
        HomeBean homeBean = homeDao.getHome(homeId);
        String jsonData = homeBean.getJsonData();
        for (HomeLayoutParameter homeLayoutParameter : homeLayoutParameters) {
            String parameterValue = homeLayoutParameter.getParameterValue();
            String parameterName = homeLayoutParameter.getParameterName();
            boolean isContainSu = parameterValue.contains(PARAMETER_VALUE_SPLIT_STR);
            boolean isContainAdd = parameterValue.contains(PARAMETER_VALUE_ADD_SPLIT_STR);
            if (isContainAdd && isContainSu) {
                String[] params = parameterValue.split(PARAMETER_VALUE_ADD_STR);
                int sum = 0;
                for (String param : params) {
                    String[] codeIdValue = param.split(PARAMETER_VALUE_SPLIT);
                    String codeId = codeIdValue[0];
                    String value = codeIdValue[1];
                    // 获取bool类型的probe和control的Id 记录是monitor开启的个数
                    List<Integer> assetIds = homeDao.getAssetIdByCodeId(Integer.parseInt(codeId));
                    for (Integer assetId : assetIds) {
                        if (value.equals(((Monitor<?>) assetStore.findAsset(assetId)).getValue().toString())) {
                            sum++;
                        }
                    }
                }
                jsonData = jsonData.replace(parameterName, sum + "");
                // 包含|不包含 +
            } else if (isContainSu) {
                if (parameterValue.contains(PARAMETER_VALUE_SPLIT_STR_STATUS)) {
                    int index = parameterValue.indexOf(PARAMETER_VALUE_SPLIT_STR_STATUS);
                    String codeId = parameterValue.substring(0, index);
                    String value = parameterValue.substring(index + PARAMETER_VALUE_SPLIT_STR_STATUS.length());
                    // 获取bool类型的probe和control的Id 记录是monitor开启的个数
                    List<Integer> assetIds = new ArrayList<>();
                    if (parameterValue.contains(PARAMETER_VALUE_SPLIT_STR_ENERGY)) {
                        assetIds = homeDao.getEnergyAssetIdByCodeId(Integer.parseInt(codeId));
                    } else {
                        assetIds = homeDao.getAssetIdByCodeId(Integer.parseInt(codeId));
                    }
                    int count = 0;
                    for (Integer assetId : assetIds) {
                        if (value.contains((String.valueOf(((Monitor<?>) assetStore.findAsset(assetId)).getState())))) {
                            count++;
                        }
                    }
                    jsonData = jsonData.replace(parameterName, count + "");
                } else {
                    String[] codeIdValue = parameterValue.split(PARAMETER_VALUE_SPLIT);
                    String codeId = codeIdValue[0];
                    String value = codeIdValue[1];
                    // 获取bool类型的probe和control的Id 记录是monitor开启的个数
                    List<Integer> assetIds = new ArrayList<>();
                    if (parameterValue.contains(PARAMETER_VALUE_SPLIT_STR_ENERGY)) {
                        assetIds = homeDao.getEnergyAssetIdByCodeId(Integer.parseInt(codeId));
                    } else {
                        assetIds = homeDao.getAssetIdByCodeId(Integer.parseInt(codeId));
                    }
                    int count = 0;
                    for (Integer assetId : assetIds) {
                        if (value.equals((String.valueOf(((Monitor<?>) assetStore.findAsset(assetId)).getValue())))) {
                            count++;
                        }
                    }
                    jsonData = jsonData.replace(parameterName, count + "");
                }
                // 包含+不包含|
            } else if (isContainAdd) {
                String[] params = parameterValue.split(PARAMETER_VALUE_ADD_STR);
                int sum = 0;
                for (String param : params) {
                    sum += homeDao.getAssetCountByCodeId(Integer.parseInt(param));
                }
                jsonData = jsonData.replace(parameterName, sum + "");
            } else {
                int count;
                if (parameterValue.contains(PARAMETER_VALUE_SPLIT_STR_ENERGY)) {
                    parameterValue = parameterValue.substring(0, parameterValue.indexOf(PARAMETER_VALUE_SPLIT_STR_ENERGY));
                    count = homeDao.getEnergyAssetCountByCodeId(Integer.parseInt(parameterValue));
                } else {
                    count = homeDao.getAssetCountByCodeId(Integer.parseInt(parameterValue));
                }
                jsonData = jsonData.replace(parameterName, count + "");
            }
        }
        return jsonData;
    }

    private static String RATE_PARAMETER_NAME = "#rate";
    private static String DO_PARAMETER_NAME = "#do";
    private static String UNDO_PARAMETER_NAME = "#undo";

    @Override
    public String homeInterfaceAlarm(int homeId) {
        // TODO Auto-generated method stub
        HomeBean homeBean = homeDao.getHome(homeId);
        List<HomeLayoutParameter> homeLayoutParameters = homeDao.getHomeLayoutParameter(homeId);
        String jsonData = homeBean.getJsonData();
        int undoCount = homeDao.getUnDoAlarmCount();
        int doCount = homeDao.getDoAlarmCount();
        int rate = 0;
        if ((doCount + undoCount) != 0) {
            rate = (int) (((doCount + 0.0) / ((doCount + 0.0) + (undoCount + 0.0))) * 100);
        }
        jsonData = jsonData.replace(RATE_PARAMETER_NAME, rate + "");
        jsonData = jsonData.replace(DO_PARAMETER_NAME, doCount + "");
        jsonData = jsonData.replace(UNDO_PARAMETER_NAME, undoCount + "");
        for (HomeLayoutParameter homeLayoutParameter : homeLayoutParameters) {
            int count = homeDao.getAlarmCountByCodeId(Integer.parseInt(homeLayoutParameter.getParameterValue()));
            jsonData = jsonData.replace(homeLayoutParameter.getParameterName(), count + "");
        }
        return jsonData;
    }

    @Override
    public AlarmModuleAll homeInterfaceAlarmByModule(String modules) {
        int doNum = homeDao.countDoAlarm(modules);
        int allNum = homeDao.countAllAlarm(modules);
        List<AlarmModule> alarmModuleList = homeDao.countAlarmByModules(modules);
        AlarmModuleAll alarmModuleAll = new AlarmModuleAll(doNum, allNum, alarmModuleList);
        return alarmModuleAll;
    }

    @Override
    public List<AlarmModule> homeInterfaceAlarmById(String ids) {
        return homeDao.countAlarmByIds(ids);
    }

    @Override
    public int countAlarm() {
        return homeDao.countAlarm();
    }

    @Override
    public List<AlarmModule> countAlarmByCatalogs(String catalog) {
        return homeDao.countAlarmByModules(catalog);
    }

    @Override
    public Map<String, Integer> countMonitorByCatalogs(String catalog) {
        String[] ids = catalog.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String id : ids) {
            map.put(id, homeDao.getEnergyAssetCountByCodeId(Integer.parseInt(id)));
        }
        return map;
    }

    @Override
    public Map<String, Object> deviceTypeAlarmProportion() {
        String ids = "9000,9001,9002";
        int allAlarm = this.countAlarm();
        AlarmModuleAll alarmModuleAll = this.homeInterfaceAlarmByModule("14");
        List<AlarmModule> alarmIdList = this.homeInterfaceAlarmById(ids);
        Set<String> idSet = new HashSet<>();
        for (AlarmModule alarmModule : alarmIdList) {
            idSet.add(alarmModule.getId());
        }
        Map<String, String> idMap = new HashMap<>();
        idMap.put("9000", "烟感");
        idMap.put("9001", "温感");
        idMap.put("9002", "手报");
        Set<String> keySet = idMap.keySet();
        for (Map.Entry<String, String> entry : idMap.entrySet()) {
            if (!idSet.contains(entry.getKey())) {
                AlarmModule alarmModule = new AlarmModule(entry.getKey(), entry.getValue());
                alarmIdList.add(alarmModule);
            }
        }
        alarmIdList.addAll(alarmModuleAll.getAlarmModuleList());
        Map<String, Object> alarmMap = new HashMap<>();
        alarmMap.put("allAlarm", allAlarm);
        alarmMap.put("alarms", alarmIdList);
        return alarmMap;
    }

    @Override
    public Map<String, List<Object>> countMonitorStatus(String ids) {
        String[] idArr = ids.split(",");
        Set<String> idSet = new HashSet<String>(Arrays.asList(idArr));
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
        int all = 0;
        int alarm = 0;
        for (Asset<?> asset : this.store.getAssets()) {
            if (asset.isMonitor()) {
                if (idSet.contains(String.valueOf(((Monitor) asset).getMonitorKind()))) {
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

    @Override
    public Map<String, Integer> homeAssetHealth(Integer spaceId, Integer kind) {
        List<Integer> catalogList = getCatalogList(-1, null, 2002);
        List<TreeNode> buildAssetList = monitorService.buildAssetListPage(catalogList, spaceId, kind, null, "");
        List<TreeNode> assetList = likeFindCaption(buildAssetList, null);
        int health = 0, subHealth = 0, unHealth = 0;
        for (TreeNode treeNode : assetList) {
            if (treeNode.getHealthIndex() != null) {
                int nodeHealth = Integer.parseInt(treeNode.getHealthIndex());
                if (nodeHealth >= 90) {
                    health++;
                } else if (nodeHealth <= 59) {
                    unHealth++;
                } else {
                    subHealth++;
                }
            } else {
                unHealth++;
            }

        }
        Map<String, Integer> healthMap = new HashMap<>();
        healthMap.put("all", assetList.size());
        healthMap.put("health", health);
        healthMap.put("subHealth", subHealth);
        healthMap.put("unHealth", unHealth);
        return healthMap;
    }

    @Override
    public Map<String, Object> getEnergyModule(EnergyModule energyModule) {
        if (energyModule.getFloorId() == null) {
            energyModule.setFloorId(1);
        }
        if (energyModule.getCatalog() == null) {
            energyModule.setCatalog(34);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("energy", energyService.getEnergyElecAndWater(energyModule, "1", "1"));
        map.put("elecProp", energyCountService.getEnergyComAndSeq(energyModule, energyModule.getFloorId()));
        energyModule.setCatalog(37);
        map.put("waterProp", energyCountService.getEnergyComAndSeq(energyModule, energyModule.getFloorId()));
        map.put("alarm", this.countAlarmByCatalogs(energyModule.getCatalogs()));
        Map<String, Integer> numMap = this.countMonitorByCatalogs(energyModule.getCatalogs());
        numMap.put("waterNum", numMap.get("4000"));
        numMap.put("elecNum", numMap.get("1002"));
        numMap.remove("1002");
        numMap.remove("4000");
        map.put("num", numMap);
        return map;
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

    @Override
    public String homeInterfaceFault(int homeId) {
        // TODO Auto-generated method stub
        HomeBean homeBean = homeDao.getHome(homeId);
        String jsonData = homeBean.getJsonData();
        int undoCount = homeDao.getUnDoFaultCount();
        int doCount = homeDao.getDoFaultCount();
        int rate = 0;
        if ((doCount + undoCount) != 0) {
            rate = (int) (((doCount + 0.0) / ((doCount + 0.0) + (undoCount + 0.0))) * 100);
        }
        jsonData = jsonData.replace(RATE_PARAMETER_NAME, rate + "");
        jsonData = jsonData.replace(DO_PARAMETER_NAME, doCount + "");
        jsonData = jsonData.replace(UNDO_PARAMETER_NAME, undoCount + "");

        return jsonData;
    }

    private static String UNDO_NUM_PARAMETER_NAME = "#undonum";
    private static String TOTAL_PARAMETER_NAME = "#total";
    private static String MONTH_DO_PARAMETER_NAME = "#monthdonum";
    private static String TODAY_DO_PARAMETER_NAME = "#todaydonum";
    private static String MONTH_TOTAL_PARAMETER_NAME = "#monthtotalnum";
    private static String TODAY_TOTAL_PARAMETER_NAME = "#todaytotalnum";

    @Override
    public String homeInterfaceTask(int homeId) {
        // TODO Auto-generated method stub
        HomeBean homeBean = homeDao.getHome(homeId);
        String jsonData = homeBean.getJsonData();
        jsonData = jsonData.replace(UNDO_NUM_PARAMETER_NAME, homeDao.getUndoTaskCount() + "");
        jsonData = jsonData.replace(TOTAL_PARAMETER_NAME, homeDao.getTotalTaskCount() + "");
        jsonData = jsonData.replace(MONTH_DO_PARAMETER_NAME, homeDao.getMonthDoTaskCount() + "");
        jsonData = jsonData.replace(TODAY_DO_PARAMETER_NAME, homeDao.getTodayDoTaskCount() + "");
        jsonData = jsonData.replace(MONTH_TOTAL_PARAMETER_NAME, homeDao.getMonthTotalTaskCount() + "");
        jsonData = jsonData.replace(TODAY_TOTAL_PARAMETER_NAME, homeDao.getTodayTotalTaskCount() + "");

        return jsonData;
    }

    @Override
    public String homeInterfaceEnvironment(int homeId) {
        // TODO Auto-generated method stub
        HomeBean homeBean = homeDao.getHome(homeId);
        String jsonData = homeBean.getJsonData();
        List<HomeLayoutParameter> homeLayoutParameters = homeDao.getHomeLayoutParameter(homeId);
        if (jsonData.contains("#")) {
            for (HomeLayoutParameter homeLayoutParameter : homeLayoutParameters) {
                String value = null;
                String parameterValue = homeLayoutParameter.getParameterValue();
                int id = Integer.parseInt(parameterValue);
                Monitor<?> mo = (Monitor<?>) assetStore.findAsset(id);
                Object assetValue = mo.getValue();
                if (assetValue == null) {
                    value = "";
                } else {
                    value = new UseCode(codeDictManager).getValue(assetValue, (short) Code.PROBE, mo.getMonitorKind());
                }
                jsonData = jsonData.replace(homeLayoutParameter.getParameterName(), value);

            }
        }
        return jsonData;
    }

    @Override
    public Map<Object, Object> homeInterfaceAsset(String codeNames, int type) {
        // TODO Auto-generated method stub
        System.out.println("codeNames " + codeNames);
        Map<Object, Object> map = new HashMap<>();
        String[] codeNameArr = codeNames.split(",");
        for (String codeName : codeNameArr) {
            HomeDeviceState homeDeviceState = homeInterfaceAssetItem(codeName, type);
            map.put(codeName, homeDeviceState);
        }
        return map;
    }

    private HomeDeviceState homeInterfaceAssetItem(String codeName, int type) {
        Integer catlog = CatlogAssetCache.getCatlogAssetCache(codeDictManager, assetStore, selectOptionDao)
                .getCodeNameId().get(codeName);
        List<Asset<?>> assets = null;
        if (catlog != null) {
            if (type == 2001) {
                assets = CatlogAssetCache.getCatlogAssetCache(codeDictManager, assetStore, selectOptionDao)
                        .getMonitorCatlogAssetMap().get(catlog);
            } else {
                assets = CatlogAssetCache.getCatlogAssetCache(codeDictManager, assetStore, selectOptionDao)
                        .getDeviceCatlogAssetMap().get(catlog);
            }
        }
        HomeDeviceState homeDeviceState = new HomeDeviceState();
        int normal = 0;
        int error = 0;
        int unknown = 0;
        int warning = 0;
        if (assets != null) {
            for (Asset<?> asset : assets) {
                switch (asset.getState()) {
                    case NORMAL:
                        normal++;
                        break;
                    case UNKNOWN:
                        unknown++;
                        break;
                    case WARNING:
                        warning++;
                        break;
                    case ERROR:
                        error++;
                        break;
                }
            }
        }

        homeDeviceState.setError(error);
        homeDeviceState.setNormal(normal);
        homeDeviceState.setUnknown(unknown);
        homeDeviceState.setWarning(warning);
        return homeDeviceState;
    }

    @Override
    public int alarmCount() {
        // TODO Auto-generated method stub
        return alarmMessageDao.alarmCount();
    }

    @Override
    public Map<Object, Object> allInterface(String url) {
        return ExecuteExpressionMethod.allInterface(url);
    }

    @Override
    public List<HomeCamera> homeCamera(String codeName) {
        // TODO Auto-generated method stub
        int size = 0;
        Random random = new Random();
        Integer catlog = CatlogAssetCache.getCatlogAssetCache(codeDictManager, assetStore, selectOptionDao)
                .getCodeNameId().get(codeName);
        List<HomeCamera> homeCameras = new ArrayList<>();
        if (catlog != null) {
            List<Asset<?>> assets = CatlogAssetCache.getCatlogAssetCache(codeDictManager, assetStore, selectOptionDao)
                    .getDeviceCatlogAssetMap().get(catlog);
            if (assets != null) {
                List<Asset<?>> subAssets = new ArrayList<>();
                while (true) {
                    if (size == 3) {
                        break;
                    }
                    size++;
                    subAssets.add(assets.get(random.nextInt(assets.size() - 1)));
                }
                for (Asset<?> asset : subAssets) {
                    HomeCamera homeCamera = new HomeCamera();
                    AssetVo assetVo = assetService.assetDteail(asset, "device");
                    homeCamera.setCaption(assetVo.getCaption());
                    homeCamera.setUrl(assetVo.getRtspUrlPattern());
                    homeCameras.add(homeCamera);
                }
            }
        }
        return homeCameras;
    }

    @Override
    public Map<Object, Object> allDeviceStateData() {
        // TODO Auto-generated method stub
        return ExecuteExpressionMethod.allDeviceStateData();
    }

    @Override
    public Map<Short, XiaofangCount> xiaofangCountData() {
        // TODO Auto-generated method stub
        return CatlogAssetCache.getCatlogAssetCache(codeDictManager, assetStore, selectOptionDao).getXiaofangCountMap();
    }


}

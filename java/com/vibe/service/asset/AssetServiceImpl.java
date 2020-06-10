package com.vibe.service.asset;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import com.vibe.monitor.drivers.generic.GenericDevice;
import com.vibe.monitor.drivers.video.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.common.Application;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.data.UseCode;
import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.dept.DeptMapper;
import com.vibe.mapper.monitor.AssetRepeatMapper;
import com.vibe.mapper.statistics.MonitorDataMapper;
import com.vibe.mapper.user.UserMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.AssetInstanceLoader;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetProperty;
import com.vibe.monitor.asset.AssetState;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.AssetTypeManager;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Control;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.asset.MonitorService;
import com.vibe.monitor.asset.Probe;
import com.vibe.monitor.asset.Space;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.monitor.asset.type.AssetTypeProperty;
import com.vibe.monitor.asset.type.ControlType;
import com.vibe.monitor.asset.type.DeviceType;
import com.vibe.monitor.asset.type.MonitorType;
import com.vibe.monitor.asset.type.ProbeType;
import com.vibe.monitor.asset.type.ServiceType;
import com.vibe.monitor.asset.type.SpaceType;
import com.vibe.monitor.db.DBAssetInstanceLoader;
import com.vibe.monitor.drivers.websocket.securityalarm.service.SocketSecurityService;
import com.vibe.monitor.util.UnitInterface;
import com.vibe.monitor.util.UnitUtil;
import com.vibe.parse.DeviceIdResult;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.DeviceCatalogSpace;
import com.vibe.pojo.MonitorData;
import com.vibe.pojo.user.Department;
import com.vibe.pojo.user.User;
import com.vibe.service.classification.Code;
import com.vibe.util.TableDataType;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.EasyUITreeNode;
import com.vibe.utils.QRCodeUtils;
import com.vibe.utils.TreeNode;
import com.vibe.utils.time.TimeSpan;
import com.vibe.utils.time.TimeUtil;
import com.vibe.utils.type.DataType;

@Service
public class AssetServiceImpl implements AssetService {
    private static final int ENERGY_CATALOG = 34;
    @Autowired
    private AssetRepeatMapper assetMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    CodeDictManager codeDictManager;
    @Autowired
    private AssetStore assetStore;
    @Autowired
    private Application application;
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private SelectOptionDao selectOptionDao;
    @Autowired
    private MonitorDataMapper monitorDataMapper;

    public AssetServiceImpl(AssetStore assetStore) {
        super();
        this.assetStore = assetStore;
    }

    @Override
    public void addAssetByKind(AssetVo assetVo) throws AssetException {

        // 获取分类
        String kind = assetVo.getKind();
        AssetKind assetKind = AssetKind.valueOf(kind);
        // 获取到多有类型
        AssetTypeManager<AssetType> assetTypes = assetStore.getAssetTypes(assetKind);
        final String typeName = assetVo.getTypeName();
        if (typeName == null) {
            System.out.println("类型名称为null");
        }
        int parentId = assetVo.getParentId();
        String name = assetVo.getName();
        AssetType atType = assetTypes.find(typeName);
        Asset<?> parent = assetStore.findAsset(parentId);
        // 获取到输入类型的实例，创建asset实例，封装参数
        Integer assetId = assetVo.getId();
        if (assetId == null) {
            int id = Math.abs((parent.getName() + name).hashCode());
            assetVo.setId(id);
        }
        if (null != atType) {
            Asset<?> asset = assetStore.createAsset(atType, assetVo.getId(), name);

            List<AssetProperty> valueList = assetVo.getValueList();
            asset.setCaption(assetVo.getCaption());
            asset.setDesc(assetVo.getMemo());
            asset.setParentId(parentId);

            Asset<?> setAsset = setAsset(assetVo, asset);

            addAsset(assetStore, setAsset, valueList);
        }

    }

    /* 封装资产列表数据 */
    public EasyUIJson assetList(Integer rows, Integer page, Asset<?> root, int workId) {

        List<Integer> optionList = selectOptionDao.queryIdListByParent(workId);
        List<AssetVo> children = new ArrayList<AssetVo>();
        Collection<Asset<?>> childrens = new ArrayList<>();
        if (root != null) {
            if (root.isCompound()) {
                CompoundAsset<?> parent = (CompoundAsset<?>) root;
                childrens = parent.children();
                for (Asset<?> asset : childrens) {
                    if (!"AmountOfChangeFloatProbe".equals(asset.getType().getName())) {
                        AssetVo vo = new AssetVo();
                        vo.setFullName(assetStore.getAssetFullName(asset, ">", "name"));
                        vo.setId(asset.getId());
                        AssetKind kind = asset.getKind();
                        //vo.setKind(kind.ordinal()+"");
                        vo.setKind(kind.toString());
                        vo.setCaption(asset.getCaption());
                        vo.setName(asset.getName());
                        vo.setTypeName(asset.getType().getName());
                        if (asset.getState() != null) {
                            vo.setState(asset.getState().toString());
                            vo.setError(asset.getError());
                        }
                        Integer catalog = null;
                        if (asset.isMonitor()) {
                            Monitor<?> monitor = (Monitor<?>) asset;
                            loadMonitorVo(monitor, vo);
                            catalog = (int) monitor.getMonitorKind();
                        } else if (asset instanceof Device) {
                            Device device = (Device) asset;
                            vo.setCatalogId((int) device.getCatalog());
                            if (asset instanceof WebCamera) {
                                WebCamera webCamera = (WebCamera) asset;
                                vo.setUsername(webCamera.getUserName());
                                vo.setPassword(webCamera.getPassword());
                                vo.setHost(webCamera.getHost());
                                vo.setPort(webCamera.getPort());
                                vo.setRtspUrlPattern(webCamera.getRtspUrlPattern());
                            }
                            catalog = (int) device.getCatalog();
                        }
                        setKindStr(asset, vo);
                        if (catalog == null || workId == 0) {
                            children.add(vo);
                        } else if (optionList.contains(catalog)) {
                            children.add(vo);
                        }
                        /*
                         * if (asset.getKind()==AssetKind.DEVICE &&
                         * ((Device)asset).getShow_in_client()==2) {
                         *
                         * }else {
                         *
                         * }
                         */
                    }
                }
            }
        }
        /*
         * for (AssetVo asset2 : children) {
         * System.out.println(asset2.getCatalogId() + "===" +
         * asset2.getCaption()); }
         */
        // 创建EasyUIJson对象，封装查询结果
        EasyUIJson uiJson = new EasyUIJson();
        // 设置查询总记录数
        uiJson.setTotal((long) children.size());
        // 设置查询记录
        uiJson.setRows(
                children.subList((page - 1) * rows, page * rows > children.size() ? children.size() : page * rows));
        return uiJson;
    }

    private void setKindStr(Asset<?> asset, AssetVo vo) {
        switch (asset.getKind()) {
            case SPACE:
                vo.setKindStr("空间");
                break;
            case PROBE:
                vo.setKindStr("监测器");
                break;
            case CONTROL:
                vo.setKindStr("控制器");
                break;
            case DEVICE:
                vo.setKindStr("设备");
                break;
            case SERVICE:
                vo.setKindStr("服务");
                break;
            default:
                break;
        }
    }

    private void loadMonitorVo(Monitor<?> monitor, AssetVo vo) {
        final long seconds = monitor.getSavingInterval().toSeconds() * 1;
        LocalDateTime startTime = LocalDateTime.now().minusSeconds(seconds);
        Object value = monitor.getValue();
        String dataTypeName = monitor.getDataTypeName();
        String tableName = TableDataType.getTableName(dataTypeName);
        List<MonitorData> loadRecently = monitorDataMapper.loadRecently(tableName, monitor.getId(), startTime, null);
        if (loadRecently != null && loadRecently.size() > 0 && monitor.getState() != AssetState.ERROR) {
            MonitorData monitorData = loadRecently.get(0);
            value = monitorData.getValue();
        }
        String unit = monitor.getUnit();

        vo.setUnit(monitor.getUnit());
        int catalog = monitor.getMonitorKind();
        if (monitor instanceof Control) {
            switch (dataTypeName) {
                case "INT":
                    vo.setMonitorType("intcontrol");
                    break;
                case "BOOLEAN":
                    vo.setMonitorType("boolcontrol");
                    break;
                case "FLOAT":
                    vo.setMonitorType("floatcontrol");
                    break;
                default:
                    break;
            }
        }

        vo.setCatalogId(catalog);
        if (value != null) {
            if (value instanceof Float && ((unit == null) ? true : (!unit.contains("|")))) {
                String number = new UseCode(codeDictManager).getValue(value, (short) Code.PROBE, (short) catalog);
                vo.setValueStr(number + (unit == null ? "" : unit));
                vo.setValue(number);
            } else {
                vo.setValue(value.toString());
                UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {

                    @Override
                    public void parseResult(String result) {
                        // TODO Auto-generated method stub
                        vo.setValueStr(result);
                    }
                });

            }
        }
    }

    /* 打印资产tree的递归方法 */
    public AllTreeNode printAssetTree(final Asset<?> root, String flag, Integer catlog) {
        AllTreeNode treeNode = new AllTreeNode();
        /*
         * treeNode.setId(root.getId()); treeNode.setState("open");
         * treeNode.setText(root.getCaption());
         */
        if (root.isCompound()) {
            /* AllTreeNode sonNode = new AllTreeNode(); */
            treeNode.setId(root.getId());
            treeNode.setKind(root.getKind().ordinal());
            treeNode.setStatus(root.getState().ordinal());
            treeNode.setState("closed");
            treeNode.setName(root.getName());
            treeNode.setText(root.getCaption());

            if (root instanceof WebCamera) {
                treeNode.setUnit("camera");
            }
            CompoundAsset<?> parent = (CompoundAsset<?>) root;
            List<AllTreeNode> list = new ArrayList<AllTreeNode>();
            Collection<Asset<?>> children = parent.children();
            if (children != null && children.size() > 0) {

                for (Asset<?> child : parent.children()) {
                    if ("device".equals(flag) && isTrue(child)) {
                        AssetKind kind = child.getKind();
                        if (kind != null) {
                            if (kind.equals(AssetKind.DEVICE) || kind.equals(AssetKind.SPACE)) {
                                AllTreeNode node2 = printAssetTree(child, flag, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                        }
                    } else if ("total".equals(flag)) {
                        if (child != null && isTrue(child)) {
                            /*
                             * if(child.isMonitor() ||
                             * child.getKind().equals(AssetKind.DEVICE)){
                             *
                             * }
                             */
                            AllTreeNode node2 = printAssetTree(child, flag, catlog);
                            if (node2 != null) {
                                list.add(node2);
                            }
                        }
                    } else if ("space".equals(flag) && isTrue(child)) {
                        if (AssetKind.SPACE.equals(child.getKind())) {
                            AllTreeNode nodespace = printAssetTree(child, flag, catlog);
                            if (nodespace != null) {
                                list.add(nodespace);
                            }
                        }
                    } else if ("sensor".equals(flag) && isTrue(child)) {
                        if (child.isCompound()) {
                            AllTreeNode node2 = printAssetTree(child, flag, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                        } else {
                            AllTreeNode node2 = printAssetTree(child, flag, catlog);
                            if (node2 != null) {
                                if (child.getKind() == AssetKind.PROBE && node2.getDataType().equals("FLOAT")) {
                                    list.add(node2);
                                }
                            }
                        }

                    } else if ("bool".equals(flag) && isTrue(child)) {
                        if (child.isCompound()) {
                            AllTreeNode node2 = printAssetTree(child, flag, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    if (hasBoolMonitorBool(parentC)) {
                                        list.add(node2);
                                    }
                                }
                            }
                        } else {
                            if ((child.getKind() == AssetKind.CONTROL || child.getKind() == AssetKind.PROBE)
                                    && ((Monitor<?>) child).getUnit() != null
                                    && ((Monitor<?>) child).getUnit().contains("|")) {
                                AllTreeNode node2 = printAssetTree(child, flag, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }

                        }
                    } /*
                     * else if ("inputProbe".equals(flag)) { if
                     * (child.isCompound()) { AllTreeNode node2 =
                     * printAssetTree(child, flag); CompoundAsset<?> parentC
                     * = (CompoundAsset<?>) child; Collection<Asset<?>>
                     * childrenC = parentC.children(); if (childrenC != null
                     * && childrenC.size() > 0) { if (node2 != null) {
                     * list.add(node2); } } } else { if (child instanceof
                     * InputProbe) { AllTreeNode node2 =
                     * printAssetTree(child, flag); if (node2 != null) {
                     * list.add(node2); } }
                     *
                     * } }
                     */ else if ("boolcontrolcamera".equals(flag) && isTrue(child)) {
                        if (child.isCompound()) {

                            if (child instanceof WebCamera) {
                                AllTreeNode node2 = printAssetTree(child, flag, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            } else {
                                AllTreeNode node2 = printAssetTree(child, flag, catlog);
                                CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                                Collection<Asset<?>> childrenC = parentC.children();
                                if (childrenC != null && childrenC.size() > 0) {
                                    if (node2 != null) {
                                        if (hasBoolControlBool(parentC) || hasCamera(parentC)) {
                                            list.add(node2);
                                        }
                                    }
                                }
                            }

                        } else {
                            if ((child.getKind() == AssetKind.CONTROL) && ((Monitor<?>) child).getUnit() != null
                                    && ((Monitor<?>) child).getUnit().contains("|")) {
                                AllTreeNode node2 = printAssetTree(child, flag, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }

                        }
                    } else if ("control".equals(flag) && isTrue(child)) {
                        if (child.isCompound()) {
                            AllTreeNode node2 = printAssetTree(child, flag, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    if (hasControl(parentC, catlog)) {
                                        list.add(node2);
                                    }
                                }
                            }

                        } else {
                            if ((child.getKind() == AssetKind.CONTROL) && ((Monitor<?>) child).getUnit() != null
                                    && ((Monitor<?>) child).getMonitorKind() == catlog) {
                                AllTreeNode node2 = printAssetTree(child, flag, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }

                        }
                    } else {
                        if (child.isCompound()) {
                            AllTreeNode node2 = printAssetTree(child, flag, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                        }
                    }
                    if (list != null && list.size() > 0) {
                        if ("space".equals(flag) && isTrue(child)) {
                            list.sort(new Comparator<AllTreeNode>() {
                                @Override
                                public int compare(AllTreeNode o1, AllTreeNode o2) {
                                    return Integer.compare(((Space) assetStore.findAsset(o1.getId())).getSequence(),
                                            ((Space) assetStore.findAsset(o2.getId())).getSequence());
                                }
                            });
                        } else {
                            list.sort(new Comparator<AllTreeNode>() {
                                @Override
                                public int compare(AllTreeNode o1, AllTreeNode o2) {
                                    if (assetStore.findAsset(o1.getId()) instanceof Space && assetStore.findAsset(o1.getId()) instanceof Space) {
                                        return Integer.compare(((Space) assetStore.findAsset(o1.getId())).getSequence(),
                                                ((Space) assetStore.findAsset(o2.getId())).getSequence());
                                    }
                                    return 0;
                                }
                            });
                        }
                        treeNode.setChildren(list);
                    }
                }
            }
        } else {
            treeNode.setId(root.getId());
            treeNode.setKind(root.getKind().ordinal());
            treeNode.setStatus(root.getState().ordinal());
            treeNode.setState("closed");
            treeNode.setText(root.getCaption());
            if ((root.getKind() == AssetKind.CONTROL || root.getKind() == AssetKind.PROBE)) {
                treeNode.setUnit(((Monitor<?>) root).getUnit());
            }
            if (root.getKind().equals(AssetKind.PROBE)) {
                Probe probe = (Probe) root;
                DataType dataType = probe.getType().getDataType();
                String name = dataType.getName();
                treeNode.setDataType(name);
            }
        }
        return treeNode;
    }

    private boolean isTrue(Asset<?> child) {
        if (!"AmountOfChangeFloatProbe".equals(child.getType().getName()) && !"SumFloatProbe".equals(child.getType().getName())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasBoolMonitorBool(CompoundAsset<?> parent) {
        return hasBoolMonitor(parent, new Value()).isFlag();
    }

    private boolean hasBoolControlBool(CompoundAsset<?> parent) {
        return hasBoolControl(parent, new Value()).isFlag();
    }

    private boolean hasCamera(CompoundAsset<?> parent) {
        return hasCamera(parent, new Value()).isFlag();
    }

    private boolean hasControl(CompoundAsset<?> parent, Integer catlog) {
        return hasControl(parent, new Value(), catlog).isFlag();
    }

    class Value {
        private boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

    }

    private Value hasBoolMonitor(CompoundAsset<?> parent, Value flag) {
        if (parent != null && parent.children() != null) {
            for (Asset<?> child : parent.children()) {
                if (child.isCompound()) {
                    CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                    Collection<Asset<?>> childrenC = parentC.children();
                    if (childrenC != null && childrenC.size() > 0) {
                        hasBoolMonitor(parentC, flag);
                    }
                } else {
                    if ((child.getKind() == AssetKind.CONTROL || child.getKind() == AssetKind.PROBE)
                            && ((Monitor<?>) child).getUnit() != null && ((Monitor<?>) child).getUnit().contains("|")) {
                        flag.setFlag(true);
                        return flag;
                    }

                }
            }
        }
        return flag;

    }

    private Value hasControl(CompoundAsset<?> parent, Value flag, Integer catlog) {
        if (parent != null && parent.children() != null) {
            for (Asset<?> child : parent.children()) {
                if (child.isCompound()) {
                    CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                    Collection<Asset<?>> childrenC = parentC.children();
                    if (childrenC != null && childrenC.size() > 0) {
                        hasControl(parentC, flag, catlog);
                    }
                } else {
                    if ((child.getKind() == AssetKind.CONTROL) && ((Monitor<?>) child).getUnit() != null
                            && ((Monitor<?>) child).getMonitorKind() == catlog) {
                        flag.setFlag(true);
                        return flag;
                    }

                }
            }
        }
        return flag;

    }

    private Value hasBoolControl(CompoundAsset<?> parent, Value flag) {
        if (parent != null && parent.children() != null) {
            for (Asset<?> child : parent.children()) {
                if (child.isCompound()) {
                    CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                    Collection<Asset<?>> childrenC = parentC.children();
                    if (childrenC != null && childrenC.size() > 0) {
                        hasBoolControl(parentC, flag);
                    }
                } else {
                    if ((child.getKind() == AssetKind.CONTROL) && ((Monitor<?>) child).getUnit() != null
                            && ((Monitor<?>) child).getUnit().contains("|")) {
                        flag.setFlag(true);
                        return flag;
                    }

                }
            }
        }
        return flag;

    }

    private Value hasCamera(CompoundAsset<?> parent, Value flag) {
        if (parent != null && parent.children() != null) {
            for (Asset<?> child : parent.children()) {
                if (child.getKind() == AssetKind.SPACE) {
                    CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                    Collection<Asset<?>> childrenC = parentC.children();
                    if (childrenC != null && childrenC.size() > 0) {
                        hasCamera(parentC, flag);
                    }
                } else {
                    if (child instanceof WebCamera) {
                        flag.setFlag(true);
                        return flag;
                    }

                }
            }
        }
        return flag;

    }

    public List<AllTreeNode> initAssetTree(String flag, Integer catlog) {
        Space root = assetStore.getRoot();
        Collection<Asset<?>> childrens = root.children();
        List<AllTreeNode> nodeList = new ArrayList<AllTreeNode>();
        for (Asset<?> child : childrens) {
            AllTreeNode newnode = null;
            // int siteId = application.getSiteId() << 16;
            if (child != null) { // && child.getId() == siteId) {
                newnode = printAssetTree(child, flag, catlog);
            }
            if (newnode != null) {
                nodeList.add(newnode);
            }
        }
        return nodeList;
    }

    public List<TreeNode> initAssetAllTree(String flag, Integer locationRoot, Integer ringCount, Integer catlog) {
        /*
         * if(catlog!=null){ catlog =
         * (int)((CodeItem)((CascadeCodeItem)codeDictManager.getLocalDict().
         * getItem((short)Code.DEVICE,
         * (short)(int)catlog)).getChildren().toArray()[0]).getId(); }
         */
        Space root = assetStore.getRoot();
        Collection<Asset<?>> childrens = root.children();
        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        for (Asset<?> child : childrens) {
            TreeNode newnode = null;
            // int siteId = application.getSiteId() << 16;
            if (child != null && locationRoot != null && child.getId() == locationRoot) {// &&
                // child.getId()
                // ==
                // siteId)
                // {
                int count = 0;
                newnode = printAssetAllTree(child, flag, ringCount, count, catlog);
            }
            if (newnode != null) {
                nodeList.add(newnode);
            }
        }
        return nodeList;
    }

    private void onAssetsSort(List<TreeNode> nodes) {
        Collections.sort(nodes, (t1, t2) -> {
            if (t1.getId() > t2.getId()) {
                return 1;
            }
            if (t1.getId() == t2.getId()) {
                return 0;
            }
            return -1;

        });
    }

    /* 新版本 */
    public TreeNode printAssetAllTree(final Asset<?> root, String flag, Integer depth, int count, Integer catlog) {
        count++;
        if (depth != null && count > depth)
            return null;
        TreeNode treeNode = new TreeNode();
        treeNode.setFullName(assetStore.getAssetFullName(root, ">", "name"));
        if (root.isCompound()) {
            treeNode.setId(root.getId());
            treeNode.setKind(root.getKind().ordinal());
            treeNode.setStatus(root.getState().ordinal());
            treeNode.setText(root.getCaption());
            treeNode.setName(root.getName());
            CompoundAsset<?> parent = (CompoundAsset<?>) root;
            List<TreeNode> list = new ArrayList<TreeNode>();
            Collection<Asset<?>> children = parent.children();
            if (children != null && children.size() > 0) {
                for (Asset<?> child : parent.children()) {
                    if ("device".equals(flag)) {
                        AssetKind kind = child.getKind();
                        if (kind != null) {
                            if (kind.equals(AssetKind.DEVICE) || kind.equals(AssetKind.SPACE)) {
                                TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                        }

                    } else if ("total".equals(flag)) {
                        if (child != null) {
                            TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);

                            if (node2 != null) {
                                list.add(node2);
                            }
                        }
                    } else if ("deviceOnly".equals(flag)) {
                        if (AssetKind.DEVICE.equals(child.getKind())) {
                            TreeNode nodespace = printAssetAllTree(child, flag, depth, count, catlog);
                            boolean parentIsSpace = child.getParent().getKind() == AssetKind.SPACE;
                            Integer catalogParentId = selectOptionDao.getParentId(((Device) child).getCatalog(), Code.DEVICE);
                            boolean catalogIsEq = catalogParentId == (short) (int) catlog;
                            if (nodespace != null && parentIsSpace && catalogIsEq) {
                                list.add(nodespace);
                            }
                            onAssetsSort(list);
                        }
                    } else if ("space".equals(flag)) {
                        if (AssetKind.SPACE.equals(child.getKind())) {
                            TreeNode nodespace = printAssetAllTree(child, flag, depth, count, catlog);
                            if (nodespace != null) {
                                list.add(nodespace);
                            }
                        }
                    } else if ("deviceAndProbe".equals(flag)) {
                        AssetKind kind = child.getKind();
                        if (kind != null) {
                            if (kind.equals(AssetKind.DEVICE) || kind.equals(AssetKind.PROBE)) {
                                TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                            onAssetsSort(list);
                        }
                    } else if ("sensor".equals(flag)) {
                        if (child.isCompound()) {
                            TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                        } else {
                            TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                            if (node2 != null) {
                                if (child.getKind() == AssetKind.PROBE && node2.getDataType().equals("FLOAT")) {
                                    list.add(node2);
                                }
                            }
                        }
                    } else if ("control".equals(flag)) {
                        if (child.isCompound()) {
                            TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    if (hasControl(parentC, catlog)) {
                                        list.add(node2);
                                    }
                                }
                            }

                        } else {
                            if ((child.getKind() == AssetKind.CONTROL) && ((Monitor<?>) child).getUnit() != null
                                    && ((Monitor<?>) child).getMonitorKind() == catlog) {
                                TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }

                        }
                    } else {
                        if (child.isCompound()) {
                            TreeNode node2 = printAssetAllTree(child, flag, depth, count, catlog);
                            CompoundAsset<?> parentC = (CompoundAsset<?>) child;
                            Collection<Asset<?>> childrenC = parentC.children();
                            if (childrenC != null && childrenC.size() > 0) {
                                if (node2 != null) {
                                    list.add(node2);
                                }
                            }
                        }
                    }
                    if (list != null && list.size() > 0) {
                        if ("space".equals(flag)) {
                            list.sort(new Comparator<TreeNode>() {
                                @Override
                                public int compare(TreeNode o1, TreeNode o2) {
                                    // TODO Auto-generated method stub
                                    return Integer.compare(((Space) assetStore.findAsset(o1.getId())).getSequence(),
                                            ((Space) assetStore.findAsset(o2.getId())).getSequence());
                                }
                            });
                        }
                        treeNode.setNodes(list);
                    }
                }
            }
        } else {
            treeNode.setId(root.getId());
            treeNode.setKind(root.getKind().ordinal());
            treeNode.setStatus(root.getState().ordinal());
            treeNode.setText(root.getCaption());
            if ((root.getKind() == AssetKind.CONTROL || root.getKind() == AssetKind.PROBE)) {
                treeNode.setUnit(((Monitor<?>) root).getUnit());
            }
            if (root.getKind().equals(AssetKind.PROBE)) {
                Probe probe = (Probe) root;
                DataType dataType = probe.getType().getDataType();
                String name = dataType.getName();
                treeNode.setDataType(name);
            }

        }
        if (root.isMonitor()) {
            Monitor<?> mo = (Monitor<?>) root;
            treeNode.setCatalog((int) mo.getMonitorKind());

            String unit = mo.getUnit();
            treeNode.setUnit(unit);
            Float minValue = mo.getMinValue();
            if (null != minValue)
                treeNode.setMinValue(minValue);
            Float maxValue = mo.getMaxValue();
            if (null != maxValue)
                treeNode.setMaxValue(maxValue);
            Object value = mo.getValue();
            treeNode.setValue(value);
            treeNode.setErrorMsg(mo.getError());
            if (mo.getLastDetectTimeMs() != 0) {
                treeNode.setTime(TimeUtil.date2String(new Date(mo.getLastDetectTimeMs())));
            }
            if (value != null) {
                treeNode.setValueStr(value.toString());
                if (value instanceof Float && ((unit == null) ? true : (!unit.contains("|")))) {
                    // treeNode.setMonitorType("floatcontrol");
                    if (root instanceof Control) {
                        treeNode.setMonitorType("floatcontrol");
                    }

                    String number = new UseCode(codeDictManager).getValue(value, (short) Code.PROBE,
                            mo.getMonitorKind());
                    treeNode.setValueStr(number + (unit == null ? "" : unit));
                } else {
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

        }
        if (root.getKind().equals(AssetKind.DEVICE)) {

            Device de = (Device) root;
            treeNode.setCatalog((int) de.getCatalog());
        }
        return treeNode;
    }

    private int getSpaceIdByAssetId(Asset<?> asset) {
        Asset<?> parent = asset.getParent();
        while (parent.getKind() != AssetKind.SPACE) {
            parent = parent.getParent();
        }
        return parent.getId();
    }

    /*
     * private int getParentCatlogIdByCatlogId(int catlogId) {
     *
     * }
     */
    private Boolean hasDevice(Asset asset) {
        if (asset.isCompound()) {
            CompoundAsset<?> parentC = (CompoundAsset<?>) asset;
            Collection<Asset<?>> childrenC = parentC.children();
            for (Asset<?> chaild : childrenC) {
                if (chaild.getKind() != AssetKind.DEVICE) {
                    return true;
                } else {
                    hasDevice(chaild);
                }
            }
        }
        return false;

    }
    /*
     * private String getLocationStrByAssetId(Asset<?> asset){ List<Asset<?>>
     * assets = new ArrayList<>(); Asset<?> parent = asset.getParent();
     * while(parent.getKind()!=AssetKind.SPACE){ parent = parent.getParent(); }
     * }
     */

    private static final String DFWL_CAMERA_SERVER_IP_STATIC = "${ServerHost}";
    private static final String DFWL_CAMERA_NUMBER_IP_STATIC = "${DeviceNumber}";
    private static final String DFWL_CAMERA_IP_STATIC = "${Host}";
    private static final String DFWL_CAMERA_PORT_STATIC = "${Port}";
    private static final String DFWL_CAMERA_USERNAME_STATIC = "${UserName}";
    private static final String DFWL_CAMERA_PASSWORD_STATIC = "${Password}";
    private static final String DFWL_CAMERA_PLAY_BACK_YARD = "${PlayBackYard}";


    /**
     * 封装资产详细信息,详情的回显
     */
    public AssetVo assetDteail(Asset<?> root, String kind) {
        AssetVo vo = new AssetVo();
        vo.setState(root.getState().toString());
        if (root instanceof Device) {
            Device device = (Device) root;
            vo.setCatalogId((int) device.getCatalog());
            setDeviceToAssetVo(vo, device);
            if (root instanceof WebCamera) {
                WebCamera webCamera = (WebCamera) root;
                String userName = webCamera.getUserName();
                String password = webCamera.getPassword();
                String rtspUrl = webCamera.getRtspUrlPattern();
                String host = webCamera.getHost();
                Integer port = webCamera.getPort();
                vo.setHost(host);
                vo.setPort(port);
                vo.setUsername(userName);
                vo.setPassword(password);
                if (rtspUrl != null) {
                    if (host != null) {
                        rtspUrl = rtspUrl.replace(DFWL_CAMERA_IP_STATIC, host);
                    }
                    rtspUrl = rtspUrl.replace(DFWL_CAMERA_PORT_STATIC, port + "");
                    rtspUrl = rtspUrl.replace(DFWL_CAMERA_USERNAME_STATIC, userName);
                    if (password != null) {
                        rtspUrl = rtspUrl.replace(DFWL_CAMERA_PASSWORD_STATIC, password);
                    }
                    vo.setRtspUrlPattern(rtspUrl);
                }
            }
            if (root instanceof DFWLCamera) {
                String cameraServerIp = vo.getRtspUrlPattern().replace(DFWL_CAMERA_SERVER_IP_STATIC,
                        SocketSecurityService.getServerIp());
                vo.setRtspUrlPattern(cameraServerIp);
            }
            if (root instanceof DFWLCameraYuanqu) {
                DFWLCameraYuanqu huifangCam = (DFWLCameraYuanqu) root;
                String huifangshibiema = huifangCam.getHuifangshibiema();
                if (null != huifangshibiema && "" != huifangshibiema) {
                    String huifangCamUrl = huifangCam.getHuifangurl().replace(DFWL_CAMERA_SERVER_IP_STATIC,
                            SocketSecurityService.getServerIp());
                    huifangCamUrl = huifangCamUrl.replace(DFWL_CAMERA_PLAY_BACK_YARD, huifangshibiema);
                    vo.setHuifangurl(huifangCamUrl);
                }
            }
            if (root instanceof DFWLYUSHICamera) {
                DFWLYUSHICamera dfwlyushiCamera = (DFWLYUSHICamera) root;
                String cameraServerIp = vo.getRtspUrlPattern().replace(DFWL_CAMERA_NUMBER_IP_STATIC,
                        dfwlyushiCamera.getDeviceNumber());
                vo.setRtspUrlPattern(cameraServerIp);
            }
            if (root instanceof YCDahuaCamera) {
                YCDahuaCamera ycdahuaCamera = (YCDahuaCamera) root;
                vo.setTypeName(ycdahuaCamera.getType().getName());
            }
            if (root instanceof YCHaiKangCamera) {
                YCHaiKangCamera ychaikangCamera = (YCHaiKangCamera) root;
                vo.setTypeName(ychaikangCamera.getType().getName());
            }
            if (root instanceof CiZingCamera) {
                CiZingCamera cizingCamera = (CiZingCamera) root;
                String serverHost = vo.getRtspUrlPattern().replace(DFWL_CAMERA_SERVER_IP_STATIC,
                        vo.getHost());
                String cameraDeviceNmb = serverHost.replace(DFWL_CAMERA_NUMBER_IP_STATIC,
                        cizingCamera.getDeviceNumber());
                vo.setRtspUrlPattern(cameraDeviceNmb);
                vo.setTypeName(cizingCamera.getType().getName());
            }
        }
        /*
         * if (root instanceof Control) { Control control = (Control) root;
         * TimeSpan interval = control.getDetectInterval(); if (interval !=
         * null) { long numeral = interval.getNumeral(); TimeUnit unit =
         * interval.getUnit(); String time = ""; if
         * (unit.toString().equals(TimeUnitAbbr.MINUTES.toString())) { time =
         * "分"; } else if
         * (unit.toString().equals(TimeUnitAbbr.SECONDS.toString())) { time =
         * "秒"; } else { time = ""; }
         * vo.setTime_interval(String.valueOf(numeral)); vo.setUnit(time);
         * String transform = control.getTransform(); vo.setTransform(transform
         * == null ? "" : transform); String warnCond = control.getWarnCond();
         * vo.setWarn_cond(warnCond == null ? "" : warnCond);
         * vo.setRefresh_delay(control.getRefreshDelay()); } MonitorService
         * source = control.getSource(); if (source != null) { String str =
         * source.getCaption(); vo.setServiceCaption(str == null ? "" : str);
         * vo.setSource(source.getId()); } }
         */
        if (root instanceof Monitor<?>) {
            Monitor<?> pro = (Monitor<?>) root;
            vo.setCatalogId((int) pro.getMonitorKind());
            //vo.setState(state);
            TimeSpan interval = pro.getDetectInterval();
            if (interval != null) {
                long numeral = interval.getNumeral();
                TimeUnit unit = interval.getUnit();
                String time = "";
                if (unit.toString().equals(TimeUnit.MINUTES.toString())) {
                    time = "分";
                } else if (unit.toString().equals(TimeUnit.SECONDS.toString())) {
                    time = "秒";
                } else {
                    time = "";
                }
                vo.setTime_interval(String.valueOf(numeral));
                vo.setUnit(time);
            }
            MonitorService source = pro.getSource();
            if (source != null) {
                String str = source.getCaption();
                vo.setServiceCaption(str == null ? "" : str);
                vo.setSource(source.getId());
            }
            vo.setTypeName(pro.getType().getName());
            String transform = pro.getTransform();
            vo.setTransform(transform == null ? "" : transform);
            String warnCond = pro.getWarnCond();
            vo.setWarn_cond(warnCond == null ? "" : warnCond);
            vo.setRefresh_delay(pro.getRefreshDelay());
            Object value = pro.getValue();
            String unit = pro.getUnit();
            if (value != null) {
                if (value instanceof Float && ((unit == null) ? true : (!unit.contains("|")))) {
                    float valu = (float) value;
                    String formatDoubleTwo = new UseCode(codeDictManager).getValue(valu, (short) Code.PROBE,
                            pro.getMonitorKind());
                    vo.setValue(value.toString());
                    vo.setValueStr(formatDoubleTwo + (pro.getUnit() == null ? "" : pro.getUnit()));
                } else {
                    vo.setValue(value.toString());
                    vo.setValueStr(value.toString());
                    UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {

                        @Override
                        public void parseResult(String result) {
                            // TODO Auto-generated method stub
                            vo.setValueStr(result);
                        }

                    });
                }
            }
        }
        vo.setSpaceId(getSpaceIdByAssetId(root));
        AssetState state = root.getState();

        if (state != null) {
            if (state.equals(AssetState.ERROR)) {
                vo.setError(root.getError());
            }
        }
        vo.setId(root.getId());
        String desc = root.getDesc();
        vo.setMemo(desc == null ? "" : desc);
        vo.setKind(kind == null ? "" : kind);
        setKindStr(root, vo);
        String assetCaption = root.getCaption();
        vo.setCaption(assetCaption == null ? "" : assetCaption);
        String assetName = root.getName();
        vo.setName(assetName == null ? "" : assetName);
        vo.setFullName(assetStore.getAssetFullName(root, ">", "name"));
        AssetType type = root.getType();
        Collection<AssetTypeProperty> properties = type.properties();
        List<AssetProperty> list = new ArrayList<AssetProperty>();
        for (AssetTypeProperty assetTypeProperty : properties) {

            AssetProperty property = new AssetProperty();
            String name = assetTypeProperty.getName();

            property.setName(name);
            String caption = assetTypeProperty.getCaption();
            property.setCaption(caption);
            Object pvalue = root.getPropValue(name);
            if (pvalue != null) {
                property.setValue(pvalue.toString());
            }

            list.add(property);
        }
        vo.setValueList(list);
        return vo;
    }

    // 封装回显的设备信息
    private void setDeviceToAssetVo(AssetVo assetVo, Device device) {
        String vendor = device.getVendor();
        LocalDateTime purchaseDate = device.getPurchaseDate();
        LocalDate warrantyDate = device.getWarrantyDate();
        assetVo.setPurchase_date(
                purchaseDate == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(purchaseDate));
        assetVo.setWarranty_date(
                warrantyDate == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(warrantyDate));
        assetVo.setVendor(vendor == null ? "" : vendor);
        /* 封装其他属性 */
        AssetVo detail = assetMapper.findDeviceDetail(device.getId());
        if (detail != null) {
            setDeviceDetail(detail);
            String specification = detail.getSpecification();
            assetVo.setSpecification(specification == null ? "" : specification);
            String models = detail.getModels();
            assetVo.setModels(models == null ? "" : models);
            String increse_way = detail.getIncrese_way();
            assetVo.setIncrese_way(increse_way == null ? "" : increse_way);
            String international_code = detail.getInternational_code();
            assetVo.setInternational_code(international_code == null ? "" : international_code);
            String detail_config = detail.getDetail_config();
            assetVo.setDetail_config(detail_config == null ? "" : detail_config);
            String production_date = detail.getProduction_date();
            assetVo.setProduction_date(production_date == null ? "" : production_date);
            Department using_department = detail.getUsing_department();
            assetVo.setUsing_department(using_department == null ? new Department() : using_department);
            String using_state = detail.getUsing_state();
            assetVo.setUsing_state(using_state == null ? "" : using_state);
            String device_type = detail.getDevice_type();
            assetVo.setDevice_type(device_type == null ? "" : device_type);
            Integer spaceId = detail.getLocation();
            AssetVo vo = new AssetVo();
            vo.setId(spaceId);
            vo.setSiteId((int) application.getSiteId());
            List<AssetVo> list = assetMapper.findSpace(vo);
            if (list != null && list.size() > 0) {
                String caption = list.get(0).getCaption();
                assetVo.setSpaceCaption(caption == null ? "" : caption);
            }
            User keepers = detail.getKeepers();
            assetVo.setKeepers(keepers == null ? new User() : keepers);
            Integer quantity = detail.getQuantity();
            assetVo.setQuantity(quantity == null ? 0 : quantity);
            String deviceUnit = detail.getDeviceUnit();
            assetVo.setDeviceUnit(deviceUnit == null ? "" : deviceUnit);
            Double price = detail.getPrice();
            assetVo.setPrice(price == null ? 0.0 : price);
            Double amount = detail.getAmount();
            assetVo.setAmount(amount == null ? 0.0 : price);
            String enabing_date = detail.getEnabing_date();
            assetVo.setEnabing_date(enabing_date == null ? "" : enabing_date);
            Integer maintenance_interval = detail.getMaintenance_interval();
            assetVo.setMaintenance_interval(maintenance_interval == null ? 0 : maintenance_interval);
            Integer original_value = detail.getOriginal_value();
            assetVo.setOriginal_value(original_value == null ? 0 : original_value);
            Integer use_year = detail.getUse_year();
            assetVo.setUse_year(use_year == null ? 0 : use_year);
            Double salvage = detail.getSalvage();
            assetVo.setSalvage(salvage == null ? 0.0 : salvage);
            Double salvage_value = detail.getSalvage_value();
            assetVo.setSalvage_value(salvage_value == null ? 0.0 : salvage_value);
            String depreciation_method = detail.getDepreciation_method();
            assetVo.setDepreciation_method(depreciation_method == null ? "" : depreciation_method);
            Integer scrap = detail.getScrap();
            assetVo.setScrap(scrap == null ? 1 : scrap);
            Integer is_using = detail.getIs_using();
            assetVo.setIs_using(is_using == null ? 0 : is_using);
        }
    }

    /**
     * 查询此类型的服务列表 方法名：serviceList() 参数：此类型资源的kind 返回值：List<DeptJson>
     */
    @Override
    public List<DeptJson> serviceList(AssetStore store, AssetType type) {
        // 创建返回值对象
        List<DeptJson> jsonList = new ArrayList<DeptJson>();
        if (type != null && type instanceof MonitorType) {
            MonitorType monitorType = (MonitorType) type;
            ServiceType source = monitorType.getSource();
            if (source != null) {
                // 根据获得的类型查该类型的服务
                Collection<Asset<ServiceType>> assets = store.getAssets(source);
                for (Asset<?> service : assets) {
                    DeptJson json = new DeptJson();
                    int id = service.getId();
                    String caption = service.getCaption();
                    json.setId(id);
                    json.setText(caption);
                    jsonList.add(json);
                }

            }
        }
        return jsonList;
    }

    /* 修改资产 assetVo为页面传过来的值 */
    @Override
    public void assetEdit(AssetVo assetVo) throws AssetException {
        Asset<?> root = assetStore.findAsset(assetVo.getId());
        if (root == null) {
            root = findAssetByID(assetVo.getId(), assetVo.getKind());
        }
        // 资产属性
        List<AssetProperty> valueList = assetVo.getValueList();
        if (valueList != null) {
            for (AssetProperty property : valueList) {
                AssetType type = root.getType();
                AssetTypeProperty assetTypeProperty = type.getProperty(property.name);
                root.setPropValue(assetTypeProperty, property.getValue());
                property.setAsset(root.getId());
                assetMapper.editAssetPropertyById(property);
            }
        }
        root.setCaption(assetVo.getCaption());
        root.setName(assetVo.getName());
        root.setDesc(assetVo.getMemo());
        root.setParentId(assetVo.getParentId());
        Asset<?> asset = setAsset(assetVo, root);
        editAsset(asset);
    }

    /**
     * 封装单个Asset
     *
     * @param assetVo
     * @param root
     * @return
     */
    @Override
    public Asset<?> setAsset(AssetVo assetVo, Asset<?> root) {
        // 监测器
        String time_interval = assetVo.getTime_interval();
        String warn_cond = assetVo.getWarn_cond();
        Integer source = assetVo.getSource();
        Integer catalogId = assetVo.getCatalogId();
        String minValue = assetVo.getMinValue();
        String maxValue = assetVo.getMaxValue();
        String save_interval = assetVo.getSave_interval();
        if (StringUtils.isBlank(save_interval)) {
            save_interval = "60m";
        }
        // 监测器的值
        if (root instanceof Space) {
            Space space = (Space) root;
            return space;
        } else if (root instanceof Device) {
            Device device = (Device) root;
            if (catalogId != null) {
                int catalog = catalogId;
                device.setCatalog((short) catalog);
            }
            setAssetVoToDevice(assetVo, device);
            return device;
            // 修改控制器的特有属性值
        } else if (root instanceof Control) {
            Control control = (Control) root;
            if (catalogId != null) {
                int catalog = catalogId;
                control.setCatalog((short) catalog);
            }

            control.setTransform(assetVo.getTransform());
            control.setWarnCond(assetVo.getWarn_cond());
            if (assetVo.getRefresh_delay() != null)
                control.setRefreshDelay(assetVo.getRefresh_delay());
            String detact_interval = assetVo.getDetact_interval();
            if (!"".equals(detact_interval))
                control.setDetectInterval(TimeSpan.parseValue(detact_interval));
            // 连接服务
            if (source != null) {// service有值说明是修改
                Asset<?> findAsset = assetStore.findAsset(source);
                if (root instanceof MonitorService) {
                    MonitorService serve = (MonitorService) findAsset;
                    control.setSource(serve);
                }
            }
            return control;
        } else if (root instanceof MonitorService) {
            MonitorService service = (MonitorService) root;
            return service;
            // 修改监测器的特有属性值
        } else if (root instanceof Probe) {
            Probe probe = (Probe) root;
            if (catalogId != null) {
                int catalog = catalogId;
                probe.setCatalog((short) catalog);
            }
            String transform = assetVo.getTransform();
            if (transform != null && !transform.equals(""))
                probe.setTransform(transform);
            if (warn_cond != null && !warn_cond.equals(""))
                probe.setWarnCond(warn_cond);
            if (assetVo.getRefresh_delay() != null)
                probe.setRefreshDelay(assetVo.getRefresh_delay());
            // String detact_interval = assetVo.getDetact_interval();
            if (!"".equals(time_interval))
                probe.setDetectInterval(TimeSpan.parseValue(time_interval));
            probe.setSavingInterval(TimeSpan.parseValue(save_interval));
            if (source != null) {
                Asset<?> findAsset = assetStore.findAsset(source);
                if (findAsset != null) {
                    MonitorService serve = (MonitorService) findAsset;
                    probe.setSource(serve);
                }
            }
            if (null != minValue && !"".equals(minValue))
                probe.setMinValue(Float.parseFloat(minValue));
            if (null != minValue && !"".equals(maxValue))
                probe.setMaxValue(Float.parseFloat(maxValue));
            return probe;
        }
        return null;
    }

    // 封装设备，将前端传过来的数据，封装到Device中
    private void setAssetVoToDevice(AssetVo assetVo, Device device) {
        // 设备
        String purchase_date = assetVo.getPurchase_date();
        String warranty_date = assetVo.getWarranty_date();
        String vendor = assetVo.getVendor();
        try {
            if (purchase_date != null && !purchase_date.equals("")) {// date类型
                device.setPurchaseDate(TimeUtil.timestampStrToLocalDateTime(purchase_date));
            }
            if (warranty_date != null && !warranty_date.equals("")) {

                device.setWarrantyDate(LocalDate.parse(warranty_date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (vendor != null) {
            device.setVendor(vendor);
        }
        // 封装其他属性
        device.setSpecification(assetVo.getSpecification());
        device.setModels(assetVo.getModels());
        device.setIncrese_way(assetVo.getIncrese_way());
        device.setInternational_code(assetVo.getInternational_code());
        device.setDetail_config(assetVo.getDetail_config());
        String production_date = assetVo.getProduction_date();
        if (production_date != null && !production_date.equals("")) {
            LocalDate parse = LocalDate.parse(assetVo.getProduction_date());
            device.setProduction_date(parse);
        }
        Department usingDepartment = assetVo.getUsing_department();
        if (null != usingDepartment) {
            device.setUsing_department(assetVo.getUsing_department().getId());
        }
        device.setUsing_state(assetVo.getUsing_state());
        device.setDevice_type(assetVo.getDevice_type());
        device.setLocation(assetVo.getParentId());
        if (assetVo.getKeepers() != null) {
            device.setKeepers(assetVo.getKeepers().getId());
        }
        device.setQuantity(assetVo.getQuantity());
        device.setDeviceUnit(assetVo.getDeviceUnit());
        device.setPrice(assetVo.getPrice());
        device.setAmount(assetVo.getAmount());
        String enabing_date = assetVo.getEnabing_date();
        if (enabing_date != null && !"".equals(enabing_date)) {
            device.setEnabing_date(LocalDate.parse(enabing_date));
        }
        device.setMaintenance_interval(assetVo.getMaintenance_interval());
        device.setOriginal_value(assetVo.getOriginal_value());
        device.setUse_year(assetVo.getUse_year());
        device.setSalvage(assetVo.getSalvage());
        device.setSalvage_value(assetVo.getSalvage_value());
        device.setDepreciation_method(assetVo.getDepreciation_method());
        device.setIs_using(assetVo.getIs_using());
        device.setScrap(assetVo.getScrap());
    }

    /**
     * 封装parentId.添加到内存的的map中，并保存到数据库
     *
     * @throws AssetException
     */
    @Override
    public void addParentId(AssetStore store, List<Asset<?>> alist) throws AssetException {
        if (alist != null && alist.size() > 0) {
            AssetInstanceLoader loader = new DBAssetInstanceLoader(store, sqlSession, null);
            for (Asset<?> asset : alist) {
                if (asset.getParentId() == 0) {// 如果是0,以表格优先
                    String parentName = asset.getParentName();
                    if (parentName != null) {
                        Asset<?> findAsset = store.findAsset(parentName, "\\.");
                        if (findAsset != null) {
                            // System.out.println(findAsset.getCaption());
                            asset.setParentId(findAsset.getId());
                        } else {
                            System.out.println("内存tree中,没有该对象");
                        }
                    }
                }
                loader.preloadAsset(asset);
            }
            // 放入内存后，在内存中，
            loader.buildAssetTree();
            // 拿到asset moniter 的service 串，找service
            addServiceToList(store, alist);
            // 获取内存中的父节点
            // 放入内存后，在内存中，
            for (Asset<?> asset : alist) {
                addAssertProperty(store, asset);
            }
        }
    }

    private void addServiceToList(AssetStore store, List<Asset<?>> alist) {
        if (alist != null && alist.size() > 0) {
            for (Asset<?> asset1 : alist) {
                if (asset1 instanceof Probe) {
                    Probe pro = (Probe) asset1;
                    String sources = pro.getSources();
                    if (sources != null) {
                        Asset<?> findAsset = store.findAsset(sources, "\\.");
                        if (findAsset != null) {
                            System.out.println(findAsset.getCaption());
                        } else {
                            System.out.println("probe链接的服务，内存中不存在");
                        }
                        if (findAsset instanceof MonitorService) {
                            MonitorService sv = (MonitorService) findAsset;
                            pro.setSource(sv);
                        }
                    }
                } else if (asset1 instanceof Control) {
                    Control cont = (Control) asset1;
                    String sources = cont.getSources();
                    if (sources != null) {
                        Asset<?> findAsset = store.findAsset(sources, "\\.");
                        if (findAsset != null) {
                            System.out.println("=========" + findAsset.getCaption() + "==============");
                        } else {
                            System.out.println("control链接的服务，内存中不存在");
                        }
                        if (findAsset instanceof MonitorService) {
                            MonitorService sv = (MonitorService) findAsset;
                            cont.setSource(sv);
                        }
                    }
                }
            }
        }
    }

    public void addAssertProperty(AssetStore store, Asset<?> asset) throws AssetException {
        Collection<AssetTypeProperty> properties = asset.getType().properties();
        for (AssetTypeProperty assetTypeProperty : properties) {
            AssetProperty assetProperty = new AssetProperty();
            assetProperty.setAsset(asset.getId());
            String name = assetTypeProperty.getName();
            assetProperty.setName(name);
            Object propValue = asset.getPropValue(name);
            if (propValue != null) {
                assetProperty.setValue(propValue.toString());
                assetMapper.addAssetProperty(assetProperty);
            }
        }
        addAssetToDB(asset);
    }

    // 将asset存到数据库
    @Override
    public void editAsset(Asset<?> asset) {
        // 将asset存到数据库
        if (asset instanceof Space) {
            assetMapper.editSpaceById(asset);
        } else if (asset instanceof Device) {
            assetMapper.editDeviceById(asset);
            AssetVo vo = assetMapper.findDeviceDetail(asset.getId());
            if (vo != null) {
                assetMapper.editDeviceDetailById(asset);
            } else {
                assetMapper.addDeviceDetail(asset);
            }
        } else if (asset instanceof Probe) {
            assetMapper.editProbeById(asset);
        } else if (asset instanceof MonitorService) {
            assetMapper.editServiceById(asset);
        } else if (asset instanceof Control) {
            assetMapper.editControlById(asset);
        }
    }

    // 将asset存到数据库
    public void addAssetToDB(Asset<?> asset) {
        // 将asset存到数据库
        if (asset instanceof Space) {
            assetMapper.addSpace(asset);
        } else if (asset instanceof Device) {
            assetMapper.addDevice(asset);
            assetMapper.addDeviceDetail(asset);
        } else if (asset instanceof Probe) {
            assetMapper.addProbe(asset);
        } else if (asset instanceof MonitorService) {
            assetMapper.addService(asset);
        } else if (asset instanceof Control) {
            assetMapper.addnewControl(asset);
        }
    }

    /**
     * 封装属性，并保存到数据库
     *
     * @throws AssetException
     */
    @Override
    public void addAsset(AssetStore store, Asset<?> asset, List<AssetProperty> valueList) throws AssetException {
        if (valueList != null) {
            for (AssetProperty assetProperty : valueList) {
                // 设置属性到asset中
                AssetTypeProperty property = asset.getType().getProperty(assetProperty.getName());
                if (assetProperty.getValue() != null && assetProperty.getValue().equals("")) {// 属性不添加，就不存
                    // 设置默认值为0；
                    assetProperty.setValue("0");
                }
                if (property == null) {
                    System.out.println(asset.getName() + "" + assetProperty.getName());
                }
                setPropertyValue(asset, assetProperty.getValue(), property);
                assetProperty.setAsset(asset.getId());
                assetMapper.addAssetProperty(assetProperty);
            }
        }
        // 新增asset到内存，从新构建内存tree
        AssetInstanceLoader loader = new DBAssetInstanceLoader(store, sqlSession, null);
        Asset<?> parent = store.findAsset(asset.getParentId() == null ? 0 : asset.getParentId());
        // 将父类放到集合
        loader.preloadAsset(parent);
        loader.preloadAsset(asset);
        loader.buildAssetTree();
        // System.out.println(asset.getName() + " parent =====" +
        // parent.getName());
        addAssetToDB(asset);
        // store.dump();
    }

    // 设置默认值的方法
    @Override
    public void setPropertyValue(Asset<?> asset, Object value, AssetTypeProperty property) {
        //首先判断AssetTypeProperty对象是否为空 得问问他们是否能添加这个判空操作
        if (null != property) {
            if (value != null && !value.equals("")) {
                asset.setPropValue(property, value.toString());
            } else if (property.getDefault() != null) {
                asset.setPropValue(property, property.getDefault());
            }
        }
    }

    /*
     * 搜索资产列表
     */
    @Override
    public EasyUIJson findAssetList(AssetStore assetStore, AssetVo assetVo, Integer rows, Integer page) {
        // TODO Auto-generated method stub
        // 设置分页参数
        PageHelper.startPage(page, rows);
        // 调用接口查询数据
        String kind = assetVo.getKind();
        short siteId = application.getSiteId();
        assetVo.setSiteId((int) siteId);
        List<AssetVo> findAsset = null;
        if (kind != null) {
            if (kind.equals("SPACE")) {
                findAsset = assetMapper.findSpace(assetVo);
            } else if (kind.equals("SERVICE")) {
                findAsset = assetMapper.findService(assetVo);
            } else if (kind.equals("PROBE")) {
                findAsset = assetMapper.findProbe(assetVo);
            } else if (kind.equals("CONTROL")) {
                findAsset = assetMapper.findControl(assetVo);
            } else if (kind.equals("DEVICE")) {
                findAsset = assetMapper.findDevice(assetVo);
            }
        }
        if (findAsset != null) {
            for (AssetVo assetVo2 : findAsset) {
                assetVo2.setKind(kind);
                if (kind.equals("DEVICE")) {
                    setDeviceDetail(assetVo2);
                    Asset<?> asset = assetStore.findAsset(assetVo2.getId());
                    assetVo2.setFullName(assetStore.getAssetFullName(asset, ">", "name"));
                }
                Integer parentId = assetVo.getParentId();
                if (parentId != null) {
                    Asset<?> asset2 = assetStore.findAsset(assetVo.getParentId());
                    if (asset2 != null) {
                        assetVo2.setParentCaption(asset2.getCaption());
                    }
                }
            }
        }
        // 创建PageInfo对象，获取分页信息
        PageInfo<AssetVo> pageInfo = new PageInfo<AssetVo>(findAsset);
        // 创建EasyUIJson对象，封装查询结果
        EasyUIJson uiJson = new EasyUIJson();
        // 设置查询总记录数
        uiJson.setTotal(pageInfo.getTotal());
        // 设置查询记录
        uiJson.setRows(findAsset);
        return uiJson;
    }

    @Override
    public EasyUIJson findDeviceList(AssetStore assetStore, DeviceCatalogSpace dcs, Integer rows, Integer page) {
        PageHelper.startPage(page, rows);
        // 调用接口查询数据
        AssetVo assetVo = dcs.getAssetVo();
        String kind = assetVo.getKind();
        short siteId = application.getSiteId();
        assetVo.setSiteId((int) siteId);
        List<AssetVo> findAsset = null;
        if (kind != null && kind.equals("DEVICE")) {
            dcs.setAssetVo(assetVo);
            findAsset = assetMapper.findDeviceByCatalogsAndSpaces(dcs);
        }
        if (findAsset != null) {
            for (AssetVo assetVo2 : findAsset) {
                assetVo2.setKind(kind);
                if (kind.equals("DEVICE")) {
                    setDeviceDetail(assetVo2);
                    Asset<?> asset = assetStore.findAsset(assetVo2.getId());
                    assetVo2.setFullName(assetStore.getAssetFullName(asset, ">", "name"));
                }
                Integer parentId = assetVo.getParentId();
                if (parentId != null) {
                    Asset<?> asset2 = assetStore.findAsset(assetVo.getParentId());
                    if (asset2 != null) {
                        assetVo2.setParentCaption(asset2.getCaption());
                    }
                }
            }
        }
        // 创建PageInfo对象，获取分页信息
        PageInfo<AssetVo> pageInfo = new PageInfo<AssetVo>(findAsset);
        // 创建EasyUIJson对象，封装查询结果
        EasyUIJson uiJson = new EasyUIJson();
        // 设置查询总记录数
        uiJson.setTotal(pageInfo.getTotal());
        // 设置查询记录
        uiJson.setRows(findAsset);
        return uiJson;
    }


    private void setDeviceDetail(AssetVo assetVo2) {
        Integer departId = assetVo2.getDepartId();
        Department department = deptMapper.queryDeptById(departId);
        assetVo2.setUsing_department(department);
        Integer userId = assetVo2.getUserId();
        User user = userMapper.queryUserById(userId);
        assetVo2.setKeepers(user);
        Integer spaceId = assetVo2.getLocation();
        if (spaceId != null) {
            AssetVo vo = new AssetVo();
            vo.setId(spaceId);
            vo.setSiteId((int) application.getSiteId());
            List<AssetVo> list = assetMapper.findSpace(vo);
            if (list != null && list.size() > 0) {
                String caption = list.get(0).getCaption();
                assetVo2.setSpaceCaption(caption == null ? "" : caption);
            }
        }
    }

    /*
     * 递归删除Asset节点，以及子节点
     */
    @Override
    public void deleteAsset(Asset<?> asset) {
        asset.setRemoved(1);
        if (asset.isCompound()) {
            CompoundAsset<?> parent = (CompoundAsset<?>) asset;
            Collection<Asset<?>> childrens = parent.children();
            for (Asset<?> asset2 : childrens) {
                deleteAsset(asset2);
            }
            // 从数据库中做修改
        }
        deletedAssetToDB(asset);
    }

    /* 删除资产链接数据库 */
    public void deletedAssetToDB(Asset<?> asset) {
        if (asset instanceof Probe) {
            Probe probe = (Probe) asset;
            assetMapper.editProbeById(probe);
        } else if (asset instanceof Space) {
            Space space = (Space) asset;
            assetMapper.editSpaceById(space);
        } else if (asset instanceof MonitorService) {
            MonitorService server = (MonitorService) asset;
            assetMapper.editServiceById(server);
        } else if (asset instanceof Control) {
            Control control = (Control) asset;
            assetMapper.editControlById(control);
        } else if (asset instanceof Device) {
            Device device = (Device) asset;
            assetMapper.editDeviceById(device);
        }
    }

    /**
     * 根据id查设备信息
     */
    @Override
    public AssetVo findDevice(AssetVo vo) {
        short siteId = application.getSiteId();
        vo.setSiteId((int) siteId);
        List<AssetVo> findDevice = assetMapper.findDevice(vo);
        if (findDevice != null && findDevice.size() == 1) {
            return findDevice.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询是否生成二维码设备信息
     */
    @Override
    public Map<String, Object> queryQrcodeListByPage(AssetVo assetVo, Integer page, Integer rows) {
        Map<String, Object> map = new HashMap<String, Object>();
        short siteId = application.getSiteId();
        assetVo.setSiteId((int) siteId);
        List<AssetVo> deviceList = assetMapper.findDevice(assetVo);
        List<AssetVo> finish = new ArrayList<AssetVo>();
        List<AssetVo> finished = new ArrayList<AssetVo>();
        if (deviceList != null && deviceList.size() > 0) {
            // System.out.println("一共有" + deviceList.size() + "个设备");
            for (AssetVo device : deviceList) {
                String qrcode = device.getQrcode();
                if (qrcode != null && !qrcode.equals("")) {
                    finished.add(device);
                } else {
                    finish.add(device);
                }
            }
        }
        if (finished != null) {
            System.out.println("已完成打印的" + finished.size());
        }
        if (finish != null) {
            System.out.println("没有完成打印的" + finish.size());
        }
        System.out.println("第" + page + "页");
        System.out.println("一页" + rows + "条");
        EasyUIJson finishJson = getPageList(finish, page, rows);
        EasyUIJson finnishedJson = getPageList(finished, page, rows);
        map.put("finish", finishJson);
        map.put("finished", finnishedJson);
        return map;
    }

    private EasyUIJson getPageList(List<AssetVo> deviceList, Integer page, Integer rows) {
        List<AssetVo> list = new ArrayList<AssetVo>();
        int i = (page - 1) * rows;
        int j = 0;
        for (AssetVo vo : deviceList) {
            if (j >= i) {
                if (j >= i + rows) {
                    break;
                }
                list.add(vo);
            }
            j++;
        }
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal((long) deviceList.size());
        uiJson.setRows(list);
        return uiJson;
    }

    /*
     * 生成二维码
     */
    @Override
    public void addDeviceQrcode(String id, String savePath, String realPath) throws Exception {
        // 生成的服务器路径
        String name = "qrcode/" + LocalDate.now().format(DateTimeFormatter.ofPattern("YYYYMMdd")) + id + ".png";
        String path = realPath + name;
        System.out.println(path);
        File file = new File(path);
        QRCodeUtils.qrCodeEncode(id, file);
        Device device = assetMapper.findDeviceById(Integer.parseInt(id));
        device.setQrcode(savePath + name);
        // System.out.println(savePath + name);
        assetMapper.editDeviceById(device);
    }

    /**
     * 查询内存中不存在的节点
     *
     * @throws AssetException
     */
    @Override
    public Asset<?> findAssetByID(Integer id, String kind) throws AssetException {
        AssetVo assetVo = new AssetVo();
        assetVo.setSiteId((int) application.getSiteId());
        assetVo.setId(id);
        assetVo.setKind(kind);
        if (AssetKind.PROBE.toString().equals(kind)) {
            List<AssetVo> findProbe = assetMapper.findProbe(assetVo);
            if (findProbe != null && findProbe.size() == 1) {
                AssetVo vo = findProbe.get(0);
                vo.setSiteId((int) application.getSiteId());
                vo.setKind(kind);
                Asset<?> createAsset = createAssetByKind(vo);
                return createAsset;
            } else {
                return null;
            }
        } else if (AssetKind.DEVICE.toString().equals(kind)) {
            return assetMapper.findDeviceById(id);

        } else if (AssetKind.SERVICE.toString().equals(kind)) {
            return assetMapper.findServiceById(id);
        } else if (AssetKind.CONTROL.toString().equals(kind)) {
            List<AssetVo> findControl = assetMapper.findControl(assetVo);
            if (findControl != null && findControl.size() == 1) {
                Asset<?> createAsset = createAssetByKind(assetVo);
                return createAsset;
            } else {
                return null;
            }
        } else if (AssetKind.SPACE.toString().equals(kind)) {
            return getSpace(assetVo);
        } else {
            return null;
        }
    }

    // 根据assetVo 创建asset实体
    private Asset<?> createAssetByKind(AssetVo assetVo) throws AssetException {
        if (assetVo == null || assetVo.getKind() == null) {
            return null;
        }
        AssetKind assetKind = AssetKind.valueOf(assetVo.getKind());
        AssetTypeManager<AssetType> assetTypes = assetStore.getAssetTypes(assetKind);
        final String name = assetVo.getTypeName();
        if (name == null) {
            throw new RuntimeException();
        }
        AssetType atType = assetTypes.find(name);
        // 获取到输入类型的实例，创建asset实例，封装参数
        Asset<?> asset = assetStore.createAsset(atType, assetVo.getId(), assetVo.getName());
        if (assetVo.getCaption() != null) {
            asset.setCaption(assetVo.getCaption());
        }
        return asset;
    }

    private Space getSpace(AssetVo assetVo) {
        short siteId = application.getSiteId();
        assetVo.setSiteId((int) siteId);
        List<AssetVo> findSpace = assetMapper.findSpace(assetVo);
        if (findSpace != null && findSpace.size() > 0) {
            AssetVo vo = findSpace.get(0);
            Space space = new Space();
            SpaceType type = assetStore.getSpaceTypes().find("3DSpace");
            space.setType(type);
            space.setId(vo.getId());
            space.setCaption(vo.getCaption());
            space.setName(vo.getName());
            space.setParentId(vo.getParentId());
            space.setDesc(vo.getMemo());
            return space;
        } else {
            return null;
        }
    }

    // 查询设备打印的数据
    @Override
    public List<AssetVo> findDeviceByIds(List<Integer> ids) {
        List<AssetVo> findDeviceByIds = assetMapper.findDeviceByIds(ids);
        return findDeviceByIds;
    }

    @Override
    public List<AssetVo> findAllDevice() {

        AssetVo vo = new AssetVo();
        short siteId = application.getSiteId();
        vo.setSiteId((int) siteId);
        List<AssetVo> findDevice = assetMapper.findDevice(vo);
        /*
         * ArrayList<Device> dList = new ArrayList<Device>(); if (findDevice !=
         * null) { for (AssetVo assetVo : findDevice) { Device device = new
         * Device(); device.setId(assetVo.getId());
         * device.setCaption(assetVo.getCaption());
         * device.setVendor(assetVo.getVendor()); String enabing_date =
         * assetVo.getEnabing_date(); if (enabing_date != null &&
         * !enabing_date.equals("")) {
         * device.setEnabing_date(LocalDate.parse(enabing_date,
         * DateTimeFormatter.ofPattern("yyyy-MM-dd"))); } String production_date
         * = assetVo.getProduction_date(); if (production_date != null &&
         * !production_date.equals("")) {
         * device.setEnabing_date(LocalDate.parse(enabing_date,
         * DateTimeFormatter.ofPattern("yyyy-MM-dd"))); } dList.add(device); } }
         */
        return findDevice;
    }

    /**
     * 获取空间树节点数据
     */
    @Override
    public List<EasyUITreeNode> querySpaceTreeData(Integer parent) {
        // 创建要封装返回值
        AssetVo assetVo = new AssetVo();
        assetVo.setParentId(parent);
        // assetVo.setSiteId((int) application.getSiteId());
        List<EasyUITreeNode> treeList = new ArrayList<EasyUITreeNode>();
        // 获取查询数据
        List<AssetVo> treeData = assetMapper.findSpace(assetVo);
        Collections.sort(treeData);
        // 遍历数据封装节点
        if (treeData != null && treeData.size() > 0) {
            for (AssetVo asset : treeData) {

                EasyUITreeNode treeNode = new EasyUITreeNode();
                // 封装id
                treeNode.setId(asset.getId());
                // 封装text,节点的名称
                treeNode.setText(asset.getCaption());
                // 封装state，是否是叶子节点
                treeNode.setState("closed");
                treeList.add(treeNode);
            }
        }
        return treeList;
    }

    // 获取分类的树
    @Override
    public List<TreeNode> getAllSpaceTree(int id) {
        AssetVo assetVo = new AssetVo();
        assetVo.setParentId(id);
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<AssetVo> treeData = assetMapper.findSpace(assetVo);
        Collections.sort(treeData);
        if (treeData != null && treeData.size() > 0) {
            for (AssetVo asset : treeData) {
                TreeNode node = printSpaceTree(asset);
                list.add(node);
            }
        }
        return list;
    }

    // 获取树的递归方法
    public TreeNode printSpaceTree(AssetVo asset) {
        TreeNode treeNode = new TreeNode();
        int id = asset.getId();
        treeNode.setId(id);
        treeNode.setText(asset.getCaption());
        AssetVo vo = new AssetVo();
        vo.setParentId(id);
        vo.setSiteId((int) application.getSiteId());
        List<AssetVo> treeData = assetMapper.findSpace(vo);
        Collections.sort(treeData);
        if (treeData != null && treeData.size() > 0) {
            for (AssetVo child : treeData) {
                TreeNode childNode = printSpaceTree(child);
                if (childNode != null) {
                    treeNode.addChild(childNode);
                }
            }
        }
        return treeNode;
    }

    @Override
    public List<AssetVo> findSpaceByParentId(Integer parentId) {
        AssetVo vo = new AssetVo();
        short siteId = application.getSiteId();
        vo.setParentId(parentId);
        vo.setSiteId((int) siteId);
        List<AssetVo> findDevice = assetMapper.findDevice(vo);
        return findDevice;
    }

    @Override
    public AssetVo findProbeByPanrentAndCatalog(AssetVo vo) {
        Integer parentId = vo.getParentId();
        if (parentId == null)
            return null;

        Asset<?> asset = assetStore.findAsset(parentId);
        if (!asset.isCompound())
            return null;
        CompoundAsset<?> parentAsset = (CompoundAsset<?>) asset;

        Collection<Asset<?>> children = parentAsset.children();
        for (Asset<?> child : children) {
            if (child instanceof Probe) {
                AssetVo assetVo = null;
                Probe probe = (Probe) child;
                short catalog = probe.getMonitorKind();
                if ((int) catalog == ENERGY_CATALOG) {
                    assetVo = new AssetVo();
                    assetVo.setCaption(asset.getCaption());
                    assetVo.setId(probe.getId());
                    if (probe.getValue() != null) {
                        assetVo.setValue(probe.getValue().toString());
                    }
                    return assetVo;
                }
            }
        }
        return null;
    }

    @Override
    public List<AssetProperty> getPropertyDefaultValue(String typeName, String kind) {
        List<AssetProperty> plist = new ArrayList<>();
        // Set default property values.
        if (typeName == null) {
            return plist;
        }
        AssetTypeManager<AssetType> assetTypes = assetStore.getAssetTypes(AssetKind.valueOf(kind));
        AssetType type = assetTypes.find(typeName);
        if (type == null)
            return plist;
        for (AssetTypeProperty propType : type.props.values()) {
            AssetProperty property = new AssetProperty();
            final String defaultValue = propType.getDefault();
            String caption = propType.getCaption();

            property.setCaption(caption);

            property.setName(propType.getName());
            if (defaultValue != null && !defaultValue.equals("null")) {
                property.setValue(defaultValue);
            } else {
                property.setValue("");
            }
            plist.add(property);
        }
        return plist;
    }

    @Override
    public List<AssetVo> findSpace(AssetVo assetVo) {
        // TODO Auto-generated method stub
        return assetMapper.findSpace(assetVo);
    }

    @Override
    public DeviceIdResult getFirstDeviceIdByAssetId(Integer id) {
        // TODO Auto-generated method stub
        DeviceIdResult deviceIdResult = new DeviceIdResult();
        deviceIdResult.setDeviceId(assetStore.getFirstDeviceIdByAssetId(id));
        return deviceIdResult;
    }

    @Override
    public List<AllTreeNode> selectAssetTree(Integer catlog) {
        List<AllTreeNode> initAssetTree = initAssetTree("total", catlog);
        List<AllTreeNode> tree = selectAssetTree(initAssetTree);
        return tree;
    }

    public List<AllTreeNode> selectAssetTree(List<AllTreeNode> srcTree) {
        Iterator<AllTreeNode> it = srcTree.iterator();
        while (it.hasNext()) {
            AllTreeNode srcNode = it.next();
            if (!haveProbe(srcNode)) {
                it.remove();
            } else {
                List<AllTreeNode> srcChildren = srcNode.getChildren();
                if (srcChildren != null) {
                    selectAssetTree(srcChildren);
                }
            }
        }
        return srcTree;
    }

    private boolean haveProbe(AllTreeNode node) {
        if (AssetKind.PROBE.ordinal() == node.getKind()) {
            return true;
        } else {
            List<AllTreeNode> childList = node.getChildren();
            if (null != childList) {
                for (AllTreeNode treeNode : childList) {
                    boolean haveProbe = haveProbe(treeNode);
                    if (haveProbe) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    @Override
    public List<Integer> findDeviceLikeNameAndCaption(String name, String caption) {
        return assetMapper.findDeviceLikeNameAndCaption(name, caption);
    }

    @Override
    public List<Integer> findProbeLikeNameAndCaption(String name, String caption) {
        // TODO Auto-generated method stub
        return assetMapper.findProbeLikeNameAndCaption(name, caption);
    }

    private static final Integer ENERGY_CATALOGID = 1002;

    @Override
    public List<AssetVo> queryAssetAddEnergyDeviceType(Asset<?> root, int workId) {
        List<Integer> optionList = selectOptionDao.queryIdListByParent(workId);
        List<AssetVo> children = new ArrayList<AssetVo>();
        Collection<Asset<?>> childrens = new ArrayList<>();
        if (root != null) {
            if (root.isCompound()) {
                CompoundAsset<?> parent = (CompoundAsset<?>) root;
                childrens = parent.children();
                for (Asset<?> asset : childrens) {
                    AssetVo vo = new AssetVo();
                    vo.setFullName(assetStore.getAssetFullName(asset, ">", "name"));
                    vo.setId(asset.getId());
                    vo.setKind(asset.getKind().toString());
                    vo.setCaption(asset.getCaption());
                    vo.setName(asset.getName());
                    vo.setTypeName(asset.getType().getName());
                    if (asset.getState() != null) {
                        vo.setState(asset.getState().toString());
                        vo.setError(asset.getError());
                    }
                    Integer catalog = null;
                    if (asset.isMonitor()) {
                        Monitor<?> monitor = (Monitor<?>) asset;
                        loadMonitorVo(monitor, vo);
                        if (ENERGY_CATALOGID == monitor.getMonitorKind()) {
                            String ItemizeType = assetMapper.findProbeEnergyItemizeType(monitor.getId());
                            if (null != ItemizeType) {
                                vo.setDevice_type(ItemizeType);
                            }
                        }
                        catalog = (int) monitor.getMonitorKind();
                    } else if (asset instanceof Device) {
                        Device device = (Device) asset;
                        vo.setCatalogId((int) device.getCatalog());
                        catalog = (int) device.getCatalog();
                    }
                    setKindStr(asset, vo);
                    if (catalog == null || workId == 0) {
                        children.add(vo);
                    } else if (optionList.contains(catalog)) {
                        children.add(vo);
                    }

                }
            }
        }
        return children;
    }

    // 将父名称，Service封装到map,属性封装到List
    public void potService(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList, Integer parentId)
            throws AssetException {
        MonitorService service = null;
        Object typeName = assets.get("类型");
        if (typeName != null) {
            // 封装类型
            ServiceType serviceType = assetStore.getServiceTypes().find(typeName.toString());
            Object name = assets.get("名称");
            Asset<?> parent = assetStore.findAsset(parentId);
            // 获取到输入类型的实例，创建asset实例，封装参数
            int id = Math.abs((parent.getName() + name.toString()).hashCode());
            if (name != null) {
                service = assetStore.createAsset(serviceType, id, name.toString());
                service.setName(name.toString());
            }

            service.setType(serviceType);
            // 封装service 的属性
            addAProperty(assets, service, serviceType);
        }
        Object caption = assets.get("标题");
        if (caption != null) {
            service.setCaption(caption.toString());
        }
        Object desc = assets.get("描述");
        if (desc != null) {
            service.setDesc(desc.toString());
        }
        if (parentId != null) {
            service.setParentId(parentId);
        } else {
            Object parentName = assets.get("父名称");
            if (parentName != null) {
                service.setParentName(parentName.toString());
            } else {
                System.out.println("上传service ,没有父节点");
            }
        }

        alist.add(service);
    }

    // 将父名称，Probe封装到map,属性封装到List
    public void potProbe(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList, Integer parentId)
            throws AssetException {
        Probe probe = null;

        Object typeName = assets.get("类型");
        if (typeName != null) {
            // 封装类型
            ProbeType probeType = assetStore.getProbeTypes().find(typeName.toString());
            Object name = assets.get("名称");
            if (name != null) {
                Asset<?> parent = assetStore.findAsset(parentId);
                int id = Math.abs((parent.getName() + name.toString()).hashCode());
                probe = assetStore.createAsset(probeType, id, name.toString());
                probe.setName(name.toString());
            }
            probe.setType(probeType);
            // 封装brobe的属性信息
            addAProperty(assets, probe, probeType);

        }
        Object caption = assets.get("标题");
        if (caption != null) {
            probe.setCaption(caption.toString());
        }
        Object desc = assets.get("描述");
        if (desc != null) {
            probe.setDesc(desc.toString());
        }
        if (parentId != null) {
            probe.setParentId(parentId);
        } else {
            Object parentName = assets.get("父名称");
            if (parentName != null) {
                probe.setParentName(parentName.toString());
            } else {
                System.out.println("上传probe ,没有父节点");
            }
        }

        Object time = assets.get("时间间隔");
        if (time != null) {
            String str = time.toString();
            TimeSpan timespan = TimeSpan.parseValue(str);
            probe.setSavingInterval(timespan);
        }
        Object warning = assets.get("报警表达式");
        if (warning != null) {
            probe.setWarnCond(warning.toString());
        }
        Object transform = assets.get("结果变换");
        if (transform != null) {
            probe.setTransform(transform.toString());
        }
        Object service = assets.get("服务");
        if (service != null) {
            probe.setSources(service.toString());
        }
        alist.add(probe);

    }

    // 将父名称,evice到map,属性封装到List
    @Override
    public void potDevice(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList, Integer parentId)
            throws AssetException {
        Device device = null;

        Object typeName = assets.get("类型");
        if (typeName != null) {
            DeviceType deviceType = assetStore.getDeviceTypes().find(typeName.toString());
            Object name = assets.get("名称");
            if (name != null) {
                Asset<?> parent = assetStore.findAsset(parentId);
                int id = Math.abs((parent.getName() + name.toString()).hashCode());
                device = assetStore.createAsset(deviceType, id, name.toString());
                device.setName(name.toString());
            }
            device.setType(deviceType);
            // 封装device的属性信息
            addAProperty(assets, device, deviceType);

        }
        Object caption = assets.get("标题");
        if (caption != null) {
            device.setCaption(caption.toString());
        }
        Object desc = assets.get("描述");
        if (desc != null) {
            device.setDesc(desc.toString());
        }
        if (parentId != null) {
            device.setParentId(parentId);
        } else {
            Object parentName = assets.get("父名称");
            if (parentName != null) {
                device.setParentName(parentName.toString());
            } else {
                System.out.println("上传device ,没有父节点");
            }
        }

        Object purchaseDate = assets.get("采购日期");
        if (purchaseDate != null) {
            device.setPurchaseDate(LocalDateTime.parse(purchaseDate.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        Object vendor = assets.get("生产厂家");
        if (vendor != null) {
            device.setVendor(vendor.toString());
        }
        Object warrantyDate = assets.get("保修截止日期");
        if (warrantyDate != null) {
            device.setWarrantyDate(LocalDate.parse(warrantyDate.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        alist.add(device);

    }

    //将父名称，control到map,属性封装到List
    @Override
    public void potControl(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList, Integer parentId)
            throws AssetException {
        Control control = null;
        Object typeName = assets.get("类型");
        if (typeName != null) {
            // 封装类型
            ControlType controlType = assetStore.getControlTypes().find(typeName.toString());
            Object name = assets.get("名称");
            if (name != null) {
                Asset<?> parent = assetStore.findAsset(parentId);
                int id = Math.abs((parent.getName() + name.toString()).hashCode());
                control = assetStore.createAsset(controlType, id, name.toString());
                control.setName(name.toString());
            }
            control.setType(controlType);
            addAProperty(assets, control, controlType);
        }
        Object caption = assets.get("标题");
        if (caption != null) {
            control.setCaption(caption.toString());
        }
        Object desc = assets.get("描述");
        if (desc != null) {
            control.setDesc(desc.toString());
        }
        Object transform = assets.get("结果变换");
        if (transform != null) {
            control.setTransform(transform.toString());
        }
        if (parentId != null) {
            control.setParentId(parentId);
        } else {
            Object parentName = assets.get("父名称");
            if (parentName != null) {
                control.setParentName(parentName.toString());
            } else {
                System.out.println("上传control,没有父节点");
            }
        }

        Object time = assets.get("时间间隔");

        if (time != null) {
            String str = time.toString();
            TimeSpan timespan = TimeSpan.parseValue(str);
            control.setSavingInterval(timespan);
        }

        Object refreshDelay = assets.get("延迟刷新的秒数");
        if (refreshDelay != null) {
            control.setRefreshDelay(Integer.parseInt(refreshDelay.toString()));
        }
        Object warning = assets.get("报警表达式");
        if (warning != null) {
            control.setWarnCond(warning.toString());
        }
        Object service = assets.get("服务");
        if (service != null) {
            control.setSources(service.toString());
        }
        alist.add(control);

    }

    // 将父名称，space到map
    @Override
    public void potSpace(List<Asset<?>> alist, Map<String, Object> assets, Integer parentId) throws AssetException {
        Space space = null;
        Object typeName = assets.get("类型");
        if (typeName != null) {
            SpaceType spaceType = assetStore.getSpaceTypes().find(typeName.toString());
            Object name = assets.get("名称");
            if (name != null) {
                Asset<?> parent = assetStore.findAsset(parentId);
                int id = Math.abs((parent.getName() + name.toString()).hashCode());
                space = assetStore.createAsset(spaceType, id, name.toString());
                space.setName(name.toString());
            }
            space.setType(spaceType);
        }
        Object caption = assets.get("标题");
        if (caption != null) {
            space.setCaption(caption.toString());
        }
        Object desc = assets.get("描述");
        if (desc != null) {
            space.setDesc(desc.toString());
        }
        if (parentId != null) {
            space.setParentId(parentId);
        } else {
            Object parentName = assets.get("父名称");
            if (parentName != null) {
                space.setParentName(parentName.toString());
            } else {
                System.out.println("上传space ,没有父节点");
            }
        }

        alist.add(space);

    }

    // 封装 每一个属性的通用方法
    private void addAProperty(Map<String, Object> assets, Asset<?> asset, AssetType assetType) {
        List<AssetProperty> aps = getPropertyNameList(assetType);
        for (AssetProperty ap : aps) {
            Object propertyValue = assets.get(ap.getName());

            AssetTypeProperty property = assetType.getProperty(ap.getName());
            if (property != null) {// 根据类型的不同，属性值不同
                setPropertyValue(asset, propertyValue, property);
            }
        }

    }

    /* 根据类型实例，获取属性名的集合 */
    private List<AssetProperty> getPropertyNameList(AssetType assetType) {
        List<AssetProperty> assetPropertys = new ArrayList<AssetProperty>();
        Collection<AssetTypeProperty> properties = assetType.properties();
        if (properties != null) {
            for (AssetTypeProperty property : properties) {
                AssetProperty assetProperty = new AssetProperty();
                assetProperty.setName(property.getName());
                assetProperty.setCaption(property.getCaption());
                assetPropertys.add(assetProperty);
            }
        }
        return assetPropertys;
    }

    @Override
    public List<TreeNode> queryDeviceBySpaceId(Integer spaceId) {

        List<TreeNode> allSpaceTree = getAllSpaceTree(spaceId);

        String spaceIdsNotFirst = getSpaceIds(allSpaceTree, "");
        String spaceIds = spaceIdsNotFirst + spaceId;

        List<TreeNode> deviceTree = new ArrayList<>();
        Collection<Asset<?>> assets = assetStore.getAssets(GenericDevice.class);
        for (Asset<?> asset : assets) {
            Device device = (Device) asset;
            if (!"".equals(spaceIds) && spaceIds.contains(device.getParentId().toString())) {
                TreeNode treeNode = addChildrenDevice(device);
                deviceTree.add(treeNode);
            }
        }
        return deviceTree;
    }

    private TreeNode addChildrenDevice(Device device) {
        TreeNode node = new TreeNode();
        node.setFullName(assetStore.getAssetFullName(device, ">", "name"));
        node.setId(device.getId());
        node.setKind(device.getKind().ordinal());
        node.setText(device.getCaption());
        node.setName(device.getName());
        node.setCatalog((int) device.getCatalog());
        Collection<Asset<?>> children = device.children();
        if (null != children && !children.isEmpty()) {
            List<TreeNode> childrens = new ArrayList<>();
            for (Asset<?> chil : children) {
                if (chil instanceof Device) {
                    TreeNode childrenDevice = addChildrenDevice((Device) chil);
                    childrens.add(childrenDevice);
                }
            }
            node.setNodes(childrens.isEmpty() ? null : childrens);

        }
        return node;
    }

    private String getSpaceIds(List<TreeNode> allSpaceTree, String spaceIds) {
        for (TreeNode treeNode : allSpaceTree) {
            spaceIds += treeNode.getId() + ",";
            List<TreeNode> nodes = treeNode.getNodes();
            if (null != nodes && nodes.size() > 0) {
                spaceIds = getSpaceIds(nodes, spaceIds);
            }
        }
        return spaceIds;
    }


}

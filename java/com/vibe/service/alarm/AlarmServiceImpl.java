package com.vibe.service.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.vibe.pojo.*;
import com.vibe.util.StringUtils;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.common.code.CascadeCodeItem;
import com.vibe.common.code.CodeDictManager;
import com.vibe.mapper.alarm.AlarmMessageDao;
import com.vibe.monitor.alarm.AlarmRule;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.push.Push;
import com.vibe.service.asset.AssetService;
import com.vibe.service.classification.Code;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.time.TimeUtil;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMessageDao alarmMessageDao;

    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetStore assetStore;
    @Autowired
    CodeDictManager codeDictManager;

    @Override
    public EasyUIJson queryAlarmMessageListByTime(int pageNum, int pageCount, String start, String end, int handled) {
        // TODO Auto-generated method stub
        //PageHelper.startPage(pageNum, pageCount);
        List<AlarmMessage> list = alarmMessageDao.queryAllAlarmMessageListByTime(start, end, handled);
        for (AlarmMessage alarmMessage : list) {
            if (assetStore.findAsset(alarmMessage.getAssetId()) instanceof Monitor) {
                alarmMessage.setSystem(((CascadeCodeItem) codeDictManager.getLocalDict().getItem((short) Code.PROBE, ((Monitor<?>) assetStore.findAsset(alarmMessage.getAssetId())).getMonitorKind())).getParent().getName());
            }
            if (assetStore.findAsset(alarmMessage.getAssetId()) instanceof Device) {
                alarmMessage.setSystem(((CascadeCodeItem) codeDictManager.getLocalDict().getItem((short) Code.DEVICE, ((Device) assetStore.findAsset(alarmMessage.getAssetId())).getCatalog())).getParent().getName());
            }

        }
//		AlarmMessageMap alarmMessageMap = new AlarmMessageMap();
//		alarmMessageMap.setList(list.subList((pageNum - 1) * pageCount, pageNum * pageCount > list.size() ? list.size() : pageNum * pageCount));
//		//alarmMessageMap.setSize(((Page<AlarmMessage>)list).getTotal());
//		alarmMessageMap.setSize((long) list.size());
//		return alarmMessageMap;
        // 创建EasyUIJson对象，封装查询结果
        EasyUIJson uiJson = new EasyUIJson();
        // 设置查询总记录数
        uiJson.setTotal((long) list.size());
        // 设置查询记录
        uiJson.setRows(
                list.subList((pageNum - 1) * pageCount, pageNum * pageCount > list.size() ? list.size() : pageNum * pageCount));
        return uiJson;
    }


    @Override
    public void handleAlarmMessage(int id) {
        // TODO Auto-generated method stub
        alarmMessageDao.handleAlarmMessage(id);
        try {
            Push.getPush().removeMsg(id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private AlarmRule getAlarmRuleByAssetId(int id) {
        // TODO Auto-generated method stub
        return alarmMessageDao.getAlarmRuleByAssetId(id);
    }


    @Override
    public void insertAlarmRule(InsertUpdateAlarmRuleItem insertUpdateAlarmRuleItem) {
        // TODO Auto-generated method stub
        alarmMessageDao.insertAlarmRule(insertUpdateAlarmRuleItem.getId(), insertUpdateAlarmRuleItem.getRule(), insertUpdateAlarmRuleItem.getWay(), insertUpdateAlarmRuleItem.getStart());
    }


    @Override
    public void updateAlarmRule(InsertUpdateAlarmRuleItem insertUpdateAlarmRuleItem) {
        // TODO Auto-generated method stub
        alarmMessageDao.updateAlarmRule(insertUpdateAlarmRuleItem.getId(), insertUpdateAlarmRuleItem.getRule(), insertUpdateAlarmRuleItem.getWay(), insertUpdateAlarmRuleItem.getStart());
    }


    @Override
    public List<TreeAlarmData> getTreeAlarmRule() {
        // TODO Auto-generated method stub
        List<TreeAlarmData> alarmDatas = new ArrayList<>();
        TreeAlarmData treeAlarmData = new TreeAlarmData();
        alarmDatas.add(treeAlarmData);
        List<AllTreeNode> allTreeNodes = assetService.initAssetTree("total", null);
        if (allTreeNodes != null && allTreeNodes.size() != 0) {
            AllTreeNode allTreeNode = allTreeNodes.get(0);
            initTreeAlarmData(allTreeNode, treeAlarmData);
        }
        return alarmDatas;
    }

    private void initTreeAlarmData(AllTreeNode allTreeNode, TreeAlarmData treeAlarmData) {
        treeAlarmData.setId(allTreeNode.getId());
        treeAlarmData.setText(allTreeNode.getText());
        if (allTreeNode.getChildren() != null) {
            List<TreeAlarmData> treeAlarmDataChildrenList = new ArrayList<>();
            treeAlarmData.setChildren(treeAlarmDataChildrenList);
            for (AllTreeNode child : allTreeNode.getChildren()) {
                TreeAlarmData treeAlarmDataChild = new TreeAlarmData();
                treeAlarmDataChildrenList.add(treeAlarmDataChild);
                initTreeAlarmData(child, treeAlarmDataChild);
            }
        } else {
            AlarmRule alarmRule = getAlarmRuleByAssetId(allTreeNode.getId());
            if (alarmRule != null) {
                TreeAlarmData.AlarmData alarmData = treeAlarmData.new AlarmData();
                alarmData.setRule(alarmRule.getRule());
                alarmData.setWay(alarmRule.getWay());
                alarmData.setStart(alarmRule.getStart());
                treeAlarmData.setAlarm(alarmData);
            }
        }
    }


    @Override
    public void insertUpdateTreeAlarmRule(InsertUpdateAlarmRuleData insertUpdateAlarmRuleData) {
        // TODO Auto-generated method stub
        if (insertUpdateAlarmRuleData != null) {
            if (insertUpdateAlarmRuleData.getAdd() != null) {
                for (InsertUpdateAlarmRuleItem item : insertUpdateAlarmRuleData.getAdd()) {
                    insertAlarmRule(item);
                }
            }
            if (insertUpdateAlarmRuleData.getUpdate() != null) {
                for (InsertUpdateAlarmRuleItem item : insertUpdateAlarmRuleData.getUpdate()) {
                    updateAlarmRule(item);
                }
            }
        }
    }


    @Override
    public void insertPersonAlarm(AlarmMessage alarmMessage) {
        // TODO Auto-generated method stub
        if (alarmMessage.getAssetId() != 0 && alarmMessageDao.getAlarmRuleByAssetId(alarmMessage.getAssetId()) != null) {
            alarmMessage.setAlarmRuleId(alarmMessageDao.getAlarmRuleByAssetId(alarmMessage.getAssetId()).getId());
        }
        alarmMessageDao.insertPersonAlarmLog(alarmMessage);
        alarmMessageDao.insertPersonAlarmMessage(alarmMessage.getId(), (alarmMessage.getAssetId() == 0 ? "" : assetStore.getAssetFullName(assetStore.findAsset(alarmMessage.getAssetId()), AssetStore.LOCATION_SEPARATOR)) + " " + alarmMessage.getCaption());
        com.vibe.monitor.alarm.AlarmData alarmData = new com.vibe.monitor.alarm.AlarmData();
        alarmData.setAssetId(alarmMessage.getAssetId());
        alarmData.setErrorMessage(alarmMessage.getErrorMessage());
        alarmData.setAuto(2);
        alarmData.setState(2);
        alarmData.setCaption(alarmMessage.getCaption());
        alarmData.setTime(TimeUtil.getCurrentDateTime());
        Push.getPush().addMsg(Push.ALARM_TYPE, alarmData);
    }


    @Override
    public List<AlarmMessage> queryAlarmMessageListByAssetId(int pageNum, int pageCount, int assetId) {
        // TODO Auto-generated method stub
        return alarmMessageDao.queryAlarmMessageListByAssetId(pageNum, pageCount, assetId);
    }

    @Override
    public ResponseModel getAlarmByTime(String time) {
        return this.getAlarmByTimeState(time, null);
    }

    @Override
    public ResponseModel getAlarmByTimeState(String time, Integer state) {
        if (StringUtils.isBlank(time)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = dateFormat.format(new Date());
        }
        List<AvgNum> list = alarmMessageDao.getAlarmByTimeState(time, state);
        return ResponseModel.success(ResultUtils.avgResultFormat(list, new Date())).code(ResultCode.SUCCESS);
    }
}

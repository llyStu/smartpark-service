package com.vibe.service.alarm;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vibe.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.common.code.CascadeCodeItem;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.code.CodeItem;
import com.vibe.mapper.alarm.AlarmMessageDao;
import com.vibe.monitor.alarm.AlarmRule;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.push.Push;
import com.vibe.pojo.AlarmMessage;
import com.vibe.pojo.InsertUpdateAlarmRuleData;
import com.vibe.pojo.InsertUpdateAlarmRuleItem;
import com.vibe.pojo.TreeAlarmData;
import com.vibe.service.asset.AssetService;
import com.vibe.service.classification.Code;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.time.TimeUtil;

@Service
public class AlarmServiceImpl implements AlarmService{

	@Autowired
	private AlarmMessageDao alarmMessageDao;

	@Autowired
	private AssetService assetService;
	@Autowired
	private AssetStore assetStore;
	@Autowired
	CodeDictManager codeDictManager;
	@Override
	public EasyUIJson queryAlarmMessageListByTime(int pageNum, int pageCount,String start,String end, int handled) {
		// TODO Auto-generated method stub
		//PageHelper.startPage(pageNum, pageCount);
		List<AlarmMessage> list = alarmMessageDao.queryAllAlarmMessageListByTime(start,end,handled);
		for (AlarmMessage alarmMessage : list) {
			if (assetStore.findAsset(alarmMessage.getAssetId()) instanceof Monitor) {
				alarmMessage.setSystem(((CascadeCodeItem)codeDictManager.getLocalDict().getItem((short)Code.PROBE,((Monitor<?>)assetStore.findAsset(alarmMessage.getAssetId())).getMonitorKind())).getParent().getName());
			}
			if(assetStore.findAsset(alarmMessage.getAssetId()) instanceof Device){
				alarmMessage.setSystem(((CascadeCodeItem)codeDictManager.getLocalDict().getItem((short)Code.DEVICE,((Device)assetStore.findAsset(alarmMessage.getAssetId())).getCatalog())).getParent().getName());
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
		List<AllTreeNode> allTreeNodes = assetService.initAssetTree("total",null);
		if(allTreeNodes!=null && allTreeNodes.size()!=0){
			AllTreeNode allTreeNode = allTreeNodes.get(0);
			initTreeAlarmData(allTreeNode,treeAlarmData);
		}
		return alarmDatas;
	}
	private void initTreeAlarmData(AllTreeNode allTreeNode,TreeAlarmData treeAlarmData) {
		treeAlarmData.setId(allTreeNode.getId());
		treeAlarmData.setText(allTreeNode.getText());
		if(allTreeNode.getChildren()!=null){
			List<TreeAlarmData> treeAlarmDataChildrenList = new ArrayList<>();
			treeAlarmData.setChildren(treeAlarmDataChildrenList);
			for (AllTreeNode child : allTreeNode.getChildren()) {
				TreeAlarmData treeAlarmDataChild = new TreeAlarmData();
				treeAlarmDataChildrenList.add(treeAlarmDataChild);
				initTreeAlarmData(child,treeAlarmDataChild);
			}
		}else {
			AlarmRule alarmRule = getAlarmRuleByAssetId(allTreeNode.getId());
			if(alarmRule!=null){
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
		if(insertUpdateAlarmRuleData!=null){
			if(insertUpdateAlarmRuleData.getAdd()!=null){
				for (InsertUpdateAlarmRuleItem item : insertUpdateAlarmRuleData.getAdd()) {
					insertAlarmRule(item);
				}
			}
			if(insertUpdateAlarmRuleData.getUpdate()!=null){
				for (InsertUpdateAlarmRuleItem item : insertUpdateAlarmRuleData.getUpdate()) {
					updateAlarmRule(item);
				}
			}
		}
	}


	@Override
	public void insertPersonAlarm(AlarmMessage alarmMessage) {
		// TODO Auto-generated method stub
		if(alarmMessage.getAssetId()!=0 && alarmMessageDao.getAlarmRuleByAssetId(alarmMessage.getAssetId())!=null){
			alarmMessage.setAlarmRuleId(alarmMessageDao.getAlarmRuleByAssetId(alarmMessage.getAssetId()).getId());
		}
		alarmMessageDao.insertPersonAlarmLog(alarmMessage);
		alarmMessageDao.insertPersonAlarmMessage(alarmMessage.getId(), (alarmMessage.getAssetId()==0?"":assetStore.getAssetFullName(assetStore.findAsset(alarmMessage.getAssetId()), AssetStore.LOCATION_SEPARATOR)) + " " + alarmMessage.getCaption());
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

	/**
	 * 报警消息查询
	 */
	@Override
	public EasyUIJson queryAlarmMessages(int page, int rows, AlarmMessage am) {
		List<AlarmMessage> queryAlarmMessages = alarmMessageDao.queryAlarmMessages(am);
		final Integer catalogId = am.getCatalogId();

		List<AlarmMessage> alarmMessageList=new ArrayList<>();
		for (AlarmMessage alarmMessage : queryAlarmMessages) {
			if(0==alarmMessage.getAssetId())continue;

			Asset<?> asset = assetStore.findAsset(alarmMessage.getAssetId());
			if(null==asset){
				continue;
			}
			alarmMessage.setMonitorId(asset.getId());
			alarmMessage.setMonitorName(asset.getCaption());//添加检测器
			alarmMessage.setDeviceId(asset.getParentId());
			CompoundAsset<?> parent = asset.getParent();
			if(parent instanceof Device){
				alarmMessage.setDeviceName(parent.getCaption());//添加设备
				CodeItem item = codeDictManager.getLocalDict().getItem((short)Code.DEVICE,((Device)parent).getCatalog());
				alarmMessage.setCatalogId((int)item.getId());
				alarmMessage.setCatalogName(item.getName());
			}
			if (asset instanceof Monitor) {
				alarmMessage.setSystem(((CascadeCodeItem)codeDictManager.getLocalDict().getItem((short)Code.PROBE,((Monitor<?>)asset).getMonitorKind())).getParent().getName());
			}else if(asset instanceof Device){
				alarmMessage.setSystem(((CascadeCodeItem)codeDictManager.getLocalDict().getItem((short)Code.DEVICE,((Device)asset).getCatalog())).getParent().getName());
			}//添加系统

			if(catalogId==null){
				alarmMessageList.add(alarmMessage);
			}else{
				if(catalogId.equals(alarmMessage.getCatalogId())){
					alarmMessageList.add(alarmMessage);
				}
			}
		}
		EasyUIJson uiJson = new EasyUIJson();
		// 设置查询总记录数
		uiJson.setTotal((long) alarmMessageList.size());
		// 设置查询记录
		uiJson.setRows(
				alarmMessageList.subList((page - 1) * rows, page * rows > alarmMessageList.size() ? alarmMessageList.size() : page * rows));
		return uiJson;
	}




	/*@Override
	public void updatePersonAlarm(AlarmMessage alarmMessage) {
		// TODO Auto-generated method stub

	}*/

}

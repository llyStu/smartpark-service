package com.vibe.service.alarm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.common.code.CascadeCodeItem;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.code.CodeItem;
import com.vibe.mapper.alarm.AlarmRuleMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.pojo.event.AlarmRuleMessage;
import com.vibe.pojo.event.EventRank;
import com.vibe.pojo.event.RuleRankRelation;
import com.vibe.service.classification.Code;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.EasyUIJson;

@Service
public class AlarmRuleServiceImpl  implements AlarmRuleService{

    @Autowired
    private AlarmRuleMapper alarmRuleMapper;

    @Autowired
    private AssetStore assetStore;
    @Autowired
    CodeDictManager codeDictManager;

    @Override
    public EasyUIJson queryAlarmRules(Integer page, Integer rows, AlarmRuleMessage amr) {
        // TODO Auto-generated method stub
        List<AlarmRuleMessage> alarms=alarmRuleMapper.queryAlarmRules(amr);
        List<AlarmRuleMessage> alarmsList=new ArrayList<AlarmRuleMessage>();
        final Integer deviceCatalogId = amr.getParentCatalogId();
        for (AlarmRuleMessage alarmRule : alarms) {
            CodeItem monitorItem = codeDictManager.getLocalDict().getItem((short) Code.PROBE, Short.parseShort(alarmRule.getCatalog() + ""));
            if (null != monitorItem){
                alarmRule.setCatalogName(monitorItem.getName());// 点位类型
            }
            Asset<?> asset = assetStore.findAsset(alarmRule.getParentId());
            if (null == asset)
                continue;
            if (asset instanceof Device) {
                alarmRule.setParentCaption(asset.getCaption());// 添加设备
                CodeItem item = codeDictManager.getLocalDict().getItem((short) Code.DEVICE,((Device) asset).getCatalog());
                alarmRule.setParentCatalogId((int) item.getId());
                alarmRule.setParentCatalogName(item.getName());
                alarmRule.setSystem(((CascadeCodeItem) codeDictManager.getLocalDict().getItem((short) Code.DEVICE,((Device) asset).getCatalog())).getParent().getName());
            }
            if (deviceCatalogId == null) {
                alarmsList.add(alarmRule);
            } else {
                if (deviceCatalogId.equals(alarmRule.getParentCatalogId())) {
                    alarmsList.add(alarmRule);
                }
            }
        }
        EasyUIJson uiJson = new EasyUIJson();
        // 设置查询总记录数
        uiJson.setTotal((long) alarmsList.size());
        // 设置查询记录
        uiJson.setRows(
                alarmsList.subList((page - 1) * rows, page * rows > alarmsList.size() ? alarmsList.size() : page * rows));
        return uiJson;
    }


    @Override
    public List<EventRank> queryEventRanks(Integer rankId) {
        // TODO Auto-generated method stub
        return alarmRuleMapper.queryEventRanks(rankId);
    }


    @Override
    public ResponseModel<String> updateAssetEventRank(Integer[] ids, Integer rankId) {
        // TODO Auto-generated method stub
        int updataAssetEventRank = alarmRuleMapper.updateAssetEventRank(ids,rankId);
        return ResponseModel.success("成功修改"+updataAssetEventRank).code(ResultCode.SUCCESS);

    }


    @Override
    public ResponseModel<String> deleteEventRank(List<RuleRankRelation> ruleRankRelations) {
        if(ruleRankRelations.isEmpty())return ResponseModel.failure("不能为空").code(ResultCode.ERROR);
        for (RuleRankRelation ruleRankRelation : ruleRankRelations) {
            Integer id = ruleRankRelation.getId();
            Integer assetId = ruleRankRelation.getAssetId();
            String delwarnCond = ruleRankRelation.getWarnCond();
            Asset<?> findAsset = assetStore.findAsset(assetId);
            if(findAsset instanceof Monitor){
                Monitor<?> monitor=	(Monitor<?>)findAsset;
                String oldWarnCond = monitor.getWarnCond();
                int result=alarmRuleMapper.deleteEventRank(id);
                if(null!=oldWarnCond && result==1){
                    String newWarnCond = getNewWarnCond(delwarnCond, oldWarnCond);
                    if(null==newWarnCond)monitor.setWarnCond(null);
                    monitor.setWarnCond(newWarnCond);
                }
            }
        }
        return ResponseModel.success("删除成功").code(ResultCode.SUCCESS);
    }


    public String getNewWarnCond(String delWarnCond, String oldWarnCond) {
        StringBuffer newWarnCond=new StringBuffer();
        if(oldWarnCond.contains("\\|\\|")){
            String[] split = oldWarnCond.split("\\|\\|");
            for (int i = 0; i < split.length; i++) {
                if(!split[i].equals(delWarnCond)){
                    if(i!=split.length-1){
                        newWarnCond.append(split[i]);
                        newWarnCond.append("\\|\\|");
                    }else{
                        newWarnCond.append(split[i]);
                    }
                }
            }
            return newWarnCond.toString();
        }else if(delWarnCond.equals(oldWarnCond)){
            return "";
        }
        return "";
    }

}

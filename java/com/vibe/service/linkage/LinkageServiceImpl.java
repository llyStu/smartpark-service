package com.vibe.service.linkage;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.service.energy.Meter;
import com.vibe.utils.EasyUIJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.common.Application;
import com.vibe.mapper.linkage.LinkageDao;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.db.LinkageDAO;
import com.vibe.monitor.linkage.LinkageRuleCauseBean;
import com.vibe.monitor.linkage.LinkageRuleEffectBean;
import com.vibe.monitor.util.UnitInterface;
import com.vibe.monitor.util.UnitUtil;
import com.vibe.pojo.LinkageCause;
import com.vibe.pojo.LinkageEffect;
import com.vibe.pojo.LinkageLogBean;
import com.vibe.pojo.LinkageLogCauseBean;
import com.vibe.pojo.LinkageLogEffectBean;
import com.vibe.pojo.LinkageRule;
import com.vibe.pojo.LinkageRuleBean;

@Service
public class LinkageServiceImpl implements LinkageService {

	@Autowired
	private LinkageDao linkageDao;
	@Autowired
	private Application app;

	@Autowired
	private AssetStore assetStore;

	private static String UNIT_SEPARATOR = "\\|";
	private static String UNIT_SEPARATOR_Str = "|";
	private static String ID_SEPARATOR = ",";

	@Override
	public void insertLinkageRule(LinkageRuleBean linkageRuleBean) {
		// TODO Auto-generated method stub
		linkageDao.insertLinkageRuleCause(linkageRuleBean.getCauseBean());
		for (LinkageRuleEffectBean effectBean : linkageRuleBean.getEffectBean()) {
			effectBean.setCauseId(linkageRuleBean.getCauseBean().getId());
			linkageDao.insertLinkageRuleEffect(effectBean);
		}
	}

	@Override
	public EasyUIJson queryLinkageLogListByTime(int pageNum, int pageCount, String start, String end) {
		// TODO Auto-generated method stub
		List<LinkageLogBean> linkageLogBeans = new ArrayList<>();
		LinkageDAO rootLinkageDao = new LinkageDAO(app.getSqlSession());
		PageHelper.startPage(pageNum, pageCount);
		List<LinkageLogCauseBean> causeBeans = linkageDao.getLinkageLog(start,end);
		if (causeBeans != null) {
			for (LinkageLogCauseBean logBean : causeBeans) {
				LinkageLogBean linkageLogBean = new LinkageLogBean();
				List<LinkageRuleEffectBean> linkageRuleEffectBeans = rootLinkageDao
						.getLinkageRuleEffects(logBean.getId());
				linkageLogBean.setTime(logBean.getTime());
				String unit = ((Monitor<?>) assetStore.findAsset(logBean.getCauseAsset())).getUnit();
				UnitUtil.unitParse(unit, logBean.getValue()+"", new UnitInterface() {
					
					@Override
					public void parseResult(String result) {
						// TODO Auto-generated method stub
						linkageLogBean.setValue(result);
					}
				});
				linkageLogBean.setCauseAsset(
						assetStore.getAssetFullName(assetStore.findAsset(logBean.getCauseAsset()), AssetStore.LOCATION_SEPARATOR));
				List<LinkageLogEffectBean> effectBeans = new ArrayList<>();
				if (linkageRuleEffectBeans != null) {
					for (LinkageRuleEffectBean linkageRuleEffectBean : linkageRuleEffectBeans) {
						LinkageLogEffectBean linkageLogEffectBean = new LinkageLogEffectBean();
						linkageLogEffectBean.setEffectAsset(assetStore.getAssetFullName(
								assetStore.findAsset(linkageRuleEffectBean.getAssetId()), AssetStore.LOCATION_SEPARATOR));
						if(linkageRuleEffectBean.getValue()!=-1){
							String effectUnit = ((Monitor<?>) assetStore.findAsset(linkageRuleEffectBean.getAssetId())).getUnit();
							UnitUtil.unitParse(effectUnit, linkageRuleEffectBean.getValue()+"", new UnitInterface() {
								
								@Override
								public void parseResult(String result) {
									// TODO Auto-generated method stub
									linkageLogEffectBean.setValue(result);
								}
							});
						}
						effectBeans.add(linkageLogEffectBean);
					}
					linkageLogBean.setEffectBeans(effectBeans);
				}
				linkageLogBeans.add(linkageLogBean);
			}
		}
		PageInfo<LinkageLogCauseBean> pageInfo = new PageInfo<LinkageLogCauseBean>(causeBeans);
		EasyUIJson uiJson = new EasyUIJson();
		uiJson.setTotal(pageInfo.getTotal());
		uiJson.setRows(linkageLogBeans);
		return uiJson;
	}

	@Override
	public List<LinkageRule> queryLinkageList() {
		// TODO Auto-generated method stub
		List<LinkageRule> linkageRules = new ArrayList<>();
		LinkageDAO rootLinkageDao = new LinkageDAO(app.getSqlSession());
		List<LinkageRuleCauseBean> linkageRuleCauseBeans = rootLinkageDao.getLinkageRuleCauses();
		if (linkageRuleCauseBeans != null) {
			for (LinkageRuleCauseBean linkageRuleCauseBean : linkageRuleCauseBeans) {
				LinkageRule linkageRule = new LinkageRule();
				LinkageCause linkageCause = new LinkageCause();
				linkageCause.setId(linkageRuleCauseBean.getId());
				linkageCause.setAssetId(linkageRuleCauseBean.getAssetId());
				linkageCause.setValue(linkageRuleCauseBean.getValue());
				linkageCause.setAssetStr(assetStore
						.getAssetFullName(assetStore.findAsset(linkageRuleCauseBean.getAssetId()), AssetStore.LOCATION_SEPARATOR));
				linkageCause.setValueStr(((Monitor<?>) assetStore.findAsset(linkageRuleCauseBean.getAssetId())).getUnit());
				linkageRule.setLinkageCause(linkageCause);
				List<LinkageRuleEffectBean> linkageRuleEffectBeans = rootLinkageDao
						.getLinkageRuleEffects(linkageRuleCauseBean.getId());
				if (linkageRuleEffectBeans != null) {
					List<LinkageEffect> linkageEffects = new ArrayList<>();
					for (LinkageRuleEffectBean linkageRuleEffectBean : linkageRuleEffectBeans) {
						LinkageEffect linkageEffect = new LinkageEffect();
						linkageEffect.setAssetId(linkageRuleEffectBean.getAssetId());
						linkageEffect.setCauseId(linkageRuleEffectBean.getCauseId());
						linkageEffect.setId(linkageRuleEffectBean.getId());
						linkageEffect.setValue(linkageRuleEffectBean.getValue());
						linkageEffect.setAssetStr(assetStore.getAssetFullName(
								assetStore.findAsset(linkageRuleEffectBean.getAssetId()), AssetStore.LOCATION_SEPARATOR));
						if (assetStore.findAsset(linkageRuleEffectBean.getAssetId()).isMonitor() && ((Monitor<?>) assetStore.findAsset(linkageRuleEffectBean.getAssetId())).getUnit() != null) {
							linkageEffect.setValueStr(((Monitor<?>) assetStore.findAsset(linkageRuleEffectBean.getAssetId())).getUnit());
						}
						linkageEffects.add(linkageEffect);
					}
					linkageRule.setLinkageEffects(linkageEffects);
				}
				linkageRules.add(linkageRule);
			}
		}

		return linkageRules;
	}

	@Override
	public void deleteLinkageRuleCause(String linkageIds) {
		// TODO Auto-generated method stub
		String[] linkageIdArr = linkageIds.split(ID_SEPARATOR);
		for (String id : linkageIdArr) {
			int parseInt = Integer.parseInt(id);
			linkageDao.deleteLinkageRuleCause(parseInt);
			linkageDao.deleteLinkageRuleEffectByCause(parseInt);
		}
	}

	@Override
	public void updateLinkage(LinkageRuleBean linkageRuleBean) {
		//更新Cause
		LinkageRuleCauseBean inCausebean = linkageRuleBean.getCauseBean();
		int inCauseId = inCausebean.getId();
		LinkageRuleCauseBean causeBean = linkageDao.getLinkageRuleCause(inCauseId);
		causeBean.setAssetId(inCausebean.getAssetId());
		causeBean.setValue(inCausebean.getValue());
		linkageDao.updateLinkageRuleCause(causeBean);
		//先删除，在新加
		List<LinkageRuleEffectBean> effectBeans = linkageRuleBean.getEffectBean();
		linkageDao.deleteLinkageRuleEffectByCause(inCauseId);
		for (LinkageRuleEffectBean effectBean : effectBeans) {
			effectBean.setCauseId(inCauseId);
			linkageDao.insertLinkageRuleEffect(effectBean);
		}
	}

}

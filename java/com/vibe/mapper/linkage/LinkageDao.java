package com.vibe.mapper.linkage;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.vibe.monitor.linkage.LinkageRuleCauseBean;
import com.vibe.monitor.linkage.LinkageRuleEffectBean;
import com.vibe.pojo.LinkageLogCauseBean;

@Repository
public interface LinkageDao {

	public int insertLinkageRuleCause(LinkageRuleCauseBean linkageRuleCauseBean);
	
	public void insertLinkageRuleEffect(LinkageRuleEffectBean linkageRuleEffectBean);
	
//	public List<LinkageLogCauseBean> getLinkageLog(@Param("start") String start,@Param("end") String end);
	public List<LinkageLogCauseBean> getLinkageLog(@Param("start") String start,@Param("end") String end);

	public void deleteLinkageRuleCause(@Param("id") int id);
	
	public LinkageRuleCauseBean getLinkageRuleCause(@Param("id") int id);
	
	public void updateLinkageRuleCause(LinkageRuleCauseBean linkageRuleCauseBean);
	
	public void deleteLinkageRuleEffect(@Param("id") int id);
	
	public void updateLinkageRuleEffect(LinkageRuleEffectBean linkageRuleEffectBean);
	
	public LinkageRuleEffectBean queryLinkageEffect(LinkageRuleEffectBean linkageRuleEffectBean);
	
	public void deleteLinkageRuleEffectByCause(@Param("causeId") int causeId);
}

package com.vibe.service.linkage;

import java.util.List;

import com.vibe.pojo.LinkageLogBean;
import com.vibe.pojo.LinkageRule;
import com.vibe.pojo.LinkageRuleBean;
import com.vibe.utils.EasyUIJson;

public interface LinkageService {

	public void insertLinkageRule(LinkageRuleBean linkageRuleBean);
	
	public EasyUIJson queryLinkageLogListByTime(int pageNum, int pageCount, String start, String end);

	public List<LinkageRule> queryLinkageList();
	
	public void deleteLinkageRuleCause(String linkageIds);
	
	public void updateLinkage(LinkageRuleBean linkageRuleBean);
	
}

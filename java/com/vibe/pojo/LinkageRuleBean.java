package com.vibe.pojo;

import java.util.List;

import com.vibe.monitor.linkage.LinkageRuleCauseBean;
import com.vibe.monitor.linkage.LinkageRuleEffectBean;

public class LinkageRuleBean {

	private LinkageRuleCauseBean causeBean;
	private List<LinkageRuleEffectBean> effectBean;
	public LinkageRuleCauseBean getCauseBean() {
		return causeBean;
	}
	public void setCauseBean(LinkageRuleCauseBean causeBean) {
		this.causeBean = causeBean;
	}
	public List<LinkageRuleEffectBean> getEffectBean() {
		return effectBean;
	}
	public void setEffectBean(List<LinkageRuleEffectBean> effectBean) {
		this.effectBean = effectBean;
	}
	
	
	
}

package com.vibe.pojo;

import java.util.List;

public class LinkageRule {
	private LinkageCause linkageCause;
	private List<LinkageEffect> linkageEffects;
	public LinkageCause getLinkageCause() {
		return linkageCause;
	}
	public void setLinkageCause(LinkageCause linkageCause) {
		this.linkageCause = linkageCause;
	}
	public List<LinkageEffect> getLinkageEffects() {
		return linkageEffects;
	}
	public void setLinkageEffects(List<LinkageEffect> linkageEffects) {
		this.linkageEffects = linkageEffects;
	}

	
	
}

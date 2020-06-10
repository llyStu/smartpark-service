package com.vibe.controller;

import java.util.List;

import com.vibe.utils.EasyUIJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.monitor.linkage.LinkageBusiness;
import com.vibe.pojo.LinkageLogBean;
import com.vibe.pojo.LinkageRule;
import com.vibe.pojo.LinkageRuleBean;
import com.vibe.pojo.Response;
import com.vibe.service.linkage.LinkageService;
import com.vibe.util.ResponseResult;

@Controller
public class LinkageController {
	@Autowired
	private  LinkageService linkageService;


	@RequestMapping("/insertLinkageRule")
	@ResponseBody
	public Response insertUpdateTreeAlarmRule(@RequestBody LinkageRuleBean linkageRuleBean){
		try {
			linkageService.insertLinkageRule(linkageRuleBean);
			LinkageBusiness.linkageRuleCauseChanged.onChanged();
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
	}
	
	
	@RequestMapping("/queryLinkageLogListByTime")
	@ResponseBody
	public EasyUIJson queryLinkageLogListByTime(@RequestParam("pageNum") int pageNum,
												@RequestParam("pageCount") int pageCount, @RequestParam("start") String start, @RequestParam("end") String end){
		return linkageService.queryLinkageLogListByTime(pageNum, pageCount, start, end);
	}
	
	@RequestMapping("/queryLinkageList")
	@ResponseBody
	public List<LinkageRule> queryLinkageList(){
		return linkageService.queryLinkageList();
	}
	
	@RequestMapping("/deleteLinkage")
	@ResponseBody
	public Response deleteLinkage(@RequestParam("linkageArrStr") String linkageIds){
		try {
			linkageService.deleteLinkageRuleCause(linkageIds);
			LinkageBusiness.linkageRuleCauseChanged.onChanged();
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
		
	}
	
	@RequestMapping("/updateLinkage")
	@ResponseBody
	public Response updateLinkage(@RequestBody LinkageRuleBean linkageRuleBean){
		
		try {
			linkageService.updateLinkage(linkageRuleBean);
			LinkageBusiness.linkageRuleCauseChanged.onChanged();
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
	}
}

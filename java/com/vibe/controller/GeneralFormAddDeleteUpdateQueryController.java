package com.vibe.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.GeneralForm;
import com.vibe.pojo.Response;
import com.vibe.service.general.GeneralFormAddDeleteUpdateQueryService;
import com.vibe.util.ResponseResult;
@Controller
public class GeneralFormAddDeleteUpdateQueryController {
	
	@Autowired
	private GeneralFormAddDeleteUpdateQueryService service;

	@RequestMapping("/queryForms")
	@ResponseBody
	public List<GeneralForm> queryForms(@RequestParam("business") String business){
		return service.queryForms(business);
	}
	
	@RequestMapping("/deleteForms")
	@ResponseBody
	public Response deleteForm(List<GeneralForm> generalForms){
		try {
			service.deleteForm(generalForms);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
		
		
	}
	
	@RequestMapping("/insertForms")
	@ResponseBody
	public List<GeneralForm> insertForm(List<GeneralForm> generalForms){
		return service.insertForm(generalForms);
	}
	
	@RequestMapping("/updateForms")
	@ResponseBody
	public Response updateForm(List<GeneralForm> generalForms){
		try {
			service.updateForm(generalForms);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
		
	}
}

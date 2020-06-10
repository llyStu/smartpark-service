package com.vibe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.SmartWallData;
import com.vibe.service.smartwall.SmartWallService;

@Controller
public class SmartWallController {
	@Autowired
	private SmartWallService smartWallService;
	
	@RequestMapping("/smartWall")
	@ResponseBody
	public void smartWall(@RequestBody SmartWallData smartWallData){
		smartWallService.smartWall(smartWallData);
	}
	
}

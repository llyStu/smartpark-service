package com.vibe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.Response;
import com.vibe.util.ResponseResult;

@Controller
public class BackupController {
	
	@RequestMapping("/backup")
	@ResponseBody
	public Response backup(){
		
		return ResponseResult.getANewResponse(true);
	}
	
	@RequestMapping("/restore")
	@ResponseBody
	public Response restore(){
		
		return ResponseResult.getANewResponse(true);
	}
}

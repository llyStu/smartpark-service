package com.vibe.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.vibe.monitor.linkage.LinkageBusiness;
import com.vibe.pojo.MaintenaceDevicesBean;
import com.vibe.pojo.MaintenaceDevicesData;
import com.vibe.pojo.PageResult;
import com.vibe.pojo.Response;
import com.vibe.service.maintenace.MaintenaceService;
import com.vibe.util.PathTool;
import com.vibe.util.ResponseResult;

@Controller
public class MaintenaceController {
	
	@Autowired
	private MaintenaceService maintenaceService;
	
	@RequestMapping("/queryMaintenaceByDeviceId")
	public @ResponseBody PageResult<MaintenaceDevicesBean> queryMaintenaceByDeviceId(int deviceId, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int rows) {
		PageHelper.startPage(page, rows);		
		return maintenaceService.queryMaintenaceByDeviceId(deviceId);
	}
	
	@RequestMapping("/addMaintenace")
	public @ResponseBody Response addMaintenace(MaintenaceDevicesData maintenaceDevicesData,@RequestParam(required=false)MultipartFile[] photoFile,
			HttpServletRequest request) {
		try {
			if(photoFile != null){
				String pics = PathTool.getRelativePath(photoFile, request);
				maintenaceDevicesData.getMaintenaceBean().setPicture(pics);
			}
			maintenaceService.addMaintenace(maintenaceDevicesData);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
		
	}
	
	@RequestMapping("/updateMaintenace")
	public @ResponseBody Response updateMaintenace(MaintenaceDevicesData maintenaceDevicesData,@RequestParam(required=false)MultipartFile[] photoFile,
			HttpServletRequest request) {
		
		try {
			if(photoFile != null){
				String pics = PathTool.getRelativePath(photoFile, request);
				maintenaceDevicesData.getMaintenaceBean().setPicture(pics);
			}
			maintenaceService.updateMaintenace(maintenaceDevicesData);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
	}
	
	@RequestMapping("/deleteMaintenace")
	public @ResponseBody Response deleteMaintenace(String ids) {
		
		try {
			String[] ides = ids.split(",");
			for (String id : ides) {
				maintenaceService.deleteMaintenace(Integer.parseInt(id));
			}
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
	}
	
}

package com.vibe.controller.park;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.park.CameraRecognitionData;
import com.vibe.service.park.ParkCameraRecognitionService;
import com.vibe.utils.FormJson;
import com.vibe.utils.Page;

@Controller
public class ParkCameraRecognitionController {
	
	@Autowired
	private ParkCameraRecognitionService pcrs;
	
	@RequestMapping("park/findLimitCameraRecognitionlog")
	@ResponseBody
	public Page<CameraRecognitionData> cameraRecognitionLog(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int rows, CameraRecognitionData data){
		return pcrs.findCameraRecognitionLog(page,rows,data);
	}
	@RequestMapping("park/delCameraRecognitionlog")
	@ResponseBody
	public FormJson delCameraRecognitionlog(int[] ids){
		return pcrs.delCameraRecognitionlog(ids);
	}
	
	
}

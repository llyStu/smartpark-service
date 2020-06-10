package com.vibe.controller.safety;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.safety.SafetyMessage;
import com.vibe.service.safety.DoorInformationService;
import com.vibe.utils.Page;

@RestController
@RequestMapping("anfInformation")
public class DoorInformationController {
	
	@Autowired
	private DoorInformationService dis;
	
	@RequestMapping("/findAll_limit_door_record")
	public Page<SafetyMessage> findDoorRecord(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int rows, SafetyMessage message) {
		Page<SafetyMessage> doorMsgList = dis.findDoorRecord(page, rows, message);
		return doorMsgList;

	}
	@RequestMapping("/findAll_door_record")
	public List<SafetyMessage> findAllDoorRecord(SafetyMessage message) {
		List<SafetyMessage> doorMsgList = dis.findAllDoorRecord(message);
		return doorMsgList;

	}
}

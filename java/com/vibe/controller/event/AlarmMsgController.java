package com.vibe.controller.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.AlarmMessage;
import com.vibe.service.alarm.AlarmService;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.EasyUIJson;

@RestController
public class AlarmMsgController {


	@Autowired
	private  AlarmService alarmService;
	/**
	 * 查询报警信息列表
	 * @param page
	 * @param rows
	 * @param alarmMessage
	 * @return
	 */
	@RequestMapping("/alarm/queryAlarmMessages")
	@ResponseBody
	public ResponseModel<EasyUIJson> queryAlarmMessages(@RequestParam(defaultValue = "1") int page,
														@RequestParam(defaultValue = "10") int rows,@RequestBody AlarmMessage alarmMessage){
		EasyUIJson queryAlarmMessages = alarmService.queryAlarmMessages(page,rows,alarmMessage);
		return ResponseModel.success(queryAlarmMessages).code(ResultCode.SUCCESS);
	}

}

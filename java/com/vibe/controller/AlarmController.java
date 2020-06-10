package com.vibe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.AlarmMessage;
import com.vibe.pojo.AlarmMessageMap;
import com.vibe.pojo.InsertUpdateAlarmRuleData;
import com.vibe.pojo.Response;
import com.vibe.pojo.TreeAlarmData;
import com.vibe.service.alarm.AlarmService;
import com.vibe.util.ResponseResult;
import com.vibe.utils.EasyUIJson;

@Controller
public class AlarmController {
	@Autowired
	private  AlarmService alarmService;

	@RequestMapping("/queryAlarmMessageListByTime")
	@ResponseBody
	public EasyUIJson queryAlarmMessageListByTime(@RequestParam("pageNum") int pageNum,
												  @RequestParam("pageCount") int pageCount,@RequestParam("start") String start,@RequestParam("end") String end,@RequestParam(name="handled",defaultValue="0")int handled){
		return alarmService.queryAlarmMessageListByTime(pageNum,pageCount,start,end,handled);
	}

	@RequestMapping("/handleAlarmMessage")
	@ResponseBody
	public Response handleAlarmMessage(@RequestParam("id") int id){
		try {
			alarmService.handleAlarmMessage(id);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}
	}

	@RequestMapping("/treeAlarmRule")
	@ResponseBody
	public List<TreeAlarmData> getTreeAlarmRule(){
		return alarmService.getTreeAlarmRule();
	}

	@RequestMapping("/insertUpdateTreeAlarmRules")
	@ResponseBody
	public Response insertUpdateTreeAlarmRule(@RequestBody InsertUpdateAlarmRuleData insertUpdateAlarmRuleData){
		try {
			alarmService.insertUpdateTreeAlarmRule(insertUpdateAlarmRuleData);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}

	}

	@RequestMapping("/insertPersonAlarm")
	@ResponseBody
	public Response insertPersonAlarm(AlarmMessage alarmMessage){
		try {
			alarmService.insertPersonAlarm(alarmMessage);
			return ResponseResult.getANewResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseResult.getANewResponse(false);
		}

	}

	/*@RequestMapping("/updatePersonAlarm")
	@ResponseBody
	public Response updatePersonAlarm(AlarmMessage alarmMessage){

		return ResponseResult.getANewResponse();
	}
	@RequestMapping("/deletePersonAlarm")
	@ResponseBody
	public Response deletePersonAlarm(@RequestParam("id") int id){

		return ResponseResult.getANewResponse();
	}*/
	@RequestMapping("/queryAlarmMessageListByAssetId")
	@ResponseBody
	public AlarmMessageMap queryAlarmMessageListByAssetId(@RequestParam("pageNum") int pageNum,
														  @RequestParam(defaultValue = "10") int pageCount,@RequestParam("assetId") int assetId){
		PageHelper.startPage(pageNum, pageCount);
		List<AlarmMessage> alarmMessages = alarmService.queryAlarmMessageListByAssetId(pageNum,pageCount,assetId);
		AlarmMessageMap alarmMessageMap = new AlarmMessageMap();
		alarmMessageMap.setList(alarmMessages);
		alarmMessageMap.setSize((int) ((Page<AlarmMessage>)alarmMessages).getTotal());
		return alarmMessageMap;
	}

}

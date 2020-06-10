package com.vibe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.guardcente.CallbackGeneralMsg;
import com.vibe.service.guardcente.AlarmGuardCenteService;


@RestController
public class AlarmGuardCenteController {
	
	@Autowired
	private AlarmGuardCenteService alarmGuardCenteService;
	
	@RequestMapping(value="guardcente/acquireAlarm",method=RequestMethod.POST,consumes="application/json")
	public void acquireAlarm(@RequestBody CallbackGeneralMsg alarmGuardCenteMsg){
		//System.out.println(alarmGuardCenteMsg.toString());
		alarmGuardCenteService.acquireAlarm(alarmGuardCenteMsg);
	}
}

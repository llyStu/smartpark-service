package com.vibe.controller.auty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.auty.Abnormality;
import com.vibe.pojo.auty.ArrangInfo;
import com.vibe.pojo.auty.ArrangInfoConf;
import com.vibe.pojo.auty.ArrangInfoConfType;
//import com.vibe.pojo.auty.ArrayInfo;
import com.vibe.pojo.auty.Auty;
import com.vibe.pojo.auty.ChangeShifts;
import com.vibe.pojo.auty.ChangeShiftsGood;
import com.vibe.service.auty.AutyService;
import com.vibe.utils.FormJson;
import com.vibe.utils.Page;

@RestController
public class AutyController {
	@Autowired
	private AutyService am;
	

	
	@RequestMapping("/auty/findArrangInfoConfType")
	public List<ArrangInfoConfType> findArrangInfoConfType() {
		return am.findArrangInfoConfType();
	}
	
	@RequestMapping("/auty/insertArrangInfoConf")
	public FormJson insertArrangInfoConf(ArrangInfoConf info) {
		return am.insertArrangInfoConfAndSchedule(info);
	}
	@RequestMapping("/auty/deleteArrangInfoConf")
	public FormJson deleteArrangInfoConf(int[] ids) {
		return am.deleteArrangInfoConfAndSchedule(ids);
	}
	@RequestMapping("/auty/updateArrangInfoConf")
	public FormJson updateArrangInfoConf(ArrangInfoConf info) {
		return am.updateArrangInfoConfAndSchedule(info);
	}
	@RequestMapping("/auty/findArrangInfoConf")
	public Page<ArrangInfo> findArrangInfo(ArrangInfoConf info) {
		return am.findArrangInfoConf(info, info.getPageNum(), info.getPageSize());
	}
////新增
	@RequestMapping("/auty/insertArrangInfoConfType1")
	public FormJson insertArrangInfoConfType1(int id,String type,String[] startime,String[] endtime,String memo) {
		return am.insertArrangInfoConfType1(id,type, startime, endtime, memo);
	}
	@RequestMapping("/auty/deleteArrangInfoConfType1")
	public FormJson deleteArrangInfoConfType1(int[] ids) {
		return am.deleteArrangInfoConfType1(ids);
	}
	@RequestMapping("/auty/updateArrangInfoConfType1")
	public FormJson updateArrangInfoConfType1(ArrangInfoConf info) {
		return am.updateArrangInfoConfType1(info);
	}
	@RequestMapping("/auty/findArrangInfoConfType1")
	public Page<ArrangInfo> findArrangInfoType1(ArrangInfoConf info) {
		return am.findArrangInfoConfType1(info, info.getPageNum(), info.getPageSize());
	}
	
	
	
	@RequestMapping("/auty/insertArrangInfo")
	public FormJson insertArrangInfo(ArrangInfo info) {
		return am.insertArrangInfo(info);
	}
	@RequestMapping("/auty/deleteArrangInfo")
	public FormJson deleteArrangInfo(int[] ids) {
		return am.deleteArrangInfo(ids);
	}
	@RequestMapping("/auty/updateArrangInfo")
	public FormJson updateArrangInfo(ArrangInfo info) {
		return am.updateArrangInfo(info);
	}
	@RequestMapping("/auty/findArrangInfo")
	public Page<ArrangInfo> findArrangInfo(ArrangInfo info) {
		return am.findArrangInfo(info, info.getPageNum(), info.getPageSize());
	}
	
	@RequestMapping("/auty/insertAuty")
	public FormJson insertAuty(Auty info) {
		return am.insertAuty(info);
	}
	@RequestMapping("/auty/deleteAuty")
	public FormJson deleteAuty(int[] ids) {
		return am.deleteAuty(ids);
	}
	@RequestMapping("/auty/updateAuty")
	public FormJson updateAuty(Auty info) {
		return am.updateAuty(info);
	}
	@RequestMapping("/auty/findAuty")
	public Page<Auty> findAuty(Auty info) {
		return am.findAuty(info, info.getPageNum(), info.getPageSize());
	}
	
	@RequestMapping("/auty/insertAbnormality")
	public FormJson insertAbnormality(Abnormality info) {
		return am.insertAbnormality(info);
	}
	@RequestMapping("/auty/deleteAbnormality")
	public FormJson deleteAbnormality(int[] ids) {
		return am.deleteAbnormality(ids);
	}
	@RequestMapping("/auty/updateAbnormality")
	public FormJson updateAbnormality(Abnormality info) {
		return am.updateAbnormality(info);
	}
	@RequestMapping("/auty/findAbnormality")
	public Page<Abnormality> findAbnormality(Abnormality info) {
		return am.findAbnormality(info, info.getPageNum(), info.getPageSize());
	}
	
	@RequestMapping("/auty/insertChangeShifts")
	public FormJson insertChangeShifts(ChangeShifts info) {
		return am.insertChangeShifts(info);
	}
	@RequestMapping("/auty/deleteChangeShifts")
	public FormJson deleteChangeShifts(int[] ids) {
		return am.deleteChangeShifts(ids);
	}
	@RequestMapping("/auty/updateChangeShifts")
	public FormJson updateChangeShifts(ChangeShifts info) {
		return am.updateChangeShifts(info);
	}
	@RequestMapping("/auty/findChangeShifts")
	public Page<ChangeShifts> findChangeShifts(ChangeShifts info) {
		return am.findChangeShifts(info, info.getPageNum(), info.getPageSize());
	}
	
	@RequestMapping("/auty/insertChangeShiftsGood")
	public FormJson insertChangeShiftsGood(ChangeShiftsGood info) {
		return am.insertChangeShiftsGood(info);
	}
	@RequestMapping("/auty/deleteChangeShiftsGood")
	public FormJson deleteChangeShiftsGood(int[] ids) {
		return am.deleteChangeShiftsGood(ids);
	}
	@RequestMapping("/auty/updateChangeShiftsGood")
	public FormJson updateChangeShiftsGood(ChangeShiftsGood info) {
		return am.updateChangeShiftsGood(info);
	}
	@RequestMapping("/auty/findChangeShiftsGood")
	public Page<ChangeShiftsGood> findChangeShiftsGood(ChangeShiftsGood info) {
		return am.findChangeShiftsGood(info, info.getPageNum(), info.getPageSize());
	}
	
}

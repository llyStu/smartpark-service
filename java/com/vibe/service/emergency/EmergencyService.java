package com.vibe.service.emergency;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.emergency.EmergencyEventMapper;
import com.vibe.mapper.emergency.EmergencyUserMapper;
import com.vibe.pojo.emergency.EmergencyEventType;
import com.vibe.pojo.emergency.EmergencyUser;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Service
public class EmergencyService {
	
	@Autowired
	private EmergencyEventMapper eetm;
	
	@Autowired
	private EmergencyUserMapper eum;
	public List<EmergencyEventType> findAllEventType() {
		// TODO Auto-generated method stub
		return eetm.findAllEventType();
	}
	
	public List<EmergencyEventType> findAllEventGrade() {
		// TODO Auto-generated method stub
		return eetm.findAllEventGrade();
	}
	
	
	private static <T> Page<T> toPage(List<T> list) {
		PageInfo<T> page = new PageInfo<>(list);
		Page<T> result = new Page<>();
		result.setRows(list);
		result.setPage(page.getPageNum());
		result.setSize(page.getPageSize());
		result.setTotal((int) page.getTotal());
		return result;
	}

	public Page<EmergencyUser> findAllLimitUser(int page, int rows, EmergencyUser emergencyUser) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, rows);
		List<EmergencyUser> list=eum.findAllLimitUser(emergencyUser);
		return toPage(list);
	}

	public FormJson deleteLead(int[] ids) {
		// TODO Auto-generated method stub
		if (ids == null || ids.length == 0)
			 FormJsonBulider.success();
		int deleted = eum.deleteLeads(ids);
		if (deleted != ids.length) {
			return FormJsonBulider.fail("只删除了 " + deleted + " 条记录");
		}
		eum.deleteLeads(ids);
		return FormJsonBulider.success();
	}

	public int addLead(EmergencyUser emergencyUser) {
		// TODO Auto-generated method stub
		emergencyUser.setUdefault(0);
		  return eum.insertLead(emergencyUser);
	}

	public FormJson updateLead(EmergencyUser emergencyUser) {
		// TODO Auto-generated method stub
		if (null== emergencyUser.getId()) {
			return FormJsonBulider.fail("需要id");
		}
		if (eum.updateOneEmergencyLead(emergencyUser) != 1) {
			return FormJsonBulider.fail(null);
		}
		return FormJsonBulider.success();
	}
	public FormJson updateUdefault(EmergencyUser emergencyUser) {
		// TODO Auto-generated method stub
		if(null==emergencyUser.getId() || null==emergencyUser.getUdefault()||null==emergencyUser.getEtid()||null==emergencyUser.getEgid())
			return FormJsonBulider.fail("需要id和默认值");
		
		List<EmergencyUser> list = eum.findAllLimitUser(emergencyUser);
		if(list.size()>0){
			for (EmergencyUser eu: list) {
				eu.setUdefault(0);
				if(eum.updateOneLead(eu)!=1)
				return FormJsonBulider.fail("设置失败");
			}
		}	
		if(eum.updateOneLead(emergencyUser)!=1){
			return FormJsonBulider.fail("设为默认失败");
		}
		
		return FormJsonBulider.success();
	}

	public List<EmergencyUser> findAllTypeGradeOfLead(Integer etid, Integer egid) {
		// TODO Auto-generated method stub
		return eum.findAllTypeGradeOfLead(etid,egid);
	}

	

	
}

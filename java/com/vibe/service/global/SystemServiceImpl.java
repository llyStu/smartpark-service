package com.vibe.service.global;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.global.SystemDao;
import com.vibe.pojo.SystemSettingBean;

@Service
public class SystemServiceImpl implements SystemService {
	
	@Autowired
	private SystemDao systemDao;


	@Override
	public List<SystemSettingBean> getSystemSettings() {
		return systemDao.getSystemSettings();
	}
	
	
}

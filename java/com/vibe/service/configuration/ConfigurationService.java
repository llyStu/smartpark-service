package com.vibe.service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.configuration.ConfigurationMapper;

@Service
public class ConfigurationService {
	@Autowired
	private ConfigurationMapper cm;
	
	public String get(String key) {
		return cm.get(key);
	}
	
	public boolean set(String key, String value) {
		return cm.update(key, value) == 1 || cm.save(key, value) == 1;
	}
	
	public boolean remove(String key) {
		return cm.delete(key) == 1;
	}
}

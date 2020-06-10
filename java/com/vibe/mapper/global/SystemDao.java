package com.vibe.mapper.global;

import com.vibe.pojo.SystemSettingBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemDao {

	public List<SystemSettingBean> getSystemSettings();
}

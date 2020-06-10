package com.vibe.service.energy;

import java.util.List;

import com.vibe.pojo.KeyValueBean;

public interface EnergyService {
	List<KeyValueBean> fenxiangduibi(String timeType, String time, String start, String end);
	List<KeyValueBean> wangqitongji(String timeType, String time, String start, String end);
}

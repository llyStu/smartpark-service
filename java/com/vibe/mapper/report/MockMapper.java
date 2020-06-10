package com.vibe.mapper.report;

import com.vibe.pojo.report.Qspp;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MockMapper {
	public List<Qspp> qs(Map<?, ?> map) {
		List<Qspp> dataList = new ArrayList<Qspp>();
		dataList.add(new Qspp(new Date(), 1067, 1121, 483, 390, 2042, 1196, 779));
		dataList.add(new Qspp(new Date(), 1093, 1126, 599, 832, 2054, 1240, 778));
		dataList.add(new Qspp(new Date(), 2463, 2693, 569, 4426, 2481, 1986, 1021));
		dataList.add(new Qspp(new Date(), 2495, 2773, 568, 3838, 2542, 1943, 1249));
		dataList.add(new Qspp(new Date(), 2599, 2810, 587, 2791, 2914, 1954, 1198));
		dataList.add(new Qspp(new Date(), 2620, 2729, 594, 3705, 2990, 2023, 1201));
		
		return dataList;
	}
}

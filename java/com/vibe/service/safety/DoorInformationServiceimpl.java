package com.vibe.service.safety;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.safety.DoorInformationMapper;
import com.vibe.pojo.safety.SafetyMessage;
import com.vibe.utils.Page;

@Service
public class DoorInformationServiceimpl implements DoorInformationService {
	@Autowired
	private DoorInformationMapper dis;

	@Override
	public Page<SafetyMessage> findDoorRecord(int pageNum, int pageSize, SafetyMessage message) {
		PageHelper.startPage(pageNum, pageSize);
		List<SafetyMessage> list = dis.findDoorRecord(message);
		return toPage(list);
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

	@Override
	public List<SafetyMessage> findAllDoorRecord(SafetyMessage message) {
		return dis.findAllDoorRecord(message);
	}
}

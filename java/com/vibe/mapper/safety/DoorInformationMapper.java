package com.vibe.mapper.safety;

import com.vibe.pojo.safety.SafetyMessage;

import java.util.List;

public interface DoorInformationMapper {


	public List<SafetyMessage> findDoorRecord(SafetyMessage message);

	public List<SafetyMessage> findAllDoorRecord(SafetyMessage message);
	
}

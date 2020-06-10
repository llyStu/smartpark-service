package com.vibe.service.emergency;

import java.util.List;

import com.vibe.pojo.emergency.EmergencyEvent;
import com.vibe.pojo.emergency.Operation;

public interface IBMSClient {
	void init();

	String getName();

	void handle(List<Operation> operations, EmergencyEvent event, IBMSClient src);
}

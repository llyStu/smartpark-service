package com.vibe.service.dahua;

import com.vibe.pojo.dahua.ServerInfo;

public interface DahuaService {
	
	public ServerInfo getServer();
	public String getVisitor(int pageNum, int pageSize, String singleCondition, String status, ServerInfo info) throws Exception;
	public String getChargeInfo(int pageNum, int pageSize, String startTime, String endTime, String carNum,
                                String ownerType, String carType, String ownerName, Integer feeType, Integer operatorId, ServerInfo info);
	public String getParkinglotInfo(String parkingLotCode, String parkingLot, ServerInfo info);
}

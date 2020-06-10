package com.vibe.parse;

import java.util.List;

public class BacnetServiceParser extends BaseServiceParser<BacnetService>{
	
	public static final String OUPUT_FILE_NAME = "init_bacnet_service.sql";
	
	public static final int IP_INDEX = 3;
	public static final int PORT_INDEX = 4;
	public static final int DEVICE_ID_INDEX = 5;
	
	@Override
	public BacnetService createBean() {
		// TODO Auto-generated method stub
		return new BacnetService();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}


	@Override
	public void fillUniqueData(List<String> excelData, BacnetService data) {
		// TODO Auto-generated method stub
		data.setIp(excelData.get(IP_INDEX));
		data.setPort(ExcelUtil.rvZeroAndDot(excelData.get(PORT_INDEX)));
		data.setDeviceId(ExcelUtil.rvZeroAndDot(excelData.get(DEVICE_ID_INDEX)));
	}


}

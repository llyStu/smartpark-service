package com.vibe.parse;

import java.util.List;

public class BacnetMonitorParser extends BaseMonitorParser<BacnetMonitor> {

	public static final String OUPUT_FILE_NAME = "init_bacnet_monitor.sql";
	
	public static final int OBJECT_TYPE_INDEX = 11;
	public static final int INSTANCE_NUMBER_INDEX = 12;
	

	@Override
	public BacnetMonitor createBean() {
		// TODO Auto-generated method stub
		return new BacnetMonitor();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}

	@Override
	public void fillUniqueData(List<String> excelData, BacnetMonitor data) {
		// TODO Auto-generated method stub
		data.setObjectType(ExcelUtil.rvZeroAndDot(excelData.get(OBJECT_TYPE_INDEX)));
		data.setInstanceNumber(ExcelUtil.rvZeroAndDot(excelData.get(INSTANCE_NUMBER_INDEX)));
	}

	@Override
	public List<BaseService> getServices() {
		// TODO Auto-generated method stub
		return App.bacnetServiceParser.getServices();
	}


}

package com.vibe.parse;

import java.util.List;

public class IEC104MonitorParser extends BaseMonitorParser<IEC104Monitor> {

	public static final String OUPUT_FILE_NAME = "init_iec104_monitor.sql";
	
	public static final int ADDRESS_TYPE_INDEX = 11;
	public static final int COMMON_ADDR_TYPE_INDEX = 12;
	public static final int PARENT_MERER_TYPE_INDEX = 13;
	public static final int GRADE_TYPE_INDEX = 14;
	public static final int METER_TYPE_TYPE_INDEX = 15;
	public static final int ITEMIZE_TYPE_INDEX = 16;
	
	@Override
	public void fillUniqueData(List<String> excelData, IEC104Monitor data) {
		// TODO Auto-generated method stub
		data.setAddress(ExcelUtil.rvZeroAndDot(excelData.get(ADDRESS_TYPE_INDEX)));
		data.setCommonAddr(ExcelUtil.rvZeroAndDot(excelData.get(COMMON_ADDR_TYPE_INDEX)));
		data.setParent_meter(ExcelUtil.rvZeroAndDot(excelData.get(PARENT_MERER_TYPE_INDEX)));
		data.setGrade(ExcelUtil.rvZeroAndDot(excelData.get(GRADE_TYPE_INDEX)));
		data.setMeter_type(ExcelUtil.rvZeroAndDot(excelData.get(METER_TYPE_TYPE_INDEX)));
		data.setItemize_type(ExcelUtil.rvZeroAndDot(excelData.get(ITEMIZE_TYPE_INDEX)));
	}

	@Override
	public List<BaseService> getServices() {
		// TODO Auto-generated method stub
		return App.iec104ServiceParser.getServices();
	}

	@Override
	public BaseMonitorBean createBean() {
		// TODO Auto-generated method stub
		return new IEC104Monitor();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}

}

package com.vibe.parse;

import java.util.List;

public class OpcServiceParser extends BaseServiceParser<OpcService>{
	
	public static final String OUPUT_FILE_NAME = "init_opc_service.sql";
	
	public static final int IP_INDEX = 3;
	public static final int USERNAME_INDEX = 4;
	public static final int PASSWORD_INDEX = 5;
	public static final int CLSID_INDEX = 6;
	public static final int PROGID_INDEX = 7;
	
	@Override
	public ModbusService createBean() {
		// TODO Auto-generated method stub
		return new ModbusService();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}

	@Override
	public void fillUniqueData(List<String> excelData, OpcService data) {
		// TODO Auto-generated method stub
		data.setIp(excelData.get(IP_INDEX));
		data .setUsername(excelData.get(USERNAME_INDEX));
		data.setPassword(ExcelUtil.rvZeroAndDot(excelData.get(PASSWORD_INDEX)));
		data.setClsid(excelData.get(CLSID_INDEX));
		data.setProgId(excelData.get(PROGID_INDEX));
	}

}

package com.vibe.parse;

import java.util.List;

public class CameraDeviceParser extends BaseDeviceParser<CameraDevice> {

	public static final String OUPUT_FILE_NAME = "init_camera_device.sql";

	public static final int HOST_INDEX = 4;
	public static final int PORT_INDEX = 5;
	public static final int USERNAME_INDEX = 6;
	public static final int PASSWORD_INDEX = 7;
	

	@Override
	public CameraDevice createBean() {
		// TODO Auto-generated method stub
		return new CameraDevice();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}

	@Override
	public void fillUniqueData(List<String> excelData, CameraDevice data) {
		// TODO Auto-generated method stub
		data.setHost(excelData.get(HOST_INDEX));
		data.setPort(ExcelUtil.rvZeroAndDot(excelData.get(PORT_INDEX)));
		data.setUsername(excelData.get(USERNAME_INDEX));
		data.setPassword(ExcelUtil.rvZeroAndDot(excelData.get(PASSWORD_INDEX)));
	}


}

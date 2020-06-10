package com.vibe.parse;

import java.util.List;

public class AtzgbServiceParser extends BaseServiceParser<AtzgbService>{

	public static final String OUPUT_FILE_NAME = "init_atzgb_service.sql";
	
	@Override
	public void fillUniqueData(List<String> excelData, AtzgbService data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseService createBean() {
		// TODO Auto-generated method stub
		return new AtzgbService();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}

}

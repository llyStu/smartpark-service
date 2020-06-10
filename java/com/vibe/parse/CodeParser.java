package com.vibe.parse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CodeParser extends BaseParser<Code>{

	public static final String OUPUT_FILE_NAME = "init_code.sql";

	public static final int ID_INDEX = 0;
	public static final int NAME_INDEX = 1;
	public static final int PARENT_INDEX = 2;
	public static final int CATALOG_INDEX = 3;
	public static final int UNIT_INDEX = 4;
	
	@Override
	public Code createBean() {
		// TODO Auto-generated method stub
		return new Code();
	}

	@Override
	public String getOutputFileName() {
		// TODO Auto-generated method stub
		return OUPUT_FILE_NAME;
	}

	@Override
	public void fillData(List<String> excelData, Code data) {
		// TODO Auto-generated method stub
		data.setName(excelData.get(NAME_INDEX));
		data.setId(ExcelUtil.rvZeroAndDot(excelData.get(ID_INDEX)));
		data.setParent(ExcelUtil.rvZeroAndDot(excelData.get(PARENT_INDEX)));
		data.setCatalog(ExcelUtil.rvZeroAndDot(excelData.get(CATALOG_INDEX)));
		data.setUnit(excelData.get(UNIT_INDEX));
	}

	@Override
	public void setParent(List<List<String>> excelData, List<Code> data) {
		// TODO Auto-generated method stub
		// 这里不需要做任何操作，因为code的parent从Excel获取
	}

	@Override
	public void setId(Code t) {
		// 这里覆盖默认的，因为code的id从Excel获取
	}

	@Override
	public void setDatabase(PrintWriter bw) throws IOException {
		// TODO Auto-generated method stub
		bw.print("use db_vibe_basic;");
	}
	
	

}

package com.vibe.parse;

import java.util.List;

public abstract class BaseServiceParser<T extends BaseService> extends BaseParser<BaseService> {
	
	public static final int NAME_INDEX = 0;
	public static final int CAPTION_INDEX = 1;
	public static final int PARENT_INDEX = 2;
	

	@SuppressWarnings("unchecked")
	@Override
	public void fillData(List<String> excelData, BaseService data) {
		// TODO Auto-generated method stub
		data.setName(excelData.get(NAME_INDEX));
		data.setCaption(excelData.get(CAPTION_INDEX));
		data.setParent(ExcelUtil.rvZeroAndDot(excelData.get(PARENT_INDEX)));
		
		fillUniqueData(excelData,(T) data);
	}

	public abstract void fillUniqueData(List<String> excelData, T data);
	
	public List<BaseService> baseServices;
	
	@Override
	public void setParent(List<List<String>> excelData, List<BaseService> data) {
		// TODO Auto-generated method stub
		baseServices = data;
	}

	public List<BaseService> getServices() {
		return baseServices;
	}

}

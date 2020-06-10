package com.vibe.parse;

import java.util.List;

public abstract class BaseDeviceParser<T extends Device> extends BaseParser<Device> {

	public static final int NAME_INDEX = 0;
	public static final int CAPTION_INDEX = 1;
	public static final int PARENT_INDEX = 2;
	public static final int CATALOG_INDEX = 3;
	
	public static List<Device> devices;

	
	public abstract void fillUniqueData(List<String> excelData, T data);

	@SuppressWarnings("unchecked")
	@Override
	public void fillData(List<String> excelData, Device data) {
		// TODO Auto-generated method stub
		data.setName(excelData.get(NAME_INDEX));
		data.setCaption(excelData.get(CAPTION_INDEX));
		data.setCatalog(ExcelUtil.rvZeroAndDot(excelData.get(CATALOG_INDEX)));
		
		fillUniqueData(excelData, (T) data);
	}

	@Override
	public void setParent(List<List<String>> excelData, List<Device> data) {
		// TODO Auto-generated method stub
		devices = data;
		for (int i = 0; i < excelData.size(); i++) {
			String parent = excelData.get(i).get(PARENT_INDEX);
			boolean found = false;
			for (int j = 0; j < data.size(); j++) {
				if (data.get(j).getName().equals(parent)) {
					found = true;
					data.get(i).setParent(data.get(j).getId());
					break;
				}
			}
			if(!found){
				for (int j = 0; j < SpaceParser.spaces.size(); j++) {
					if (SpaceParser.spaces.get(j).getName().equals(parent)) {
						found = true;
						data.get(i).setParent(SpaceParser.spaces.get(j).getId());
						break;
					}
				}
			}
		}
	}

}

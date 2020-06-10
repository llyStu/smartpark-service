package com.vibe.parse;

import java.util.List;

public abstract class BaseMonitorParser<T extends BaseMonitorBean> extends BaseParser<BaseMonitorBean> {

	public static final int NAME_INDEX = 0;
	public static final int CAPTION_INDEX = 1;
	public static final int PARENT_INDEX = 2;
	public static final int CATALOG_INDEX = 3;
	public static final int IO_INDEX = 4;
	public static final int SOURCE_INDEX = 5;
	public static final int TIME_INTERVAL_INDEX = 6;
	public static final int TRANSFORM_INDEX = 7;
	public static final int ALARM_INDEX = 8;
	public static final int MIN_INDEX = 9;
	public static final int MAX_INDEX = 10;

	@SuppressWarnings("unchecked")
	@Override
	public void fillData(List<String> excelData, BaseMonitorBean data) {
		// TODO Auto-generated method stub
		data.setName(excelData.get(NAME_INDEX));
		data.setCaption(excelData.get(CAPTION_INDEX));
		data.setCatalog(ExcelUtil.rvZeroAndDot(excelData.get(CATALOG_INDEX)));
		data.setIo(excelData.get(IO_INDEX));
		data.setSource(excelData.get(SOURCE_INDEX));
		data.setAlarm(excelData.get(ALARM_INDEX));
		data.setTimeInterval(excelData.get(TIME_INTERVAL_INDEX));
		data.setTransform(excelData.get(TRANSFORM_INDEX));
		data.setMin(ExcelUtil.rvZeroAndDot(excelData.get(MIN_INDEX)));
		data.setMax(ExcelUtil.rvZeroAndDot(excelData.get(MAX_INDEX)));
		fillUniqueData(excelData, (T) data);
	}

	public abstract void fillUniqueData(List<String> excelData, T data);

	@Override
	public void setParent(List<List<String>> excelData, List<BaseMonitorBean> data) {
		// TODO Auto-generated method stub
		for (int i = 0; i < excelData.size(); i++) {
			String parent = excelData.get(i).get(PARENT_INDEX);
			boolean found = false;
			if (GenericDeviceParser.devices != null) {
				for (int j = 0; j < GenericDeviceParser.devices.size(); j++) {
					if (GenericDeviceParser.devices.get(j).getName().equals(parent)) {
						found = true;
						data.get(i).setParent(GenericDeviceParser.devices.get(j).getId());
						break;
					}
				}
			}

			if (!found) {
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
	
	public abstract List<BaseService> getServices();

	@Override
	public void setSource(List<List<String>> excelData, List<BaseMonitorBean> data) {
		// TODO Auto-generated method stub
		for (int i = 0; i < excelData.size(); i++) {
			String source = excelData.get(i).get(SOURCE_INDEX);
			for (int j = 0; j < getServices().size(); j++) {
				if (getServices().get(j).getName().equals(source)) {
					data.get(i).setSource(getServices().get(j).getId());
					break;
				}
			}

		}
	}

}

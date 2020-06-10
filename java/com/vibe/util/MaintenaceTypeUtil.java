package com.vibe.util;

import java.util.ArrayList;
import java.util.List;

import com.vibe.pojo.SelectKeyValue;

public class MaintenaceTypeUtil {
	//维护类型或者维护原因 0巡检，1维修，2维保， 3清洁
	private static List<SelectKeyValue> keyValues = new ArrayList<>();
	static{
		//keyValues.add(new SelectKeyValue(0, "巡检"));
		keyValues.add(new SelectKeyValue(1, "维修"));
		keyValues.add(new SelectKeyValue(2, "维保"));
		keyValues.add(new SelectKeyValue(3, "清洁"));
	}
	public static String getTypeStr(int type) {
		for (SelectKeyValue selectKeyValue : keyValues) {
			if(selectKeyValue.getId() == type){
				return selectKeyValue.getText();
			}
		}
		return null;
	}
	public static List<SelectKeyValue> getKeyValues() {
		return keyValues;
	}
	
	
}

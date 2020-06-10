package com.vibe.service.energy;

import java.util.List;

public interface EnergyServiceTime {
	
	List<?> searchByTime(String timeType, String time);
	List<?> searchByStartEndTime(String timeType, String start, String end);
}

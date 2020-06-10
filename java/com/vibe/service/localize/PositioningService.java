package com.vibe.service.localize;

import java.util.List;

import com.vibe.pojo.ApLocation;
import com.vibe.pojo.user.UserLocationData;

public interface PositioningService {
	public List<ApLocation> queryApLocation();
	public void uploadUserLocation(String username, String mac, String longitude, String latitude, String time);
	public List<UserLocationData> getUsersCurrentLocation();
	public List<UserLocationData> getUserLocation(String username, String start, String end);
}

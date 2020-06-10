package com.vibe.mapper.localize;

import com.vibe.pojo.ApLocation;
import com.vibe.pojo.user.UserLocationData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositioningDao {
	public List<ApLocation> queryApLocation();
	public void uploadUserLocation(@Param("username") String username, @Param("mac") String mac, @Param("longitude") String longitude, @Param("latitude") String latitude, @Param("time") String time);
	public List<UserLocationData> getUsersCurrentLocation();
	public List<UserLocationData> getUserLocation(@Param("username") String username, @Param("start") String start, @Param("end") String end);
}

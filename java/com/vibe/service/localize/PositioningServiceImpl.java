package com.vibe.service.localize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.localize.PositioningDao;
import com.vibe.pojo.ApLocation;
import com.vibe.pojo.user.UserLocationData;

@Service
public class PositioningServiceImpl implements PositioningService {
    @Autowired
    private PositioningDao positioningDao;

    @Override
    public List<ApLocation> queryApLocation() {
        // TODO Auto-generated method stub
        return positioningDao.queryApLocation();
    }

    @Override
    public void uploadUserLocation(String username, String mac, String longitude, String latitude, String time) {
        // TODO Auto-generated method stub
        positioningDao.uploadUserLocation(username, mac, longitude, latitude, time);
    }

    @Override
    public List<UserLocationData> getUsersCurrentLocation() {
        // TODO Auto-generated method stub
        List<UserLocationData> datas = positioningDao.getUsersCurrentLocation();
        for (UserLocationData userLocationData : datas) {
            userLocationData.setTime(userLocationData.getTime().substring(0, userLocationData.getTime().length() - 2));
        }
        return datas;
    }

    @Override
    public List<UserLocationData> getUserLocation(String username, String start, String end) {
        // TODO Auto-generated method stub
        return positioningDao.getUserLocation(username, start, end);
    }

}

package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.scene.TouristDao;
import com.vibe.pojo.Tourist;

@Service
public class TouristServiceImpl implements TouristService {

    @Autowired
    private TouristDao touristDao;

    public void insertTourist(Tourist tourist) {

        touristDao.insertTourist(tourist);

    }

    public void deleteTourist(int id) {

        touristDao.deleteTourist(id);
    }

    public Tourist queryTourist(int id) {

        Tourist d = touristDao.queryTourist(id);
        //return TouristDao.queryTourist(id);
        return d;
    }

    @Override
    public List<Tourist> queryTouristList() {

        return touristDao.queryTouristList();
    }

    @Override
    public void updateTourist(Tourist tourist) {

        touristDao.updateTourist(tourist);
    }

}

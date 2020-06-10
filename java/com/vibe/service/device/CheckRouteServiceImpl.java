package com.vibe.service.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.dailycheck.CheckRouteDao;
import com.vibe.pojo.CheckRoute;
import com.vibe.service.dailycheck.CheckRouteService;

@Service
public class CheckRouteServiceImpl implements CheckRouteService {

    @Autowired
    private CheckRouteDao checkRouteDao;

    @Override
    public void insert(CheckRoute checkRoute) {
        // TODO Auto-generated method stub
        checkRouteDao.insert(checkRoute);
    }

    @Override
    public Integer queryMaxId() {
        // TODO Auto-generated method stub
        return checkRouteDao.queryMaxId();
    }

    @Override
    public List<CheckRoute> queryForList(int orderId) {
        // TODO Auto-generated method stub
        return checkRouteDao.queryForList(orderId);
    }

}

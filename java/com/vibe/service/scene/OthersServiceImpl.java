package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.scene.OthersDao;
import com.vibe.pojo.Others;

@Service
public class OthersServiceImpl implements OthersService {

    @Autowired
    private OthersDao othersDao;

    public void insertOthers(Others others) {

        othersDao.insertOthers(others);

    }

    public void deleteOthers(int id) {

        othersDao.deleteOthers(id);
    }

    public Others queryOthers(int id) {

        Others d = othersDao.queryOthers(id);
        //return othersDao.queryOthers(id);
        return d;
    }

    @Override
    public List<Others> queryOthersList() {

        return othersDao.queryOthersList();
    }

    @Override
    public void updateOthers(Others others) {

        othersDao.updateOthers(others);
    }

}

package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.scene.ImplimentationDao;
import com.vibe.pojo.Implimentation;

@Service
public class ImplimentationServiceImpl implements ImplimentationService {

    @Autowired
    private ImplimentationDao implimentationDao;

    public void insertImplimentation(Implimentation implimentation) {

        implimentationDao.insertImplimentation(implimentation);

    }

    public void deleteImplimentation(int id) {

        implimentationDao.deleteImplimentation(id);
    }

    public Implimentation queryImplimentation(int id) {

        Implimentation d = implimentationDao.queryImplimentation(id);
        //return ImplimentationDao.queryImplimentation(id);
        return d;
    }

    @Override
    public List<Implimentation> queryImplimentationList() {

        return implimentationDao.queryImplimentationList();
    }

    @Override
    public void updateImplimentation(Implimentation implimentation) {

        implimentationDao.updateImplimentation(implimentation);
    }

}

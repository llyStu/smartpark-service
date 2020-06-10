package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.scene.DamageDao;
import com.vibe.pojo.Damage;

@Service
public class DamageServiceImpl implements DamageService {

    @Autowired
    private DamageDao damageDao;

    public void insertDamage(Damage damage) {

        damageDao.insertDamage(damage);

    }

    public void deleteDamage(int id) {

        damageDao.deleteDamage(id);
    }

    public Damage queryDamage(int id) {

        Damage d = damageDao.queryDamage(id);
        //return damageDao.queryDamage(id);
        return d;
    }

    @Override
    public List<Damage> queryDamageList() {

        return damageDao.queryDamageList();
    }

    @Override
    public void updateDamage(Damage damage) {

        damageDao.updateDamage(damage);
    }

}

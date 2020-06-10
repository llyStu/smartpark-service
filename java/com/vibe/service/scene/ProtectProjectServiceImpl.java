package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.scene.ProtectProjectDao;
import com.vibe.pojo.ProtectProject;


@Service("protectProjectService")
public class ProtectProjectServiceImpl implements ProtectProjectService {

    @Autowired
    private ProtectProjectDao protectProjectDao;

    @Override
    public void insertProtectProject(ProtectProject protectProject) {
        // TODO Auto-generated method stub
        protectProjectDao.insertProtectProject(protectProject);
    }

    @Override
    public void deleteProtectProject(int id) {
        // TODO Auto-generated method stub
        protectProjectDao.deleteProtectProject(id);
    }

    @Override
    public void updateProtectProject(ProtectProject protectProject) {
        // TODO Auto-generated method stub
        protectProjectDao.updateProtectProject(protectProject);
    }

    @Override
    public List<ProtectProject> listProtectProject() {
        // TODO Auto-generated method stub
        return protectProjectDao.listProtectProject();
    }

    @Override
    public ProtectProject queryProtectProject(int id) {
        // TODO Auto-generated method stub
        return protectProjectDao.queryProtectProject(id);
    }

}

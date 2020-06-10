package com.vibe.mapper.scene;

import com.vibe.pojo.ProtectProject;

import java.util.List;

public interface ProtectProjectDao {

    void deleteProtectProject(Integer id);

    void insertProtectProject(ProtectProject protectProject);

    List<ProtectProject> listProtectProject();

    ProtectProject queryProtectProject(Integer id);

    void updateProtectProject(ProtectProject protectProject);

}
package com.vibe.service.scene;

import java.util.List;

import com.vibe.pojo.BuildingsPhoto;
import com.vibe.pojo.UnionPolling;

public interface BuildingsPhotoService {

    public void insertBuildingsPhoto(BuildingsPhoto buildingsPhoto);

    public void deleteBuildingsPhoto(int id);

    public void updateBuildingsPhoto(BuildingsPhoto buildingsPhoto);

    public List<BuildingsPhoto> listBuildingsPhoto();

    public BuildingsPhoto queryBuildingsPhoto(int id);

    public List<UnionPolling> queryUnionPollingList();
}

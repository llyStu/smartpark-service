package com.vibe.mapper.scene;

import com.vibe.pojo.BuildingsPhoto;
import com.vibe.pojo.UnionPolling;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingsPhotoDao {
	
	//增
	public void addBuildingsPhoto(BuildingsPhoto buildingsPhoto);
	//删
	public void delBuildingsPhoto(int id);
	//改
	public void updBuildingsPhoto(BuildingsPhoto buildingsPhoto);
	//查询列表
	public List<BuildingsPhoto> lisBuildingsPhoto();
	//查询单个
	public BuildingsPhoto queBuildingsPhoto(int id);
	
	public List<UnionPolling> lisUnionPolling();

}

package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.scene.BuildingsPhotoDao;
import com.vibe.pojo.BuildingsPhoto;
import com.vibe.pojo.UnionPolling;

@Service("buildingsPhotoService")
public class BuildingsPhotoServiceImpl implements BuildingsPhotoService {

    @Autowired
    private BuildingsPhotoDao buildingsPhotoDao;

    @Override
    public void insertBuildingsPhoto(BuildingsPhoto buildingsPhoto) {
        // TODO Auto-generated method stub
        buildingsPhotoDao.addBuildingsPhoto(buildingsPhoto);
    }

    @Override
    public void deleteBuildingsPhoto(int id) {
        // TODO Auto-generated method stub
        buildingsPhotoDao.delBuildingsPhoto(id);
    }

    @Override
    public void updateBuildingsPhoto(BuildingsPhoto buildingsPhoto) {
        // TODO Auto-generated method stub
        buildingsPhotoDao.updBuildingsPhoto(buildingsPhoto);
    }

    @Override
    public List<BuildingsPhoto> listBuildingsPhoto() {
        // TODO Auto-generated method stub
        return buildingsPhotoDao.lisBuildingsPhoto();
    }

    @Override
    public BuildingsPhoto queryBuildingsPhoto(int id) {
        // TODO Auto-generated method stub
        BuildingsPhoto buildingsPhoto = buildingsPhotoDao.queBuildingsPhoto(id);
        //数据库中没有photo2字段，需对查出的结果为photo2属性手动赋值
        //行不通！服务器中保存了相对路径，此处拿不到request，不能得到绝对路径
		/*String[]arr = buildingsPhoto.getPhoto().split(",");
		List<String> list = new ArrayList<String>();
		for(int i=0;i<arr.length;i++){
			list.add(arr[i]);
		}
		buildingsPhoto.setPhoto2(list);*/
        return buildingsPhoto;
    }

    @Override
    public List<UnionPolling> queryUnionPollingList() {
        // TODO Auto-generated method stub
        return buildingsPhotoDao.lisUnionPolling();
    }

}

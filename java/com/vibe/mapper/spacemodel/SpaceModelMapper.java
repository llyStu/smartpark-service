package com.vibe.mapper.spacemodel;

import com.vibe.parse.DeviceTree;
import com.vibe.pojo.spacemodel.SceneAsset;
import com.vibe.pojo.spacemodel.SpaceModelName;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SpaceModelMapper {
	List<SpaceModelName> findSpaceModelName(SpaceModelName vo);
	
	List<SceneAsset> findSceneAssetByCatalogAndScene(@Param("cas") List<Integer> cas, @Param("scene") String scene);
	
	List<SceneAsset> findSceneAssetByScene(SceneAsset scene);

	int updateSceneAsset(SceneAsset sceneAsset);

	int insertSceneAsset(SceneAsset sceneAsset);

	List<String> findSpaceModelFile(SpaceModelName vo);

	int deleteSpaceModelFile(@Param("filenames") String[] filenames);
	int deleteSceneAsset(@Param("saids") int[] saids);
	
	Set<Integer> findSceneAssetId();

	//查询与scene_asset数据库有关联的设备信息
	SceneAsset selectSceneAssetByAssetId(@Param("assetId") Integer assetId);

	List<DeviceTree> selectDeviceByRelationSceneAsset(@Param("idList") List<Integer> idList);
}

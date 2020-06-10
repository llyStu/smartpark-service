package com.vibe.service.asset;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.common.LoginSuccessCallback;
import com.vibe.mapper.spacemodel.SpaceModelMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;

@Service
public class AssetBinding implements LoginSuccessCallback{
	@Autowired
	private SpaceModelMapper spaceModelMapper;

	private Set<Integer> assetBinding;


	@Override
	public void success() {
		// TODO Auto-generated method stub
		assetBinding = spaceModelMapper.findSceneAssetId();
	}
	public Set<Integer> getAssetBinding() {
		return assetBinding;
	}
	
	public boolean bind(Asset<?> asset){
		if(asset.getKind() != AssetKind.SERVICE){
			while (asset.getKind()!=AssetKind.SPACE) {
				if(assetBinding.contains(asset.getId())){
					return true;
				}
				asset = asset.getParent();
			}
		}
		return false;
	}
	
}

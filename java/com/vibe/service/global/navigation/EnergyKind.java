package com.vibe.service.global.navigation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vibe.utils.TreeNode;

@Service
public class EnergyKind implements NavigationData {
	
	@Override
	public List<TreeNode> loadList() {
		return Navigation.getNavigationService().getEnergyKindList();
	}

}

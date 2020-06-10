package com.vibe.service.global.navigation;

import java.util.List;
import org.springframework.stereotype.Service;

import com.vibe.utils.TreeNode;

@Service
public class SpaceTree implements NavigationData{
	
	@Override
	public List<TreeNode> loadList() {
		List<TreeNode> spaceTree = Navigation.getNavigationService().getAllSpaceTree(0);
		return spaceTree;
	}
	
	public static void main(String[] args) {
		String ss = "aa.dd.cc.ee";
		int indexOf = ss.lastIndexOf(".");
		String substring = ss.substring(indexOf+1);
		System.out.println(substring);
	}
}

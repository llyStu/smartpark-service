package com.vibe.service.global.navigation;

import java.util.List;

import com.vibe.utils.TreeNode;

public interface NavigationService {

    public List<TreeNode> getAllSpaceTree(int id);

    public List<TreeNode> getAllElectorList(int i);

    public List<TreeNode> getEnergyKindList();
}

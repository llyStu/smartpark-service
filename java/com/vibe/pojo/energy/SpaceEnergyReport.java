package com.vibe.pojo.energy;

import java.util.List;

import com.vibe.utils.TreeNode;

public class SpaceEnergyReport {
    private TreeNode treeNode;
    private List<SpaceEnergyReportData> nodes;

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public List<SpaceEnergyReportData> getNodes() {
        return nodes;
    }

    public void setNodes(List<SpaceEnergyReportData> nodes) {
        this.nodes = nodes;
    }
}

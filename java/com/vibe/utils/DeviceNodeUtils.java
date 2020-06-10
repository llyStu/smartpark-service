package com.vibe.utils;

import com.vibe.parse.DeviceTree;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类名称
 * @description 设备树工具类
 * @author hyd132@126.com
 * @create 2020/05/27
 * @module 智慧园区
 */
public class DeviceNodeUtils {

    public static List<DeviceTree> build(List<DeviceTree> treeNodes) {

        List<DeviceTree> trees = new ArrayList<DeviceTree>();

        for (DeviceTree treeNode : treeNodes) {
            String parentId = String.valueOf(treeNode.getParent());
            if ("0".equals(parentId)) {
                trees.add(treeNode);
            }

            for (DeviceTree it : treeNodes) {
                String parentIds = String.valueOf(it.getParent());
                String id = String.valueOf(treeNode.getId());
                if (parentIds.equals(id)) {
                    if (treeNode.getDeviceTreeList() == null) {
                        treeNode.setDeviceTreeList(new ArrayList<DeviceTree>());
                    }
                    treeNode.getDeviceTreeList().add(it);
                }
            }
        }
        return trees;
    }
    /*public static List<TreeNode> bulid(List<TreeNode> treeNodes) {

        List<TreeNode> trees = new ArrayList<TreeNode>();

        for (TreeNode treeNode : treeNodes) {
            String parentId = String.valueOf(treeNode.getParentId());
            if ("0".equals(parentId)) {
                trees.add(treeNode);
            }

            for (TreeNode it : treeNodes) {
                String parentIds = String.valueOf(it.getParentId());
                String id = String.valueOf(treeNode.getId());
                if (parentIds.equals(id)) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }*/
}
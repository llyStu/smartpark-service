package com.vibe.utils;

import com.vibe.pojo.energy.Energy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: EnergyTreeNode
 * Package:com.vibe.utils
 * Description:
 *
 * @Date:2019/12/13 11:23
 * @Author: hyd132@136.com
 */
public class EnergyTreeNode {

    public static List<Energy> build(List<Energy> treeNodes) {

        List<Energy> trees = new ArrayList<Energy>();
        if (null == treeNodes || treeNodes.size() <= 0){
            return treeNodes;
        }
        for (Energy treeNode : treeNodes) {
            String parentId = String.valueOf(treeNode.getParentId());
            if (StringUtils.equals("0",parentId)) {
                trees.add(treeNode);
            }
            for (Energy it : treeNodes) {
                String parentIds = String.valueOf(it.getParentId());
                String floorId = String.valueOf(treeNode.getFloorId());
                if (StringUtils.equals(parentIds,floorId)) {
                    if (treeNode.getNodes() == null) {
                        treeNode.setNodes(new ArrayList<Energy>());
                    }
                    treeNode.getNodes().add(it);
                }
            }
        }
        return trees;
    }

}
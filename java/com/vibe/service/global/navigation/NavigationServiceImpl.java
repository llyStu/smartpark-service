package com.vibe.service.global.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.global.NavigationMapper;
import com.vibe.monitor.asset.Device;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.service.classification.Code;
import com.vibe.utils.TreeNode;

@Service
public class NavigationServiceImpl implements NavigationService {


    @Autowired
    private NavigationMapper mapper;
    @Autowired
    private SelectOptionDao selectOption;

    // 获取空间的树
    @Override
    public List<TreeNode> getAllSpaceTree(int id) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<AssetVo> spaceList = mapper.querySpaceTreeData(id);
        Collections.sort(spaceList);
        if (spaceList != null && spaceList.size() > 0) {
            for (AssetVo space : spaceList) {
                TreeNode node = printDeptTree(space);
                list.add(node);
            }
        }
        return list;
    }

    public List<TreeNode> setNodeList(List<Device> elevatorList) {

        List<TreeNode> treelist = new ArrayList<TreeNode>();
        addSpaceParentNode(treelist);

        if (elevatorList != null && elevatorList.size() > 0) {
            for (Device device : elevatorList) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(device.getId());
                treeNode.setText(device.getCaption());
                treelist.add(treeNode);
            }
        }
        return treelist;
    }

    private void addSpaceParentNode(List<TreeNode> treelist) {
        List<AssetVo> spaceList = mapper.querySpaceTreeData(0);
        for (AssetVo space : spaceList) {
            TreeNode node = new TreeNode();
            node.setId(space.getId());
            node.setText(space.getCaption());
            treelist.add(node);
        }

    }

    // 获取树的递归方法
    public TreeNode printDeptTree(AssetVo space) {
        TreeNode treeNode = new TreeNode();
        int id = space.getId();
        treeNode.setId(id);
        treeNode.setText(space.getCaption());
        List<AssetVo> spaceList = mapper.querySpaceTreeData(id);
        Collections.sort(spaceList);
        if (spaceList != null && spaceList.size() > 0) {
            for (AssetVo child : spaceList) {
                TreeNode childNode = printDeptTree(child);
                if (childNode != null) {
                    treeNode.addChild(childNode);
                }
            }
        }
        return treeNode;
    }

    @Override
    public List<TreeNode> getAllElectorList(int catalog) {
        List<Device> elevatorList = mapper.queryElevatorList(catalog);
        return setNodeList(elevatorList);
    }

    @Override
    public List<TreeNode> getEnergyKindList() {
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<CommonSelectOption> optionList = selectOption.querySelectOptionList(Code.ROOT_ID, Code.ENERGY);
        if (optionList != null && optionList.size() > 0) {
            for (CommonSelectOption option : optionList) {
                TreeNode node = new TreeNode();
                node.setCatalog(option.getCatalogId());
                node.setText(option.getName());
                node.setId(option.getId());
                list.add(node);
            }
        }
        return list;
    }

}

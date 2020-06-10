package com.vibe.controller.asset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Space;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.monitor.asset.type.SpaceType;
import com.vibe.pojo.AssetVo;
import com.vibe.service.asset.AssetService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.EasyUITreeNode;
import com.vibe.utils.TreeNode;


@Controller
public class SpaceController {
    @Autowired
    public AssetService assetService;
    @Autowired
    public Application application;
    @Autowired
    AssetStore assetStore;

    /*初始化空间树的数据*/
    @RequestMapping("/space/spaceTreeData")
    public @ResponseBody
    List<EasyUITreeNode> treeData(@RequestParam(defaultValue = "0", value = "id") Integer parent) {
        List<EasyUITreeNode> treeList = assetService.querySpaceTreeData(parent);
        return treeList;
    }

    @RequestMapping("/space/spaceAllTreeData")
    public @ResponseBody
    List<TreeNode> allTreeData(@RequestParam(defaultValue = "0", value = "id") Integer parent) {
        List<TreeNode> treeList = assetService.getAllSpaceTree(parent);
        return treeList;
    }

    /*加载树节点下的数据*/
    @RequestMapping("/space/spaceList")
    public @ResponseBody
    EasyUIJson spaceList(AssetVo assetVo, @RequestParam(defaultValue = "10") Integer rows, @RequestParam(defaultValue = "1") Integer page) {
        assetVo.setKind("SPACE");
        EasyUIJson json = assetService.findAssetList(assetStore, assetVo, rows, page);
        return json;

    }

    /*加载树节点下的数据*/
    @RequestMapping("/space/addSpace")
    @MethodLog(remark = "add", option = "添加空间信息")
    public @ResponseBody
    String addSpace(Space space) {
        try {
            IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
            space.setId(gen.next());
            AssetType assetType = assetStore.getAssetTypes(AssetKind.SPACE).find("3DSpace");
            space.setType((SpaceType) (assetType));
            assetService.addAsset(assetStore, space, null);
            return "200";
        } catch (Exception e) {
            return "500";

        }
    }

    /*查询空间详情*/
    @RequestMapping("/space/spaceDetail/{id}")
    public @ResponseBody
    AssetVo spaceDetail(@PathVariable Integer id) throws AssetException {
        Asset<?> root = assetStore.findAsset(id);
        if (root == null) {
            root = assetService.findAssetByID(id, AssetKind.SPACE.toString());
        }
        AssetVo vo = assetService.assetDteail(root, AssetKind.SPACE.toString());
        int parentId = root.parentId;
        vo.setParentId(parentId);
        return vo;
    }
}

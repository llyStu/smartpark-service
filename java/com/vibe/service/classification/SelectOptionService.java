package com.vibe.service.classification;

import java.util.List;

import com.vibe.pojo.CommonSelectOption;
import com.vibe.utils.TreeNode;

public interface SelectOptionService {

    public List<CommonSelectOption> querySelectOptionList(Integer parentId, Integer catalogId);

    public List<CommonSelectOption> querySelectOptionListOther(int parentId, int catalogId);

    public List<CommonSelectOption> anQuerySelectOptionList(int catalogId);

    public CommonSelectOption getSelectListData(int id);

    public List<Integer> getSelectIdList(int catalogId, int kind);

    public List<CommonSelectOption> querySelectOptionListbyArray(List<Integer> list1, int catalogId);

    public List<TreeNode> getItemsTree(int rootId, int catalogId);

    public List<TreeNode> getControlItemsTree(int rootId, int catalogId);

    List<Integer> getCatalogs(int workId);

    public int getParentId(int id, int catalogId);

    CommonSelectOption getSelectOption(int id, int catalogId);

    List<Integer> getEnergyCatalogIds(int parent);

    public List<Integer> queryMenusCodes(Integer menu, Integer catalog);


}

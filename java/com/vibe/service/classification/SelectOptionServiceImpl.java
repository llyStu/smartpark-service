package com.vibe.service.classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Control;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.utils.TreeNode;

@Service("selectOptionService")
public class SelectOptionServiceImpl implements SelectOptionService {

	@Autowired
	private SelectOptionDao selectOptionDao;
	@Autowired
	private AssetStore assetStore;

	@Override
	public List<Integer> getEnergyCatalogIds(int parentId) {
		List<Integer> list = new ArrayList<Integer>();
		List<CommonSelectOption> energyCatalogList = selectOptionDao.querySelectOptionList(parentId, Code.ENERGY);
		for (CommonSelectOption commonSelectOption : energyCatalogList) {
			list.add(commonSelectOption.getId());
		}
		return list;
	}

	@Override
	public List<CommonSelectOption> querySelectOptionList(Integer parentId, Integer catalogId) {

		return selectOptionDao.querySelectOptionList(parentId, catalogId);

	}

	@Override
	public List<CommonSelectOption> anQuerySelectOptionList(int catalogId) {
		// TODO Auto-generated method stub
		return selectOptionDao.anQuerySelectOptionList(catalogId);
	}

	@Override
	public int getParentId(int id, int catalogId) {
		CommonSelectOption selectOption = selectOptionDao.getSelectOption(id, catalogId);
		if (selectOption != null) {
			return selectOption.getParentId();
		}
		return 0;
	}

	@Override
	public CommonSelectOption getSelectOption(int id, int catalogId) {
		// TODO Auto-generated method stub
		return selectOptionDao.getSelectOption(id, catalogId);
	}

	@Override
	public CommonSelectOption getSelectListData(int id) {
		// TODO Auto-generated method stub
		return selectOptionDao.getSelectListData(id);
	}

	@Override
	public List<Integer> getSelectIdList(int catalogId, int kind) {
		// TODO Auto-generated method stub
		return selectOptionDao.getSelectIdList(catalogId, kind);
	}

	@Override
	public List<CommonSelectOption> querySelectOptionListbyArray(List<Integer> list1, int catalogId) {
		// TODO Auto-generated method stub
		return selectOptionDao.querySelectOptionListbyArray(list1, catalogId);
	}

	@Override
	public List<Integer> getCatalogs(int workId) {
		List<Integer> list = new ArrayList<Integer>();
		List<CommonSelectOption> optionList = selectOptionDao.querySelectOptionList(workId, Code.PROBE);
		if (optionList != null && optionList.size() > 0)
			for (CommonSelectOption selectOption : optionList) {
				int id = selectOption.getId();
				list.add(id);
				List<Integer> catalogs = getCatalogs(id);
				if (catalogs != null) {
					list.containsAll(catalogs);
				}
			}
		return list;
	}

	// 获取分类的树
	@Override
	public List<TreeNode> getControlItemsTree(int rootId, int catalogId) {
		List<TreeNode> list = new ArrayList<TreeNode>();

		List<CommonSelectOption> optionList = getWorkItems(rootId, catalogId);

		if (optionList != null && optionList.size() > 0) {
			for (CommonSelectOption selectOption : optionList) {
				TreeNode node = printControlItemTree(selectOption, catalogId);
				if(node!=null){
					list.add(node);
				}
			}
		}
		return list;
	}
	
	public TreeNode printControlItemTree(CommonSelectOption selectOption, int catalogId) {
		TreeNode treeNode = new TreeNode();
		int id = selectOption.getId();
		treeNode.setId(id);
		treeNode.setStatus(selectOption.getType());
		treeNode.setText(selectOption.getName());
		treeNode.setUnit(selectOption.getUnit());
		treeNode.setCatalog(selectOption.getId());
		List<CommonSelectOption> optionList = getWorkItems(id, catalogId);
		boolean hasControlItem = hasControlItem(optionList);
		if(!hasControlItem){
			return null;
		}
		if (optionList != null && optionList.size() > 0) {
			for (CommonSelectOption child : optionList) {
				TreeNode childNode = new TreeNode();
				int childId = child.getId();
				childNode.setId(childId);
				childNode.setStatus(child.getType());
				childNode.setText(child.getName());
				childNode.setUnit(child.getUnit());
				childNode.setCatalog(child.getId());
				if (childNode != null && childNode.getUnit()!=null && childNode.getUnit().contains("|") && hasUseControlItem(childNode.getCatalog())) {
					treeNode.addChild(childNode);
				}
			}
		}
		return treeNode;
	}
	
	
	private boolean hasUseControlItem(int catlog){
		Collection<Asset<?>> assets = assetStore.getSubAssets(Control.class);
		for (Asset<?> asset : assets) {
			if(((Control)asset).getMonitorKind() == catlog){
				return true;
			}
		}
		return false;
	}
	
	private boolean hasControlItem(List<CommonSelectOption> optionList){
		for (CommonSelectOption commonSelectOption : optionList) {
			if(commonSelectOption.getUnit()!=null && commonSelectOption.getUnit().contains("|") && hasUseControlItem(commonSelectOption.getId())){
				return true;
			}
		}
		return false;
	}

	// 获取分类的树
	@Override
	public List<TreeNode> getItemsTree(int rootId, int catalogId) {
		List<TreeNode> list = new ArrayList<TreeNode>();

		List<CommonSelectOption> optionList = getWorkItems(rootId, catalogId);

		if (optionList != null && optionList.size() > 0) {
			for (CommonSelectOption selectOption : optionList) {
				TreeNode node = printItemTree(selectOption, selectOption.getCatalogId());
				list.add(node);
			}
		}
		return list;
	}

	public List<CommonSelectOption> getWorkItems(int rootId, int catalogId) {
		List<CommonSelectOption> items = new ArrayList<CommonSelectOption>();
		List<CommonSelectOption> optionList = selectOptionDao.querySelectOptionList(rootId, catalogId);
		for (CommonSelectOption selectOption : optionList) {
			items.add(selectOption);
		}
		return items;
	}

	// 获取树的递归方法
	public TreeNode printItemTree(CommonSelectOption selectOption, int catalogId) {
		TreeNode treeNode = new TreeNode();
		int id = selectOption.getId();
		treeNode.setId(id);
		treeNode.setStatus(selectOption.getType());
		treeNode.setText(selectOption.getName());
		treeNode.setCatalog(selectOption.getCatalogId());
		treeNode.setUnit(selectOption.getUnit());
		List<CommonSelectOption> optionList = getWorkItems(id, catalogId);
		if (optionList != null && optionList.size() > 0) {
			for (CommonSelectOption child : optionList) {
				TreeNode childNode = printItemTree(child, catalogId);
				if (childNode != null) {
					treeNode.addChild(childNode);
					;
				}
			}
		}
		return treeNode;
	}

	@Override
	public List<CommonSelectOption> querySelectOptionListOther(int parentId, int catalogId) {
		// TODO Auto-generated method stub
		return selectOptionDao.getWorkItems(parentId, catalogId);
	}

	@Override
	public List<Integer> queryMenusCodes(Integer menu, Integer catalog) {
		// TODO Auto-generated method stub
		return selectOptionDao.queryMenusCodes(menu, catalog);
	}

}

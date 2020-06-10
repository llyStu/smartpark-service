package com.vibe.pojo.energy;

import java.util.ArrayList;
import java.util.List;

import com.vibe.pojo.CommonSelectOption;

public class CatalogTreeNode extends CommonSelectOption {
	private List<CatalogTreeNode> nodes;
	
	public static CatalogTreeNode withNodes() {
		CatalogTreeNode ret = new CatalogTreeNode();
		ret.setNodes(new ArrayList<>());
		return ret;
	}

	public List<CatalogTreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<CatalogTreeNode> nodes) {
		this.nodes = nodes;
	}

}

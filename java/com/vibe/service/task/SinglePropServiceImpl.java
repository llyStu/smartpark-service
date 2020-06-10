package com.vibe.service.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.task.SinglePropDao;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.SingleProp;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.util.CatalogIds;

@Service
public class SinglePropServiceImpl implements SinglePropService {

	@Autowired
	private SelectOptionService selectOptionService;
	@Autowired
	private SinglePropDao singlePropDao;
	
	@Override
	public List<SingleProp> queryForList(int siteId) {
		// TODO Auto-generated method stub

		List<CommonSelectOption> selectOptionList = selectOptionService.anQuerySelectOptionList(CatalogIds.CHECK_DESTINATION_CATALOG);
		List<SingleProp> result = new ArrayList<SingleProp>();
		for(CommonSelectOption item: selectOptionList){
			if(item.getParentId() == siteId){
				SingleProp prop = new SingleProp();
				prop.setName(item.getId());
				result.add(prop);
			}
		}
		return result;
	}

	@Override
	public void insert(SingleProp singleProp) {
		// TODO Auto-generated method stub
		singlePropDao.insert(singleProp);
	}

	@Override
	public void insertIntoIntervalTable(int taskId, int propId) {
		// TODO Auto-generated method stub
		singlePropDao.insertIntoIntervalTable(taskId, propId);
	}

	@Override
	public int queryMaxId() {
		// TODO Auto-generated method stub
		return singlePropDao.queryMaxId();
	}

}

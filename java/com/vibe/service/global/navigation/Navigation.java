package com.vibe.service.global.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import com.vibe.mapper.global.NavigationMapper;
import com.vibe.utils.TreeNode;

@Service
public class Navigation implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3226415496333039120L;
	/**
	 * 
	 */

	private Map<String,String> nameMap = new HashMap<String,String>();
	private Map<String,Object> valueMap = new HashMap<String,Object>();
	private Map<String,Object>  relationMap = new HashMap<String,Object>();

	@Autowired
	private NavigationMapper mapper;
	
	public static NavigationService getNavigationService(){
		ApplicationContext apx=new ClassPathXmlApplicationContext("applicationContext-mapper.xml");
		return (NavigationService) apx.getBean("navigationService");
	}
	public void addNameMap(String key,String value) {
		nameMap.put(key, value);
	}

	public Map<String, String> getNameMap() {
		return nameMap;
	}
	
	public void addRelationMap(String key,Object value) {
		relationMap.put(key, value);
	}

	public Map<String, Object> getRelationMap() {
		return relationMap;
	}
	
	public void addValueMap(String key,Object value) {
		valueMap.put(key, value);
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}
	public void loadNavigation(Integer menuId){
		//List<CommonSelectOption> codelist = selectOptionService.querySelectOptionList(0,Code.PROBE);
		queryCodeLocation(menuId);
		loadRelationMap(menuId);
	}
	
	private void loadRelationMap(Integer menuId) {
		List<Navigat> codeLocationList = mapper.queryCodeLocationList();
		List<Navigat> relationlist = new ArrayList<Navigat>();
		for (Navigat navigat : codeLocationList) {
			if(navigat.getMenu()==(int)menuId)
				relationlist.add(navigat);
		}
		addRelationMap("relation", relationlist);
	}

	public void queryCodeLocation(Integer menuId){
				List<Navigat> codeList = mapper.queryCodeLocationByCode(menuId);
				loadNameMap(codeList);
				loadValueMap(codeList);
	}

	public void loadNameMap(List<Navigat> list){
		if(list != null && list.size()>0){
			for (Navigat navigat : list) {
				addNameMap(navigat.getKeyValue(),navigat.getName());
			}
		}
	}
	public void loadValueMap(List<Navigat> list){
		if(list != null && list.size()>0){
			for (Navigat navigat : list) {
				Class<?> clazz;
				try {
					clazz = Class.forName(navigat.getClassName());
					NavigationData newInstance =(NavigationData) clazz.newInstance();
					List<TreeNode> loadList = newInstance.loadList();
					addValueMap(navigat.getKeyValue(),loadList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
		}
	}
	
}

package com.vibe.service.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.global.MenuDao;
import com.vibe.mapper.user.PermissionMapper;
import com.vibe.mapper.user.RoleMapper;
import com.vibe.pojo.MenuBean;
import com.vibe.pojo.user.User;
import com.vibe.util.UserUtil;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	/**
	 * by User get MenuTree
	 */
	@Override
	public MenuBean getMenus(HttpServletRequest request) {
		// TODO Auto-generated method stub
		MenuBean menuTree = null;
 		List<MenuBean> menuBeanList = getMenuList(request);
		menuTree = getMenuTreeByList(menuTree, menuBeanList);

		return menuTree;
	}

	private MenuBean getMenuTreeByList (MenuBean menuTree, List<MenuBean> menuBeanList) {
		for (MenuBean menuBean : menuBeanList) {
			if (menuBean != null && menuBean.getParent() == 0) {
				menuTree = menuBean;
			}
		}
		initMenuTree(menuTree, menuBeanList);
		return menuTree;
	}

	private void initMenuTree(MenuBean menuParent, List<MenuBean> menuBeanList) {
		List<MenuBean> childMenuList = new ArrayList<>();
		for (MenuBean menuBean : menuBeanList) {
			if (null != menuBean  && menuBean.getParent() == menuParent.getId()) {
				childMenuList.add(menuBean);
			}
		}
		Collections.sort(childMenuList, new Comparator<MenuBean>() {

			@Override
			public int compare(MenuBean o1, MenuBean o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getSequence(), o2.getSequence());
			}
		});
		menuParent.setChildren(childMenuList);
		if (childMenuList.size() != 0) {
			for (MenuBean menuBean : childMenuList) {
				initMenuTree(menuBean, menuBeanList);
			}
		}

	}
	@Override
	public List<MenuBean> getMenuList(HttpServletRequest request){
		List<Integer> menuIds = getMenuListByUser(request);
		List<MenuBean> menus = getMenuBeanBymenuIds(menuIds);
		return menus;
	}

	private List<MenuBean> getMenuBeanBymenuIds(List<Integer> menuIds) {
		List<MenuBean> menus = new ArrayList<MenuBean>();
		for (Integer id : menuIds) {
			MenuBean menuBean= menuDao.queryMenuById(id);
			if(menuBean != null){
				menus.add(menuBean);   
			}
		}
		return menus;
	}
	public List<Integer> getMenuListByUser(HttpServletRequest request){
		User currentUser = UserUtil.getCurrentUser(request);
		List<Integer> children = permissionMapper.queryMenubyUser(currentUser.getId());
		List<Integer> menuIds = getMenuIdsByPermissionId(children);
		return menuIds;
	}

	private List<Integer> getMenuIdsByPermissionId(List<Integer> children) {
		List<Integer> menuIds = new ArrayList<>();
		menuIds.addAll(children);
		for (Integer child : children) {
			addParentMenu(child,menuIds);
		}
		return menuIds;
	}
	public List<Integer> addParentMenu(Integer child,List<Integer> menuIds){
		
			Integer parent=menuDao.queryParentMenu(child);
			if(parent != null ){
				if(!menuIds.contains(parent)){
					menuIds.add(parent);
				}
				if(0 == parent){
					return menuIds;
				}
				addParentMenu(parent,menuIds);
			}
		return menuIds;
	}
	@Override
	public MenuBean getMenuTreeByRole(Integer roleId) {
			MenuBean menuTree = null;
	 		List<MenuBean> menuBeanList = getMenuListByRole(roleId);
	 		if(menuBeanList != null){
	 			menuTree = getMenuTreeByList(menuTree, menuBeanList);
	 		}
		return menuTree;
	}
	private List<MenuBean> getMenuListByRole(Integer roleId){
		List<Integer> menuIds = getMenuIdListByRole(roleId);
		if(menuIds.isEmpty() ){
			return null;
		}
		List<MenuBean> menus = getMenuBeanBymenuIds(menuIds);
		for (MenuBean menuBean : menus) {
			List<String> permissions = menuDao.queryPermissionBymIdAndRid(menuBean.getId(), roleId);
			menuBean.setPermisionList(permissions);
		}
		return menus;
	}
	public List<Integer> getMenuIdListByRole(Integer roleId){
		//获取所有的权限
		List<Integer> children = roleMapper.queryMenuByRoleId(roleId);
		List<Integer> menuIds = getMenuIdsByPermissionId(children);
		return menuIds;
	}

	@Override
	public Integer queryParentByMenu(int child) {
		// TODO Auto-generated method stub
		return menuDao.queryParentMenu(child);
	}
}

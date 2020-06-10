package com.vibe.service.global;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.vibe.pojo.MenuBean;

public interface MenuService {

	public List<MenuBean> getMenuList(HttpServletRequest request);

	public MenuBean getMenus(HttpServletRequest request);

	public MenuBean getMenuTreeByRole(Integer roleId);

	public Integer queryParentByMenu(int pid);

}

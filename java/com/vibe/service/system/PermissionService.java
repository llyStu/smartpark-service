package com.vibe.service.system;

import javax.servlet.http.HttpServletRequest;

import com.vibe.pojo.user.Permission;
import com.vibe.utils.EasyUIJson;

public interface PermissionService {

	public  EasyUIJson queryPemissionListByPage(Permission permission, Integer page, Integer rows);

	public void addPermission(Permission permission);

	public Permission queryPermissionById(Integer id);

	public void deletePermission(String ids);

	public void loadMenuPermission(HttpServletRequest request);
		

}

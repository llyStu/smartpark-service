package com.vibe.mapper.user;

import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.RolePermission;

import java.util.List;

/**
 * 权限的接口
 * @author FLex3
 *
 */
public interface PermissionMapper {
	/*
	 * 初始化权限树叶子节点数据
	 * 方法名queryPermissionByModuleId
	 * 参数 id
	 * 返回值List<Permission>
	 */
	public List<Permission> queryPermissionByMenuId(Integer id);
	/*
	 * 根据权限id查询权限信息
	 * 方法名queryPermissionByPid
	 * 参数 pid
	 * 返回值Permission
	 */
	public Permission queryPermissionByPid(Integer pid);
	public List<Permission> queryPermissionList(Permission permission);
	public void deletePermission(int id);
	public void addPermission(Permission permission);
	public List<Integer> queryMenubyUser(Integer id);
	public List<Permission> queryPermissionByRoleId(Integer id);
	public void deleteRolePermission(RolePermission rpBean);
	public void addRolePermission(RolePermission rolePermission);
	public RolePermission queryRolePermission(RolePermission rolePermission);
	
	
	
	
}

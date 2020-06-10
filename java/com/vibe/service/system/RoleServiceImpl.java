package com.vibe.service.system;

import java.util.List;

import com.vibe.mapper.user.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.user.PermissionMapper;
import com.vibe.mapper.user.RoleMapper;
import com.vibe.pojo.MenuBean;
import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.RolePermission;
import com.vibe.pojo.user.User;
import com.vibe.service.global.MenuService;
import com.vibe.service.user.UserService;
import com.vibe.utils.EasyUIJson;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Override
	public EasyUIJson queryRoleListByPage(Role role, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<Role> RoleList = roleMapper.queryRoleList(role);
		for (Role queryRole : RoleList) {
			MenuBean tree = menuService.getMenuTreeByRole(queryRole.getId());
			if(tree != null){
				queryRole.setMenuTree(tree);
			}
			List<User> userList = userService.queryUserListByRoleId(queryRole.getId());
			queryRole.setUserList(userList);
		}
		PageInfo<Role> pageInfo = new PageInfo<Role>(RoleList);
		EasyUIJson uiJson = new EasyUIJson();
		uiJson.setTotal(pageInfo.getTotal());
		uiJson.setRows(RoleList);
		return uiJson;
	}

	@Override
	public void addRole(Role role) {
		roleMapper.addRole(role);
		
	}

	@Override
	public Role queryRoleById(Integer id) {
		// TODO Auto-generated method stub
		return roleMapper.queryRoleById(id);
	}
	
	@Override
	public List<Permission> queryPermissionByRoleId(Integer id) {
		// TODO Auto-generated method stub
		return permissionMapper.queryPermissionByRoleId(id);
	}
	
	@Override
	public void updateRolePermission(Integer rid,String pids) {
		List<Permission> plist= permissionMapper.queryPermissionByRoleId(rid);
		for (Permission permission : plist) {
			permissionMapper.deleteRolePermission(new RolePermission(rid,permission.getId()));
		}
		if (pids != null && pids != "") {
			String[] split = pids.split(",");
			for (String id : split) {
				// 根据更新的权限，添加到用户权限表中
				int pid = Integer.parseInt(id);
				Permission permission = permissionMapper.queryPermissionByPid(pid);
				if(permission == null){
					getParentPermission(rid,pid);
				}else{
					RolePermission rolePermission = new RolePermission(rid,pid);
					RolePermission history = permissionMapper.queryRolePermission(rolePermission);
					if(history == null){
						permissionMapper.addRolePermission(rolePermission);
					}
				}
			}
		}
	}

	private void getParentPermission(Integer rid,Integer child) {
		Integer parentMenu = menuService.queryParentByMenu(child);
		List<Permission> pList = permissionMapper.queryPermissionByMenuId(parentMenu);
		if(0 != parentMenu && pList.isEmpty()){
			getParentPermission(rid,parentMenu);
		}
		if(0 == parentMenu){}
		for (Permission permission : pList) {
			RolePermission rolePermission = new RolePermission(rid,permission.getId());
			RolePermission history = permissionMapper.queryRolePermission(rolePermission);
			if(history == null){
				permissionMapper.addRolePermission(rolePermission);
			}
		}
	}
	
	@Override
	public void deleteRole(String ids, User user) {
		String[] split = ids.split(",");
		for (String string : split) {
			int id = Integer.parseInt(string);
			roleMapper.deleteRoleById(id);

		}
	}

	@Override
	public void editRole(Role role) {
		roleMapper.editRoleById(role);
	}

}

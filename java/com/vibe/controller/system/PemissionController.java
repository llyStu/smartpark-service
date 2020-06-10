package com.vibe.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.pojo.user.Permission;


import com.vibe.service.logAop.MethodLog;
import com.vibe.service.system.PermissionService;
import com.vibe.utils.EasyUIJson;

@Controller
public class PemissionController {
	@Autowired
    PermissionService permissionService;
	@RequestMapping("/permission/permissionList")
	public @ResponseBody EasyUIJson querypermissionList(Permission permission, @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer rows) {
		// 远程调用Service服务对象，获取员工列表数据

		EasyUIJson easyUIJson = permissionService.queryPemissionListByPage(permission, page, rows);
		return easyUIJson;
	}
	@RequestMapping("/permission/permissionAdd")
	@MethodLog(remark="add",option="添加权限")
	public @ResponseBody String permissionAdd(Permission permission) {
		try {
			permissionService.addPermission(permission);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
	}
	@RequestMapping("/permission/toPermissionEdit")
	@MethodLog(remark="edit",option="编辑权限")
	public @ResponseBody
    Permission toPermissionEdit(Integer id) {
		Permission permission = permissionService.queryPermissionById(id);
		return permission;
	}
	@RequestMapping("/permission/permissionDetail/{ids}")
	@MethodLog(remark="query",option="查看权限")
	public @ResponseBody List<Permission> permissionDetail(@PathVariable String ids) {
		String[] split = ids.split(",");
		String id = split[0];
		// 查询用户信息根据用户id
		List<Permission> list = new ArrayList<Permission>();
		Permission permission = permissionService.queryPermissionById(Integer.parseInt(id));
		/*
		 * Department dept = deptService.queryDeptById(permission.getDepartment());
		 * model.addAttribute("permission",permission); model.addAttribute("dept",dept);
		 */
		list.add(permission);
		return list;

	}
	@RequestMapping("/permission/deletePermission")
	@MethodLog(remark="delete",option="删除权限")
	public @ResponseBody String deletePermission(String ids) {
		try {
			permissionService.deletePermission(ids);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
		
	}
}

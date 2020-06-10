package com.vibe.controller.system;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.pojo.Lencence;
import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.User;
import com.vibe.pojo.user.UserPermission;
import com.vibe.pojo.user.UserVo;
import com.vibe.service.logAop.MethodLog;
import com.vibe.service.report.DownloadUtil;
import com.vibe.service.report.ExportType;
import com.vibe.service.user.UserService;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private Application application;


	// 跳转到员工列表
	@RequestMapping("/user/toList/{id}")
	public String toUserList(@PathVariable Integer id, Model model) {
		model.addAttribute("id", id);

		return "/system/user/userList";
	}
	@RequestMapping("/user/only_user")
	public @ResponseBody Boolean queryUserByLoginId(String loginId) {
		User user = userService.queryUserByLoginId(loginId);
		if(user == null){
			return true;
		}else{
			return false;
		}
	}
	
	
	/*
	 * 员工列表 请求：user/userList/{id}; 方法：queryUserList 参数：id 父部门id,分页组建1,10
	 * 返回值：EasyUIJson 返回值page
	 * 
	 */
	@RequestMapping("/user/userList")
	public @ResponseBody
    EasyUIJson queryUserList(UserVo userVo, @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer rows) {
		// 远程调用Service服务对象，获取员工列表数据

		EasyUIJson easyUIJson = userService.queryUserListByPage(userVo, page, rows);
		return easyUIJson;
	}
	@RequestMapping("/user/report")
	@MethodLog(remark="export",option="导出用户信息")
	public void exportUserReport(
			String type,
			HttpServletResponse resp) throws Exception {
		ExportType exportType = ExportType.of(type);
		String templateName = "user";
		
		OutputStream out = DownloadUtil.getDownloadStream(resp, templateName, exportType);
		userService.exportReport(templateName, type, out);
	}
	

	/*
	 * 新增员工 请求：user/userAdd 方法：userAdd 参数：表单参数 返回值：String 到emloyeeList
	 */
	@RequestMapping("/user/userAd")
	public String userAd(User user, Model model) {
		IdGenerator<Integer> gen = application.getIntIdGenerator("user");
		user.setId(gen.next());
		userService.addUser(user);

		model.addAttribute("id", user.getDepartment());

		return "/system/user/userList";

	}
	@RequestMapping("/user/userAdd")
	@MethodLog(remark="add",option="添加用户")
	public @ResponseBody String userAdd(User user) {
		try {
//			IdGenerator<Integer> gen = application.getIntIdGenerator("user");
//			user.setId(gen.next());
			userService.addUser(user);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
	}
	/*
	 * 跳到修改页面，加载用户信息 请求路径：/user/toUserEdit/${id} 方法：toUserEdit 参数：部门id,用户id
	 * 返回值：String 到修改页面userEdits
	 * 
	 */
	@RequestMapping("/user/toUserEdit/{id}")
	public String toOldUserEdit(@PathVariable Integer id, Model model,String flag) {
		// 查询用户信息根据用户id
		User user = userService.queryUserById(id);
		
		// 查询用户所在权限信息回显
		String str = userService.queryPermission(id);
		String[] split = str.split("=");
		if (split.length == 2) {
			model.addAttribute("permission", split[0]);
			model.addAttribute("chail", split[1]);
		}
		if(flag != null){
			model.addAttribute("flag", flag);
		}
		
		model.addAttribute("user", user);
		return "/system/user/userEdit";
	}
	
	@RequestMapping("/user/toUserEdit")
	public @ResponseBody
    User toUserEdit(Integer id) {
		// 查询用户信息根据用户id
		User user = userService.queryUserById(id);
		List<Role> allRole = userService.getAllRole(user.getId());
		user.setRlist(allRole);
		// 查询用户所在权限信息回显
		 List<Permission> permisionList = userService.queryPermisionList(id);
		user.setPlist(permisionList);
		
		return user;
	}
     /*
	 * 查询用户详情信息 请求路径：/user/userDetail/{ids} 方法：userDetail 参数：用户ids 返回值：String
	 * 到详情页
	 * 
	 */
	@RequestMapping("/user/userDetail/{ids}")
	@MethodLog(remark="query",option="查看用户")
	public @ResponseBody List<User> userDetail(@PathVariable String ids) {
		String[] split = ids.split(",");
		String id = split[0];
		// 查询用户信息根据用户id
		List<User> list = new ArrayList<User>();
		User user = userService.queryUserById(Integer.parseInt(id));
		/*
		 * Department dept = deptService.queryDeptById(user.getDepartment());
		 * model.addAttribute("user",user); model.addAttribute("dept",dept);
		 */
		list.add(user);
		return list;

	}

	/*
	 * 修改员工信息 请求：user/userEdit 方法：userEdit 参数：User 返回值:String
	 */
	@RequestMapping("/user/userEdit")
	@MethodLog(remark="edit",option="编辑用户")
	public @ResponseBody String userEdit(UserVo userVo) {
		try {
			userService.editUserById(userVo);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
	}
	/*
	 * 删除员工信息 请求：user/deleteUser 方法：deleteUser 参数：ids要删除的用户id,该部门id 返回值:String
	 */
	@RequestMapping("/user/deleteUser")
	@MethodLog(remark="delete",option="删除用户")
	public @ResponseBody String deleteUser(String ids) {
		try {
			userService.deleteUser(ids);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return "500";
		}
		
	}
	/**
	 * 权限管理，创建权限树
	 */
	@RequestMapping("/user/initTree")
	public @ResponseBody List<AllTreeNode> initTree(@RequestParam(defaultValue = "00") Integer parent) {

		List<AllTreeNode> permissionTree = userService.initPermissionTree(parent);
		return permissionTree;
	}
	/**
	 * 查询用户权限 请求：/user/queryUserPermission 方法：ueryUserPermission 参数：id用户id
	 * 返回值：List<UserPermision>
	 */
	@RequestMapping("/user/queryUserPermission")
	@MethodLog(remark="query",option="查看用户权限")
	public @ResponseBody List<UserPermission> queryUserPermission(Integer id) {
		List<UserPermission> upList = userService.queryUserPermission(id);
		return upList;
	}
	/* 查询所有人员名称的列表 */
	@RequestMapping("/user/userNameList")
	public @ResponseBody List<DeptJson> userNameList() {

		List<DeptJson> json = userService.queryUserNameList();
		return json;
	}
	
	
	@RequestMapping("/user/lencence")
	public @ResponseBody
    Lencence lencence() {
		Lencence lencence = new Lencence();
		lencence.setResultDay(userService.lencence());
		return lencence;
		
	}
}

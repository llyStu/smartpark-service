package com.vibe.service.user;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.dept.DeptMapper;
import com.vibe.mapper.global.MenuDao;
import com.vibe.mapper.user.PermissionMapper;
import com.vibe.mapper.user.RoleMapper;
import com.vibe.mapper.user.UserMapper;
import com.vibe.mapper.user.UserPermissionMapper;
import com.vibe.mapper.user.UserRoleMapper;
import com.vibe.pojo.user.Department;
import com.vibe.pojo.MenuBean;
import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.User;
import com.vibe.pojo.user.UserPermission;
import com.vibe.pojo.user.UserRole;
import com.vibe.pojo.user.UserVo;
import com.vibe.service.report.ExportType;
import com.vibe.service.report.ReportService;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.MD5Utils;
import com.vibe.utils.time.TimeUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class UserServiceimpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private UserPermissionMapper userPermissionMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private ReportService reportService;

	private static int nextId = (int) (Math.random() * 65536);

	/**
	 * 用户登录
	 */
	@Override
	public User queryUser(User user) {
		// 用MD5给密码加密，后在传入
		String password = user.getPassword();
		String md5 = MD5Utils.md5(password);
		user.setPassword(md5);
		User user2 = userMapper.queryUser(user);
		if(user2==null){
			return null;
		}
		Department dept = deptMapper.queryDeptById(user2.getDepartment());
		if(dept != null){
		user2.setDeptName(dept.getName());
		}
		return user2;
	}

	/**
	 * 根据部门id，分页查询员工列表
	 */
	@Override
	public EasyUIJson queryUserListByPage(UserVo userVo, Integer page, Integer rows) {

		// 设置分页参数
		PageHelper.startPage(page, rows);
		// 调用接口查询数据
		List<User> userList = userMapper.queryUserList(userVo);
		// 创建PageInfo对象，获取分页信息
		PageInfo<User> pageInfo = new PageInfo<User>(userList);
		// 创建EasyUIJson对象，封装查询结果
		EasyUIJson uiJson = new EasyUIJson();
		// 设置查询总记录数
		uiJson.setTotal(pageInfo.getTotal());
		// 设置查询记录
		uiJson.setRows(userList);
		return uiJson;
	}
	/**
	 * 
	 * 添加用户 方法名称：addUser 参数 类型:User user 返回值类型: void
	 */
	@Override
	public void addUser(User user) {
//		user.setId(nextId++);
		// MD5加密密码
		String password = user.getPassword();
		String md5 = MD5Utils.md5(password);
		user.setPassword(md5);
		userMapper.addUser(user);
		if (null != user.getRid()){
			userRoleMapper.addUserRole(new UserRole(user.getId(),user.getRid()));
		}
	}

	/**
	 * 编辑用户信息 方法:editUserById 参数 ：用户id 返回值:User
	 */
	@Override
	public void editUserById(UserVo userVo) {
		Integer uid = userVo.getId();
		User userById = userMapper.queryUserById(uid);
		String password = userVo.getPassword();
		if (StringUtils.isNotBlank(password) && !StringUtils.equals("",password)) {
			String md5 = MD5Utils.md5(password);
			userById.setPassword(md5);
		}
		userById.setLogin_id(userVo.getLogin_id());
		userById.setDepartment(userVo.getDepartment());
		userById.setMail(userVo.getMail());
		userById.setName(userVo.getName());
		userById.setPhone(userVo.getPhone());
		try {
			// 修改用户基本信息
			userMapper.editUserById(userById);
			if (null != userVo.getRid()){
				List<UserRole> userRoleList = userRoleMapper.queryUserRole(uid);
				if (null != userRoleList && userRoleList.size() > 0){
					userRoleMapper.updateUserRole(new UserRole(uid,userVo.getRid()));
				}else{
					userRoleMapper.addUserRole(new UserRole(uid,userVo.getRid()));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 当一个用户有多个角色时，使用这个多个角色的方法
	 */
	@SuppressWarnings("unused")
	private void updateUserRole(UserVo userVo, Integer uid) {
		// 根据uid删除该用户的权限
	
		String rids = userVo.getRids();
		if (rids != null && rids != "") {
			String[] split = rids.split(",");
			for (String id : split) {
				// 根据更新的角色，添加到用户角色表中
				int parseInt = Integer.parseInt(id);
				UserRole userRole = new UserRole(parseInt, uid);
				userRoleMapper.deleteUserRole(uid,parseInt);
				userRoleMapper.addUserRole(userRole);
			}
		}
	}
	@SuppressWarnings("unused")
	private void updateUserPermision(UserVo userVo, Integer uid) {
		// 根据uid删除该用户的权限
		userPermissionMapper.deleteUserPermission(uid);
		String pids = userVo.getPids();
		if (pids != null && pids != "") {
			String[] split = pids.split(",");
			for (String id : split) {
				// 根据更新的权限，添加到用户权限表中
				int parseInt = Integer.parseInt(id);
				UserPermission userPermission = new UserPermission();
				userPermission.setPid(parseInt);
				userPermission.setUid(uid);
				userPermissionMapper.addUserPermission(userPermission);
			}
		}
	}

	/**
	 * 查询用户信息根据用户id 方法 名称：queryUserById 参数 ：用户id 返回值:User
	 */
	@Override
	public User queryUserById(Integer id) {
		User user = userMapper.queryUserById(id);
		if (user != null) {
			user.setPassword("      ");
		}
		return user;

	}

	/**
	 * 伪删除员工信息 请求：user/deleteUser 方法：deleteUser 参数：ids要删除的用户id串 返回值:void
	 */

	@Override
	public void deleteUser(String ids) {
		// 用，号切成数组
		String[] split = ids.split(",");
		// 遍历数组
		for (String string : split) {
			int id = Integer.parseInt(string);
			User userById = userMapper.queryUserById(id);
			// 修改状态
			userById.setValid(0);
			userMapper.editUserById(userById);
		}

	}
	
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@Override
	public void exportReport(String templateName, String type, OutputStream out) throws FileNotFoundException, JRException {
		List<User> users = userMapper.queryUserAndDepartment(null);
		
		HashMap<String, Object> parms = new HashMap<>();
		parms.put("nowtime", dtf.format(LocalDateTime.now()));
		
		JasperPrint fillReport = reportService.fillReport(templateName, users, parms);
		ExportType.of(type).writeAndClose(out, fillReport);
	}

	/*@Override
	public List<AllTreeNode> initPermissionTree(Integer parent) {
		HashMap<Integer, AllTreeNode> map = new HashMap<>();
		AllTreeNode root = newAllTreeNode(0, "系统菜单");
		map.put(root.getId(), root);
		
		MenuBean menu = ms.getMenus();
		for (MenuBean it : menu.getChildren()) {
			AllTreeNode tn = newAllTreeNode(it);
			map.put(tn.getId(), tn);
			root.getChildren().add(tn);
		}
		
		List<Permission> perm = permissionMapper.queryPermissionList(new Permission());
		for (Permission it : perm) {
			AllTreeNode newAllTreeNode = newAllTreeNode(it);
			AllTreeNode old = map.put(newAllTreeNode.getId(), newAllTreeNode);
			if (old != null) newAllTreeNode.setChildren(old.getChildren());
			
			AllTreeNode node = map.get(it.getMenuId());
			if (node == null) {
				node = newAllTreeNode(it.getMenuId());
				map.put(node.getId(), node);
			}
			List<AllTreeNode> children = node.getChildren();
			if (children == null) {
				children = new ArrayList<>();
				node.setChildren(children);
			}
			children.add(newAllTreeNode);
		}
		return parent == null ? Arrays.asList(root) : Arrays.asList(map.get(parent));
	}
	private AllTreeNode newAllTreeNode(int id) {
		AllTreeNode node = new AllTreeNode();
		node.setId(id);
		return node;
	}
	private AllTreeNode newAllTreeNode(Permission p) {
		AllTreeNode node = new AllTreeNode();
		node.setId(p.getId());
		node.setText(p.getName());
		return node;
	}
	private AllTreeNode newAllTreeNode(MenuBean menu) {
		AllTreeNode ret = newAllTreeNode(menu.getId(), menu.getCaption());
		List<AllTreeNode> children = ret.getChildren();
		children.add(newAllTreeNode(340 + menu.getId(), menu.getCaption() + CheckPermission.READ));
		children.add(newAllTreeNode(680 + menu.getId(), menu.getCaption() + CheckPermission.WRITE));
		return ret;
	}
	private AllTreeNode newAllTreeNode(int id, String text) {
		AllTreeNode ret = new AllTreeNode();
		ret.setId(id);
		ret.setState("open");
		ret.setText(text);
		ret.setChildren(new ArrayList<>());
		return ret;
	}*/
	
	
	/**
	 * 加载权限树 请求： 方法：initPermissionTree 参数： 返回：List<AllTreeNode>
	 */
//	@Override
	public List<AllTreeNode> initPermissionTree(Integer parent) {
		parent = 0;
		List<AllTreeNode> node = getNode(0);
		return node;
	}
	private Integer id = null;
	
	public List<AllTreeNode> getNode(Integer parent) {
		List<AllTreeNode> node = new ArrayList<AllTreeNode>();
		List<MenuBean> menuList = menuDao.queryMenuByParent(parent);

		if (menuList != null && menuList.size() > 0) {
			for (MenuBean menu : menuList) {
				id = menu.getId();
				AllTreeNode allTreeNode = new AllTreeNode();
				allTreeNode.setId(menu.getId());
				allTreeNode.setState("open");
				allTreeNode.setText(menu.getCaption());
				List<AllTreeNode> children = getNode(menu.getId());
				allTreeNode.setChildren(children);

				node.add(allTreeNode);
			}
		} else {
			if (id != null) {
				List<Permission> permissionList = permissionMapper.queryPermissionByMenuId(id);
				if (permissionList != null && permissionList.size() > 0) {
					for (Permission permission : permissionList) {
						AllTreeNode allTreeNode2 = new AllTreeNode();
						allTreeNode2.setId(permission.getId());
						allTreeNode2.setState("open");
						allTreeNode2.setText(permission.getName());
						node.add(allTreeNode2);
					}
				}
			}
		}
		return node;
	}

	/**
	 * 查询用户权限 请求：/user/queryUserPermission 方法：ueryUserPermission 参数：id用户id
	 * 返回值：List<UserPermision>
	 */
	@Override
	public List<UserPermission> queryUserPermission(Integer id) {
		return userPermissionMapper.queryUserPermission(id);

	}
	@Override
	public List<Role> getAllRole(int uid) {
		List<Role> ret = roleMapper.getAllRole(uid);
	/*	if (ret == null || ret.isEmpty()) return ret;
		
		MenuBean menus = ms.getMenus();
		HashMap<Object, Object> map = new HashMap<>();
		for (MenuBean menu : menus.getChildren()) {
			map.put(menu.getId(), menu.getCaption());
		}
		
		for (Role it : ret) {
			if (it.getId() > 1024) continue;
			assert it.getName() == null;
			
			Integer id = it.getId();
			it.setName(id > 512 ? map.get(id - 512) + CheckPermission.WRITE : map.get(id) + CheckPermission.READ);
		}*/
		return ret;
	}

	/**
	 * 老版本   查询用户权限 请求：/user/queryUserPermission 方法：queryPermission 参数：id用户id
	 * 返回值：String 所有权限的拼接串
	 */
	@Override
	public String queryPermission(Integer id) {
		String permision = " ";
		String pids = "";
		List<UserPermission> plist = userPermissionMapper.queryUserPermission(id);
		if (plist != null && !plist.equals("")) {
			for (UserPermission up : plist) {
				pids += up.getPid() + ",";
				// 根据pid查权限的名称
				Permission pe = permissionMapper.queryPermissionByPid(up.getPid());
				if (pe != null && !pe.equals("")) {
					permision = permision + pe.getName() + "  ";
				}
			}

		}
		String str = permision + "=" + pids;
		return str;
	}
	public List<Permission> queryPermisionList(Integer id){
		//List<Permission> list = new ArrayList<Permission>();
	//	List<UserPermission> plist = userPermissionMapper.queryUserPermission(id);
		List<Permission> list = userPermissionMapper.queryPermissions(id);
		/*if (plist != null && !plist.equals("")) {
			MenuBean menus = ms.getMenus();
			HashMap<Integer, String> map = new HashMap<>();
			for (MenuBean m : menus.getChildren()) {
				map.put(m.getId(), m.getCaption());
			}
			for (UserPermission up : plist) {
				if (up.getPid() < 1024) {
					Permission p = new Permission();
					list.add(p);
					p.setId(up.getPid());
					p.setMenuId(up.getPid() % 340);
					p.setName(map.get(p.getMenuId()) + (up.getPid() / 340 == 1 ? CheckPermission.READ : CheckPermission.WRITE));
					continue;
				}
				Permission pe = permissionMapper.queryPermissionByPid(up.getPid());
				list.add(pe);
			}
		}*/
		return list;
	}

	/* 查询所有用户姓名 */
	@Override
	public List<DeptJson> queryUserNameList() {
		List<DeptJson> dlist = new ArrayList<DeptJson>();
		List<User> queryUserList = userMapper.queryUserList(null);
		if (queryUserList != null) {
			for (User user : queryUserList) {
				DeptJson deptJson = new DeptJson();
				deptJson.setId(user.getId());
				deptJson.setText(user.getName());
				deptJson.setName(user.getName());
				dlist.add(deptJson);
			}

		}
		return dlist;
	}
	@Override
	public User queryUserByLoginId(String loginId){
		return userMapper.queryUserByLoginId(loginId);
	}

	@Value("${day}")  
	private String day;
	
	@Override
	public int lencence() {
		// TODO Auto-generated method stub
		Date date = TimeUtil.string2Date(day, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
		Date current = new Date();
		long days=(date.getTime()-current.getTime())/(1000*3600*24);
		return (int) days;
	}

	@Override
	public List<User> queryUserListByRoleId(Integer id) {
		// TODO Auto-generated method stub
		return userMapper.queryUserListByRoleId(id);
	}

	
}

package com.vibe.service.user;

import java.io.OutputStream;
import java.util.List;
import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.User;
import com.vibe.pojo.user.UserPermission;
import com.vibe.pojo.user.UserVo;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;


public interface UserService {
	public void exportReport(String templateName, String type, OutputStream out) throws Exception;
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public User queryUser(User user);
	/**
	 * 分页查询员工列表
	 * @param user
	 * @param page
	 * @param rows
	 * @return
	 */
	public User queryUserByLoginId(String loginId);
	public EasyUIJson queryUserListByPage(UserVo userVo, Integer page, Integer rows);
	/**
	 * 
	 * 添加用户
	 * 方法名称：addUser
	 * 参数 类型:User user
	 * 返回值类型: void
	 */
	public void addUser(User user);
	/**
	 * 编辑用户信息
	 * 方法:editUserById
	 * 参数 ：userVo
	 * 返回值:User
	 */
	public void editUserById(UserVo userVo);
	/**
	 * 查询用户信息根据用户id
	 * 方法 名称：queryUserById
	 * 参数 ：用户id
	 * 返回值:User
	 */

	public User queryUserById(Integer id);
	
	/**
	 *伪删除员工信息
	 *请求：user/deleteUser
	 *方法：deleteUser
	 *参数：ids要删除的用户id串
	 *返回值:void
	 */
	public void deleteUser(String ids);
	/**
	 * 加载权限树
	 * 请求：
	 * 方法：initPermissionTree
	 * 参数：
	 * 返回：List<AllTreeNode>
	 */
	public List<AllTreeNode> initPermissionTree(Integer parent);
	/**
	 * 查询用户权限
	 * 请求：/user/queryUserPermission
	 * 方法：ueryUserPermission
	 * 参数：id用户id
	 * 返回值：List<UserPermision>
	 */
	public List<UserPermission> queryUserPermission(Integer id);
	/**
	 * 查询用户权限。返回权限信息的集合
	 */
	public String queryPermission(Integer id);
	/**
	 * 查询所有用户姓名
	 * @return
	 */
	public List<DeptJson> queryUserNameList();
	public List<Permission> queryPermisionList(Integer id);
	public List<Role> getAllRole(int uid);
	public int lencence();
	public List<User> queryUserListByRoleId(Integer id);
	
}

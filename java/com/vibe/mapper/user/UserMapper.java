package com.vibe.mapper.user;

import com.vibe.pojo.user.User;
import com.vibe.pojo.user.UserVo;

import java.util.List;
import java.util.Map;

/**
 * 增删改查的接口
 *
 * @author FLex3
 */

public interface UserMapper {
    public List<User> queryUserAndDepartment(Map map);

    /*
     * 实现登录功能
     * 方法名称：queryUser
     * 参数 类型:user
     * 返回值类型: user
     */
    public User queryUser(User user);

    /*
     * 查询部门用户列表
     * 方法名称：queryUserList
     * 参数 类型:id
     * 返回值类型: List<User>
     */
    public List<User> queryUserList(UserVo userVo);

    /*
     * 添加用户
     * 方法名称：addUser
     * 参数 类型:User user
     * 返回值类型: void
     */
    public void addUser(User user);

    /*
     * 查询用户信息根据用户id
     * 方法 名称：queryUserById
     * 参数 ：用户id
     * 返回值:User
     */
    public User queryUserById(Integer id);

    public User queryUserByLoginId(String loginId);

    /*编辑用户信息
     * 方法:editUserById
     * 参数 ：用户id
     * 返回值:User
     */
    public void editUserById(User user);

    public List<User> queryUserListByRoleId(Integer id);

    /**
     * 通过officeId查询数字园区用户
     *
     * @param id
     * @return
     */
    User selectOldUserByUserAndPass(String id);
}

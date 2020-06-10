package com.vibe.mapper.user;

import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.UserPermission;

import java.util.List;

/**
 * 增删改查的接口
 *
 * @author FLex3
 */

public interface UserPermissionMapper {
    /**
     * 查询用户权限
     * 方法：queryUserPermission
     * 参数：id用户id
     * 返回值：List<UserPermision>
     */
    public List<UserPermission> queryUserPermission(Integer id);

    /**
     * 根据uid删除用户权限
     * 方法 deleteUserPermission
     * 参数：uid
     * 返回值 void
     */
    public void deleteUserPermission(Integer uid);

    /**
     * 保存新的用户权限
     *
     * @param uid
     * @param pid
     */

    public void addUserPermission(UserPermission userPermission);

    public List<Permission> queryPermissions(Integer id);

}

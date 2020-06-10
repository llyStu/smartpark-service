package com.vibe.mapper.user;

import com.vibe.pojo.user.UserRole;

import java.util.List;

/**
 * 增删改查的接口
 *
 * @author FLex3
 */

public interface UserRoleMapper {

    public List<UserRole> queryUserRole(Integer id);

    public void deleteUserRole(Integer uid, int rid);

    public void addUserRole(UserRole userRole);

    public List<UserRole> queryRoles(Integer id);

    public void updateUserRole(UserRole userRole);

}

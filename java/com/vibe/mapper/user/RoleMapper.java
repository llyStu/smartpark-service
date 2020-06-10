package com.vibe.mapper.user;

import com.vibe.pojo.user.Role;

import java.util.List;

public interface RoleMapper {
    public List<Role> getAllRole(int uid);

    public List<Role> queryRoleList(Role uid);

    public void addRole(Role uid);

    public void editRoleById(Role role);

    public void deleteRoleById(int id);

    public Role queryRoleById(int id);

    public List<Integer> queryMenuByRoleId(Integer id);
}

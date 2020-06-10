package com.vibe.service.system;

import java.util.List;

import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.User;
import com.vibe.utils.EasyUIJson;

public interface RoleService {

    public EasyUIJson queryRoleListByPage(Role role, Integer page, Integer rows);

    public void addRole(Role role);

    public Role queryRoleById(Integer id);

    public void deleteRole(String ids, User user);

    public void editRole(Role role);

    public List<Permission> queryPermissionByRoleId(Integer id);

    public void updateRolePermission(Integer rid, String pids);


}

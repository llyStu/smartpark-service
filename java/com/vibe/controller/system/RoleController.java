package com.vibe.controller.system;

import java.util.ArrayList;
import java.util.List;

import com.vibe.pojo.user.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.user.Permission;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.UserPermission;
import com.vibe.service.logAop.MethodLog;
import com.vibe.service.system.RoleService;
import com.vibe.utils.EasyUIJson;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequestMapping("/role/roleList")
    public @ResponseBody
    EasyUIJson queryroleList(Role role, @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "20") Integer rows) {
        // 远程调用Service服务对象，获取员工列表数据

        EasyUIJson easyUIJson = roleService.queryRoleListByPage(role, page, rows);
        return easyUIJson;
    }

    @RequestMapping("/role/roleAdd")
    @MethodLog(remark = "add", option = "添加角色")
    public @ResponseBody
    String roleAdd(Role role) {
        try {
            roleService.addRole(role);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

    @RequestMapping("/role/queryRolePermission")
    @MethodLog(remark = "query", option = "查看角色权限")
    public @ResponseBody
    List<Permission> queryUserPermission(Integer id) {
        List<Permission> upList = roleService.queryPermissionByRoleId(id);
        return upList;
    }

    @RequestMapping("/role/updateRolePermission")
    @MethodLog(remark = "edit", option = "配置角色权限")
    public @ResponseBody
    String updateRolePermission(Integer id, String pids) {
        try {
            roleService.updateRolePermission(id, pids);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

    @RequestMapping("/role/toRoleEdit")
    @MethodLog(remark = "edit", option = "查看角色")
    public @ResponseBody
    Role toRoleEdit(Integer id) {
        Role role = roleService.queryRoleById(id);
        return role;
    }

    @RequestMapping("/role/roleDetail/{ids}")
    @MethodLog(remark = "query", option = "查看多个角色")
    public @ResponseBody
    List<Role> roleDetail(@PathVariable String ids) {
        String[] split = ids.split(",");
        String id = split[0];
        // 查询用户信息根据用户id
        List<Role> list = new ArrayList<Role>();
        Role role = roleService.queryRoleById(Integer.parseInt(id));
        list.add(role);
        return list;

    }

    @RequestMapping("/role/deleteRole")
    @MethodLog(remark = "delete", option = "删除角色")
    public @ResponseBody
    String deleteRole(String ids) {
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            roleService.deleteRole(ids, user);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }

    }

    @RequestMapping("/role/updateRole")
    @MethodLog(remark = "edit", option = "编辑角色")
    public @ResponseBody
    String updateRole(Role role) {
        try {
            roleService.editRole(role);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }

    }
}

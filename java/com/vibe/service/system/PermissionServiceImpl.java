package com.vibe.service.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.user.PermissionMapper;
import com.vibe.pojo.MenuBean;
import com.vibe.pojo.user.Permission;
import com.vibe.service.global.MenuService;
import com.vibe.utils.EasyUIJson;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    private MenuService menuService;

    @Override
    public EasyUIJson queryPemissionListByPage(Permission permission, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Permission> permissionList = permissionMapper.queryPermissionList(permission);
        PageInfo<Permission> pageInfo = new PageInfo<Permission>(permissionList);
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal(pageInfo.getTotal());
        uiJson.setRows(permissionList);
        return uiJson;
    }

    @Override
    public void addPermission(Permission permission) {
        permissionMapper.addPermission(permission);

    }

    @Override
    public Permission queryPermissionById(Integer id) {
        // TODO Auto-generated method stub
        return permissionMapper.queryPermissionByPid(id);
    }

    @Override
    public void deletePermission(String ids) {
        String[] split = ids.split(",");
        for (String string : split) {
            int id = Integer.parseInt(string);
            permissionMapper.deletePermission(id);
        }
    }

    @Override
    public void loadMenuPermission(HttpServletRequest request) {
        List<MenuBean> menuList = menuService.getMenuList(request);
        for (MenuBean menuBean : menuList) {
            if (menuBean.getParent() != 0) {
                insertPermisions(menuBean, CheckPermission.WRITE);
                insertPermisions(menuBean, CheckPermission.READ);
            }
        }
    }

    private void insertPermisions(MenuBean menuBean, String name) {
        Permission permission = getPermissionBean(menuBean, name);
        List<Permission> queryPermissionList = permissionMapper.queryPermissionList(permission);
        if (queryPermissionList.size() == 0) {
            permissionMapper.addPermission(permission);
        }
    }

    private Permission getPermissionBean(MenuBean menuBean, String name) {
        Permission permission = new Permission();
        permission.setMenuId(menuBean.getId());
        permission.setName(menuBean.getCaption() + name);
        return permission;
    }


}

package com.vibe.pojo.user;

import java.io.Serializable;
import java.util.List;

import com.vibe.pojo.MenuBean;


/**
 * 角色表
 *
 * @author FLex3
 */
public class Role implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8675750393299285351L;
    private Integer id;            //模块id
    private String name;        //模块名称
    private String caption;        //说明
    private MenuBean menuTree;  //权限tree
    private List<User> userList; //所有配置该角色的人

    public Role() {
        super();
        // TODO Auto-generated constructor stub
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public MenuBean getMenuTree() {
        return menuTree;
    }

    public void setMenuTree(MenuBean menuTree) {
        this.menuTree = menuTree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


}

package com.vibe.pojo.user;

import java.io.Serializable;

/**
 * 用户权限的管理表
 *
 * @author FLex3
 */
public class RolePermission implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6236379339750452512L;

    private Integer rid;//角色id
    private Integer pid;//权限id

    public RolePermission(Integer rid, Integer pid) {
        this.rid = rid;
        this.pid = pid;
    }

    public RolePermission(Integer rid) {
        this.rid = rid;
    }

    public RolePermission() {

    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

}

package com.vibe.pojo.user;

import java.io.Serializable;

/**
 * 用户权限的管理表
 *
 * @author FLex3
 */
public class UserRole implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6541007908970781788L;
    private Integer uid;//用户id
    private Integer rid;//权限id

    public UserRole(Integer uid, Integer rid) {
        super();
        this.uid = uid;
        this.rid = rid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }


}

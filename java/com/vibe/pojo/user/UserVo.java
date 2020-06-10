package com.vibe.pojo.user;

public class UserVo extends User {
    /**
     *
     */
    private static final long serialVersionUID = 5230234742481111453L;
    private String pids;//该用户下的权限id
    private String rids;

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

}

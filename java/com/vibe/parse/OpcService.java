package com.vibe.parse;

public class OpcService extends BaseService {
    private String ip;
    private String username;
    private String password;
    private String clsid;
    private String progId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClsid() {
        return clsid;
    }

    public void setClsid(String clsid) {
        this.clsid = clsid;
    }

    public String getProgId() {
        return progId;
    }

    public void setProgId(String progId) {
        this.progId = progId;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "insert into t_service(id, parent, type, name, caption)values (" + getId() + "," + getParent()
                + ", \"OpcUtgardMaster\", \"" + getName() + "\" , \"" + getCaption()
                + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"Host\", \""
                + getIp() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"Username\", \"" + getUsername() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"Password\", \"" + getPassword() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"ProgId\", \"" + getProgId() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"Clsid\", \"" + getClsid() + "\");";
    }
}

package com.vibe.parse;

public class IEC104Service extends BaseService {
    private String ip;
    private String port;
    private String commonAddrStr;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCommonAddrStr() {
        return commonAddrStr;
    }

    public void setCommonAddrStr(String commonAddrStr) {
        this.commonAddrStr = commonAddrStr;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "insert into t_service(id, parent, type, name, caption)values (" + getId() + "," + getParent()
                + ", \"IEC104Master\", \"" + getName() + "\" , \"" + getCaption()
                + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"Host\", \""
                + getIp() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"Host\", \"" + getIp() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"Port\", \"" + getPort() + "\");\ninsert into t_asset_prop(asset, name, value) values ("
                + getId() + ", \"CommonAddrStr\", \"" + getCommonAddrStr() + "\");";
    }
}

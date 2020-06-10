package com.vibe.pojo.mounting;

public class ConnMountiong {

    private String connname;
    private String actuator;
    private String dburl;
    private String username;
    private String password;

    public String getConnName() {
        return connname;
    }

    public void setConnName(String connName) {
        this.connname = connName;
    }

    public String getActuator() {
        return actuator;
    }

    public void setActuator(String actuator) {
        this.actuator = actuator;
    }

    public String getDburl() {
        return dburl;
    }

    public void setDburl(String dburl) {
        this.dburl = dburl;
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

    @Override
    public String toString() {
        return "ConnMountiong [connname=" + connname + ", actuator=" + actuator + ", dburl=" + dburl + ", username="
                + username + ", password=" + password + "]";
    }


}

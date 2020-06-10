package com.vibe.pojo;

public class Data {
    public Data() {
        super();
        // TODO Auto-generated constructor stub
    }

    private Integer id;
    private Integer monitorId;
    private String flag;

    public Data(Integer id, Integer monitorId, String flag) {
        super();
        this.id = id;
        this.monitorId = monitorId;
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Integer monitorId) {
        this.monitorId = monitorId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}

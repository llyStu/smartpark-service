package com.vibe.pojo;

//对页面显示部分属性（BuildingVo属性抽取）
public class BuildingTemp {

    private Integer bId;
    private String time;
    private int position;
    private Integer person;

    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

}

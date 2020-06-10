package com.vibe.pojo;

import java.time.LocalDate;

public class Check extends Task {
    private Integer cId;

    private LocalDate check_time;//巡检时间
    private String check_date;//傀儡

    private int check_position;

    //用于拼接所有图片的绝对路径，向数据库中保存
    private String pictures;

    private Integer t_id;

    public Integer getCId() {
        return cId;
    }

    public void setCId(Integer cId) {
        this.cId = cId;
    }

    //check_date只提供获取方法，因为它的值与Check_time有关
    public String getCheck_date() {
        return check_date;
    }

/*	public void setCheck_date(String check_date) {
		this.check_date = this.check_time.toString();
	}*/

    public void setCheck_time(LocalDate check_time) {
        this.check_time = check_time;
        check_date = check_time != null ? check_time.toString() : "";
    }

    public String getCheck_time() {
        return this.getCheck_date();
    }

    public Integer getCheck_position() {
        return check_position;
    }

    public void setCheck_position(int check_position) {
        this.check_position = check_position;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Integer getT_id() {
        return t_id;
    }

    public void setT_id(Integer t_id) {
        this.t_id = t_id;
    }
}
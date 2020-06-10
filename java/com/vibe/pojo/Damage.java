package com.vibe.pojo;

import java.util.List;

//实测录入，病害分析
public class Damage {

    private int id;
    private int type;
    private String damagePosition;
    private String damageType1;
    private String damageType2;
    private String photo;
    private List<String> photos;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDamagePosition() {
        return damagePosition;
    }

    public void setDamagePosition(String damagePosition) {
        this.damagePosition = damagePosition;
    }

    public String getDamageType1() {
        return damageType1;
    }

    public void setDamageType1(String damageType1) {
        this.damageType1 = damageType1;
    }

    public String getDamageType2() {
        return damageType2;
    }

    public void setDamageType2(String damageType2) {
        this.damageType2 = damageType2;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}

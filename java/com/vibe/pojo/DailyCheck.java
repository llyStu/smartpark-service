package com.vibe.pojo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vibe.util.CatalogIds;


public class DailyCheck {
    private String deviceName;


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String typeStr;
    private int id;
    private int type = 1;
    private int checkType;
    private String date;
    private String photo;
    private List<String> photos;
    private String description;
    private int person;
    private String personName;
    private String result;
    private Integer deviceId;

    private List<Integer> catalogId = Arrays.asList(CatalogIds.DAILYCHECK_CATALOGIDS);
    //在setType方法中，给type赋值的时候顺便把selectedId也赋值
    private List<Integer> selectedIdList = new ArrayList<Integer>();
    private int checkStatus;//与手机端对接，0代表可修改，1代表不可修改
    private int checkState;//审核状态
    private int state;//0删除,1待审核，2审核通过，3驳回， 4完成

    private String zhiPaiUserName;


    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getZhiPaiUserName() {
        return zhiPaiUserName;
    }

    public void setZhiPaiUserName(String zhiPaiUserName) {
        this.zhiPaiUserName = zhiPaiUserName;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
        selectedIdList.add(checkType);
    }

    public List<Integer> getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(List<Integer> catalogId) {
        this.catalogId = catalogId;
    }

    public List<Integer> getSelectedIdList() {

        return selectedIdList;
    }

    public void setSelectedIdList(List<Integer> selectedIdList) {
        this.selectedIdList = selectedIdList;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }


}

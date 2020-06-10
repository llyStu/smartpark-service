package com.vibe.pojo;

import java.util.List;

public class BlueCardParkBean {
    private String parkNumber;
    private String parkName;
    private int spaceCount;
    private int bookSpaceCount;
    private int bookInParkCount;
    private int freeSpaceCount;
    private List<BlueCardParkAreaBean> areaList;

    public List<BlueCardParkAreaBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<BlueCardParkAreaBean> areaList) {
        this.areaList = areaList;
    }

    public String getParkNumber() {
        return parkNumber;
    }

    public void setParkNumber(String parkNumber) {
        this.parkNumber = parkNumber;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public int getSpaceCount() {
        return spaceCount;
    }

    public void setSpaceCount(int spaceCount) {
        this.spaceCount = spaceCount;
    }

    public int getBookSpaceCount() {
        return bookSpaceCount;
    }

    public void setBookSpaceCount(int bookSpaceCount) {
        this.bookSpaceCount = bookSpaceCount;
    }

    public int getBookInParkCount() {
        return bookInParkCount;
    }

    public void setBookInParkCount(int bookInParkCount) {
        this.bookInParkCount = bookInParkCount;
    }

    public int getFreeSpaceCount() {
        return freeSpaceCount;
    }

    public void setFreeSpaceCount(int freeSpaceCount) {
        this.freeSpaceCount = freeSpaceCount;
    }

}

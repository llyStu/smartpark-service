package com.vibe.pojo.dahua;

import org.snmp4j.security.PrivAES;

public class ParkinglotInfo {

    private String parkingLot;
    private int carNum;
    private int entranceNum;
    private int exitusNum;
    private int totalLot;
    private int usedLot;
    private int ledNum;

    public String getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(String parkingLot) {
        this.parkingLot = parkingLot;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public int getEntranceNum() {
        return entranceNum;
    }

    public void setEntranceNum(int entranceNum) {
        this.entranceNum = entranceNum;
    }

    public int getExitusNum() {
        return exitusNum;
    }

    public void setExitusNum(int exitusNum) {
        this.exitusNum = exitusNum;
    }

    public int getTotalLot() {
        return totalLot;
    }

    public void setTotalLot(int totalLot) {
        this.totalLot = totalLot;
    }

    public int getUsedLot() {
        return usedLot;
    }

    public void setUsedLot(int usedLot) {
        this.usedLot = usedLot;
    }

    public int getLedNum() {
        return ledNum;
    }

    public void setLedNum(int ledNum) {
        this.ledNum = ledNum;
    }


}

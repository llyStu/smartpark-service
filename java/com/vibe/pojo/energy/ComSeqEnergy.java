package com.vibe.pojo.energy;

public class ComSeqEnergy {
    private int xulie;
    private String date;
    private Object dqzh;//当期综合能耗
    private Object tqzh;//1同期综合
    private Object sqzh;//上期综合
    private double tbzz;//同比增长
    private double hbzz;//环比增长

    public int getXulie() {
        return xulie;
    }

    public void setXulie(int xulie) {
        this.xulie = xulie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getDqzh() {
        return dqzh;
    }

    public void setDqzh(Object dqzh) {
        this.dqzh = dqzh;
    }

    public Object getTqzh() {
        return tqzh;
    }

    public void setTqzh(Object tqzh) {
        this.tqzh = tqzh;
    }

    public Object getSqzh() {
        return sqzh;
    }

    public void setSqzh(Object sqzh) {
        this.sqzh = sqzh;
    }

    public double getTbzz() {
        return tbzz;
    }

    public void setTbzz(double tbzz) {
        this.tbzz = tbzz;
    }

    public double getHbzz() {
        return hbzz;
    }

    public void setHbzz(double hbzz) {
        this.hbzz = hbzz;
    }

    public ComSeqEnergy() {

    }

    public ComSeqEnergy(double dqzh, double tqzh, double sqzh) {
        this.dqzh = dqzh;
        this.tqzh = tqzh;
        this.sqzh = sqzh;
    }

}

package com.vibe.pojo.energy;

public class Biao {
    private Double shuzhi;
    private int xuhao;
    private String shijian;

    public Double getShuzhi() {
        return shuzhi;
    }

    public void setShuzhi(Double shuzhi) {
        this.shuzhi = shuzhi;
    }

    public int getXuhao() {
        return xuhao;
    }

    public void setXuhao(int xuhao) {
        this.xuhao = xuhao;
    }

    public String getShijian() {
        return shijian;
    }

    public void setShijian(String shijian) {
        this.shijian = shijian;
    }

    public Biao() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Biao(Double shuzhi, int xuhao, String shijian) {
        super();
        this.shuzhi = shuzhi;
        this.xuhao = xuhao;
        this.shijian = shijian;
    }

}

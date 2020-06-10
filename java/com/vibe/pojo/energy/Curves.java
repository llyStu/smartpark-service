package com.vibe.pojo.energy;

import java.util.Map;

public class Curves {

    private int type;
    private Double total;
    private Map<String, Object> curve;
    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Curves() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Map<String, Object> getCurve() {
        return curve;
    }

    public void setCurve(Map<String, Object> curve) {
        this.curve = curve;
    }


}

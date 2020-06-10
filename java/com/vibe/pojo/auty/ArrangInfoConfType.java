package com.vibe.pojo.auty;

public class ArrangInfoConfType {
    private String value, text;

    public ArrangInfoConfType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public ArrangInfoConfType() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
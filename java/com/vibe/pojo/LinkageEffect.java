package com.vibe.pojo;

public class LinkageEffect {
    private int id;
    private int assetId;
    private int causeId;
    private int value;
    private String assetStr;
    private String valueStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getCauseId() {
        return causeId;
    }

    public void setCauseId(int causeId) {
        this.causeId = causeId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getAssetStr() {
        return assetStr;
    }

    public void setAssetStr(String assetStr) {
        this.assetStr = assetStr;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

}

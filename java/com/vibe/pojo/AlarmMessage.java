package com.vibe.pojo;

public class AlarmMessage {
    private int assetId;
    private int id;
    private String caption;
    private int state;
    private String errorMessage;
    private String startTime;
    private String duration;
    private int auto;
    private int handled;
    private String endTime;
    private int alarmRuleId;

    private String system;


    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getAlarmRuleId() {
        return alarmRuleId;
    }

    public void setAlarmRuleId(int alarmRuleId) {
        this.alarmRuleId = alarmRuleId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        String newDate = startTime.substring(0, startTime.length() - 2);
        this.startTime = newDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        String newDate = endTime.substring(0, endTime.length() - 2);
        this.endTime = newDate;
    }

    public int getHandled() {
        return handled;
    }

    public void setHandled(int handled) {
        this.handled = handled;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

package com.vibe.pojo;

import java.util.List;

public class TreeAlarmData {
    private int id;
    private String text;
    private List<TreeAlarmData> children;
    private AlarmData alarm;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public List<TreeAlarmData> getChildren() {
        return children;
    }


    public void setChildren(List<TreeAlarmData> children) {
        this.children = children;
    }


    public AlarmData getAlarm() {
        return alarm;
    }


    public void setAlarm(AlarmData alarm) {
        this.alarm = alarm;
    }


    public class AlarmData {
        private int rule;
        private int way;
        private int start;

        public int getRule() {
            return rule;
        }

        public void setRule(int rule) {
            this.rule = rule;
        }

        public int getWay() {
            return way;
        }

        public void setWay(int way) {
            this.way = way;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

    }
}

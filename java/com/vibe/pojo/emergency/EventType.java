package com.vibe.pojo.emergency;

public class EventType {
    private String type, level;

    public void withLevel(int level, int maxLevel) {
        this.level = (level - 1) * 60 / maxLevel + "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

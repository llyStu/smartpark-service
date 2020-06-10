package com.vibe.pojo.emergency;

public class Action {
    public static final String TARGET_SELF = "__TARGET_SELF__";
    public static final String TARGET_SRC = "__TARGET_SRC__";

    private String name, desc;
    private String defaultTarget;

    public static Action of(String name, String desc) {
        Action action = new Action();
        action.setName(name);
        action.setDesc(desc);
        action.setDefaultTarget(TARGET_SRC);
        return action;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDefaultTarget() {
        return defaultTarget;
    }

    public void setDefaultTarget(String defaultTarget) {
        this.defaultTarget = defaultTarget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHandler() {
        return false;
    }

    @Override
    public String toString() {
        return "Action [name=" + name + ", desc=" + desc + ", defaultTarget=" + defaultTarget + "]";
    }
}

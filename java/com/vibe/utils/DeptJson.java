package com.vibe.utils;

import java.io.Serializable;

public class DeptJson implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -112465893100894002L;
    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String text;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DeptJson() {
        super();
        // TODO Auto-generated constructor stub
    }

}

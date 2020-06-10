package com.vibe.pojo.spacemodel;

import java.util.List;

public class SpaceModelName {
    private Integer catalog;
    private String filename, name, nickname;
    private List<Integer> cas;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "SpaceModelName [filename=" + filename + ", name=" + name + ", nickname=" + nickname + "]";
    }

    public Integer getCatalog() {
        return catalog;
    }

    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
    }

    public List<Integer> getCas() {
        return cas;
    }

    public void setCas(List<Integer> cas) {
        this.cas = cas;
    }
}
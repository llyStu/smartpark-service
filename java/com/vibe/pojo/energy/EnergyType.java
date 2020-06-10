package com.vibe.pojo.energy;

import java.util.List;

public class EnergyType {
    private int id;
    private int parent;
    private String name;

    private List<EnergyType> energyType;

    public List<EnergyType> getEnergyType() {
        return energyType;
    }

    public void setEnergyType(List<EnergyType> energyType) {
        this.energyType = energyType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

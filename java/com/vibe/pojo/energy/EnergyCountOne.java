package com.vibe.pojo.energy;

public class EnergyCountOne {
    private Integer id;
    private String moment;
    private Double avg;
    private String name;
    private String unit;

    private Integer monitor;
    private Integer itemize_type;
    private String caption;
    private String captionOne;
    private Double avgNumNight;
    private Double avgnight_max;
    private Double avgnight_min;
    private Integer parent;
    private int page;
    private int size;
    private Integer floorId;


    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getMonitor() {
        return monitor;
    }

    public void setMonitor(Integer monitor) {
        this.monitor = monitor;
    }

    public Integer getItemize_type() {
        return itemize_type;
    }

    public void setItemize_type(Integer itemize_type) {
        this.itemize_type = itemize_type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaptionOne() {
        return captionOne;
    }

    public void setCaptionOne(String captionOne) {
        this.captionOne = captionOne;
    }

    public Double getAvgNumNight() {
        return avgNumNight;
    }

    public void setAvgNumNight(Double avgNumNight) {
        this.avgNumNight = avgNumNight;
    }

    public Double getAvgnight_max() {
        return avgnight_max;
    }

    public void setAvgnight_max(Double avgnight_max) {
        this.avgnight_max = avgnight_max;
    }

    public Double getAvgnight_min() {
        return avgnight_min;
    }

    public void setAvgnight_min(Double avgnight_min) {
        this.avgnight_min = avgnight_min;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public EnergyCountOne(String moment, Double avg, String name, String unit) {
        super();
        this.moment = moment;
        this.avg = avg;
        this.name = name;
        this.unit = unit;
    }

    public EnergyCountOne() {
        super();
        // TODO Auto-generated constructor stub
    }


}

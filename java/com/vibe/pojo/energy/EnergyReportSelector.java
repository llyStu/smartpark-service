package com.vibe.pojo.energy;

import java.text.ParseException;
import java.util.Date;

import com.vibe.scheduledtasks.report_statisticstask.StatisticsType;

public class EnergyReportSelector {
    private String start;
    private String end;
    private String type, catalog;
    private Integer parentSpace, exec, id, catalogId, numType;

    public EnergyReportSelector withExec(int exec) {
        this.exec = exec;
        return this;
    }

    public int catalog() {
        switch (catalog) {
            case "electricity":
                return 34;
            case "water":
                return 37;
            case "gas":
                return 38;
        }
        throw new IllegalArgumentException("不支持的资源类型: catalog = [electricity, water, gas]");
    }

    public StatisticsType srcTable() {
        if (this.numType != null) {
            switch (this.numType) {
                case 2:
                    return StatisticsType.FLOAT_DAILY;
                case 3:
                    return StatisticsType.FLOAT_MONTHLY;
                case 4:
                    return StatisticsType.FLOAT_YEAR;
            }
        }
        switch (this.type) {
            case "daily":
                return StatisticsType.FLOAT_DAILY;
            case "monthly":
                return StatisticsType.FLOAT_MONTHLY;
            case "year":
                return StatisticsType.FLOAT_YEAR;
        }
        throw new IllegalArgumentException("选择年月日类型错误: type = [daily, monthly, year]");
    }

    public Date start() throws ParseException {
        return this.srcTable().parse(this.start);
    }

    public Date end() throws ParseException {
        return this.srcTable().parse(this.end);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Integer getParentSpace() {
        return parentSpace;
    }

    public void setParentSpace(Integer parentSpace) {
        this.parentSpace = parentSpace;
    }

    public Integer getExec() {
        return exec;
    }

    public void setExec(Integer exec) {
        this.exec = exec;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public Integer getNumType() {
        return numType;
    }

    public void setNumType(Integer numType) {
        this.numType = numType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

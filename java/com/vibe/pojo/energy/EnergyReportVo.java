package com.vibe.pojo.energy;

import java.util.Date;
import java.util.List;

public class EnergyReportVo {
    private String srcTable;
    private Integer catalog, spaceId, probeId;
    private Date start, end;
    private List<Integer> spaceIds;

    public String getSrcTable() {
        return srcTable;
    }

    public void setSrcTable(String srcTable) {
        this.srcTable = srcTable;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<Integer> getSpaceIds() {
        return spaceIds;
    }

    public void setSpaceIds(List<Integer> spaceIds) {
        this.spaceIds = spaceIds;
    }

    public Integer getCatalog() {
        return catalog;
    }

    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getProbeId() {
        return probeId;
    }

    public void setProbeId(Integer probeId) {
        this.probeId = probeId;
    }
}
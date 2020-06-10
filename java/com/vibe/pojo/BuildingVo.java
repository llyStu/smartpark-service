package com.vibe.pojo;
//F本体情况大类，包含所有字段，用于录入和查询
public class BuildingVo extends Check {
	
    private Integer bId;

    private Integer building_id;

    private String evaluation;

    private String protectStatus;

    private String statusDescription;

    private Integer b_c_id;

    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    public Integer getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Integer building_id) {
        this.building_id = building_id;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation == null ? null : evaluation.trim();
    }

    public String getProtectStatus() {
        return protectStatus;
    }

    public void setProtectStatus(String protectStatus) {
        this.protectStatus = protectStatus == null ? null : protectStatus.trim();
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription == null ? null : statusDescription.trim();
    }

    public Integer getB_c_id() {
        return b_c_id;
    }

    public void setB_c_id(Integer b_c_id) {
        this.b_c_id = b_c_id;
    }
}
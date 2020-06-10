package com.vibe.pojo;

import java.time.LocalDate;

public class Task {

    private Integer taskId;//任务表id

    private String name;
    private Integer taskType;//任务类型id
    private Integer responsiblePeople;//负责人id
    private Integer taskInterval;//维护周期
    private Integer grade;//等级id
    private LocalDate createTime;//创建时间
    private Integer state;//任务状态 0-已删除 1-生成任务 2-已接收 3-完成
    private Integer maintenanceType;//维护类型
    private LocalDate maintenanceTime;//维护时间
    private String taskMemo;//任务的详情
    private String resultDesc;//处理详情
    private String picture;//图片的路径
    private String finishTime;//要求完成时间
    private String responsibleName;//负责人的名称
    private String maintenanceDate;//设备维保日期
    private String createDate;//任务创建的日期

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Task() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getResponsiblePeople() {
        return responsiblePeople;
    }

    public void setResponsiblePeople(Integer responsiblePeople) {
        this.responsiblePeople = responsiblePeople;
    }

    public Integer getTaskInterval() {
        return taskInterval;
    }

    public void setTaskInterval(Integer taskInterval) {
        this.taskInterval = taskInterval;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(Integer maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public LocalDate getMaintenanceTime() {
        return maintenanceTime;
    }

    public void setMaintenanceTime(LocalDate maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public String getTaskMemo() {
        return taskMemo;
    }

    public void setTaskMemo(String taskMemo) {
        this.taskMemo = taskMemo;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


}

package com.vibe.pojo;

import java.util.Date;

/*
 * Class providing date properties for schedual object
 * */
public class SchedualPattern {

    private Date createTime;//工单开始时间
    private Date expectStartTime;//预计开始时间
    private Date expectEndTime;//预计结束时间
    private Date deadline;//截止时间
    private Date actualStartTime;//实际开始时间
    private Date actualEndTime;//实际结束时间
    private Date sendingTime;//任务派发时间时间
    private Date expectDuration;//实际结束时间
    private Date actualDuration;//任务派发时间时间

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(Date expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public Date getExpectEndTime() {
        return expectEndTime;
    }

    public void setExpectEndTime(Date expectEndTime) {
        this.expectEndTime = expectEndTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Date getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Date sendingTime) {
        this.sendingTime = sendingTime;
    }

    public Date getExpectDuration() {
        return expectDuration;
    }

    public void setExpectDuration(Date expectDuration) {
        this.expectDuration = expectDuration;
    }

    public Date getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Date actualDuration) {
        this.actualDuration = actualDuration;
    }


}

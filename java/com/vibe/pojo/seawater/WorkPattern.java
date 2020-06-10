package com.vibe.pojo.seawater;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.vibe.pojo.user.User;
import com.vibe.utils.time.TimeCalculate;


public class WorkPattern {

    private Integer id;
    private String name;//工况名称
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;//创建时间
    private String data;
    private int state;//状态0-未运行，1-运行中
    private int delFalg;//标记：0-正常，1隐藏，2删除
    private int userId;
    private User user;//用户
    private List<WorkPeriod> workPeriods;
    /**
     * 临时字段
     */
    private int workNumber;//模拟次数
    private String sumTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastWorkTime;
    private int sort;//排序1:模拟次数;2：累计时间;3:最后一次模拟的结束时间 （默认0:创建时间；）
    private long sortSumtime;//累计时间
    private long sortLastWorkTime;//最后一次模拟的结束时间
    private long sortCreationTime;//创建时间
    private int sortType;//0：升序 1：降序

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(int workNumber) {
        this.workNumber = workNumber;
    }

    public List<WorkPeriod> getWorkPeriods() {
        return workPeriods;
    }

    public void setWorkPeriods(List<WorkPeriod> workPeriods) {
        this.workPeriods = workPeriods;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(long sumTime) {
        int day = (int) (sumTime / (24 * 60 * 60 * 1000));
        int hour = (int) (sumTime / (60 * 60 * 1000) - day * 24);
        int min = (int) ((sumTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        int s = (int) (sumTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        TimeCalculate time = new TimeCalculate(day, hour, min, s);
        this.sumTime = time.toString();
    }

    public Date getLastWorkTime() {
        return lastWorkTime;
    }

    public void setLastWorkTime(Date lastWorkTime) {
        this.lastWorkTime = lastWorkTime;
    }

    public long getSortSumtime() {
        return sortSumtime;
    }

    public void setSortSumtime(long sortSumtime) {
        this.sortSumtime = sortSumtime;
    }

    public long getSortLastWorkTime() {
        return sortLastWorkTime;
    }

    public void setSortLastWorkTime(long sortLastWorkTime) {
        this.sortLastWorkTime = sortLastWorkTime;
    }

    public long getSortCreationTime() {
        return sortCreationTime;
    }

    public void setSortCreationTime(long sortCreationTime) {
        this.sortCreationTime = sortCreationTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDelFalg() {
        return delFalg;
    }

    public void setDelFalg(int delFalg) {
        this.delFalg = delFalg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int compareTo(WorkPattern o, int sorts, int sortType) {
        final int before = 1;
        final int laft = -1;
        switch (sorts) {
            case 0:
                if (o.getSortCreationTime() > this.getSortCreationTime()) {
                    return sortType == 1 ? before : laft;
                } else if (o.getSortCreationTime() < this.getSortCreationTime()) {
                    return sortType == 1 ? laft : before;
                }
                break;
            case 1:
                if (o.getWorkNumber() > this.getWorkNumber()) {
                    return sortType == 1 ? before : laft;
                } else if (o.getWorkNumber() < this.getWorkNumber()) {
                    return sortType == 1 ? laft : before;
                }
                break;
            case 2:
                if (o.getSortSumtime() > this.getSortSumtime()) {
                    return sortType == 1 ? before : laft;
                } else if (o.getSortSumtime() < this.getSortSumtime()) {
                    return sortType == 1 ? laft : before;
                }
                break;
            case 3:
                if (o.getSortLastWorkTime() > this.getSortLastWorkTime()) {
                    return sortType == 1 ? before : laft;
                } else if (o.getSortLastWorkTime() < this.getSortLastWorkTime()) {
                    return sortType == 1 ? laft : before;
                }
                break;
        }
        return 0;

    }


}

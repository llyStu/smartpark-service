package com.vibe.pojo.auty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.format.annotation.DateTimeFormat;

public class ArrangInfoConf {
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;
    private String memo, type;
    private int[] staff;
    private int[] ids;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Set<Date> skipsDate = Collections.emptySet();

    private Integer pageNum, pageSize;

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStaffAsString() {
        if (staff == null) return null;
        return String.join(",", IntStream.of(staff).mapToObj(Integer::toString).toArray(v -> new String[v]));
    }

    public void setStaffAsString(String staffAsString) {
        if (staffAsString == null) {
            this.staff = null;
            return;
        }
        String[] split = staffAsString.split(",");
        this.staff = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getStaff() {
        return staff;
    }

    public void setStaff(int[] staff) {
        this.staff = staff;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public Set<Date> getSkipsDate() {
        return skipsDate;
    }

    public void setSkipsDate(Set<Date> skipsDate) {
        this.skipsDate = skipsDate;
    }

    public String getSkipsDateAsString() {
        if (skipsDate == null || skipsDate.isEmpty()) return null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.join(",", skipsDate.stream().map(df::format).toArray(v -> new String[v]));
    }

    public void setSkipsDateAsString(String skipsDateString) {
        if (skipsDateString == null || (skipsDateString = skipsDateString.trim()).isEmpty()) {
            this.skipsDate = Collections.emptySet();
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.skipsDate = new HashSet<>(Arrays.asList(Arrays.stream(skipsDateString.split(",")).map(t -> {
            try {
                return df.parse(t);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }).toArray(v -> new Date[v])));
    }


}

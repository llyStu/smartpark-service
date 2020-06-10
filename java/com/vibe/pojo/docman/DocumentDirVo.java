package com.vibe.pojo.docman;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DocumentDirVo {
    private String name, number, uname, filename;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified_min;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified_max;
    private String desc, attach;
    private Integer pid, id;
    private Integer[] pids;
    private Integer pageNum, pageSize;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Date getModified_max() {
        return modified_max;
    }

    public void setModified_max(Date modified_max) {
        this.modified_max = modified_max;
    }

    public Date getModified_min() {
        return modified_min;
    }

    public void setModified_min(Date modified_min) {
        this.modified_min = modified_min;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public Integer[] getPids() {
        return pids;
    }

    public void setPids(Integer[] pids) {
        this.pids = pids;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}

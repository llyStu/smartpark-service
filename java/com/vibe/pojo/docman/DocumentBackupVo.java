package com.vibe.pojo.docman;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DocumentBackupVo extends DocumentDirVo {
    private Integer bid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date backup_min;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date backup_max;
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Date getBackup_max() {
        return backup_max;
    }

    public void setBackup_max(Date backup_max) {
        this.backup_max = backup_max;
    }

    public Date getBackup_min() {
        return backup_min;
    }

    public void setBackup_min(Date backup_min) {
        this.backup_min = backup_min;
    }
}

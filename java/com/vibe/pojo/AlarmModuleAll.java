package com.vibe.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author liujingeng
 * @description Alarmshitilei
 * @create 2020/02/07
 */
@Data
public class AlarmModuleAll {

    private int doNum;

    private int allNum;

    private List<AlarmModule> alarmModuleList;

    public AlarmModuleAll(int doNum, int allNum, List<AlarmModule> alarmModuleList) {
        this.doNum = doNum;
        this.allNum = allNum;
        this.alarmModuleList = alarmModuleList;
    }
}
package com.vibe.pojo;

import lombok.Data;

@Data
public class TimeAvgNum {

    private String time;

    private Integer num;


    public TimeAvgNum(String time, int num) {
        this.time = time;
        this.num = num;
    }
}
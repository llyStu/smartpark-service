package com.vibe.pojo;

import lombok.Data;

/**
 * @author liujingeng
 * @description Alarm实体类
 * @create 2020/02/07
 */
@Data
public class AlarmModule {

    private String id;

    private String name;

    private int number;

    public AlarmModule(String id, String name) {
        this.id = id;
        this.name = name;
        this.number = 0;
    }
}
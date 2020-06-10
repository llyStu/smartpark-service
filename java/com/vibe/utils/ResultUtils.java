package com.vibe.utils;


import com.vibe.pojo.AvgNum;
import com.vibe.pojo.TimeAvgNum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultUtils {

    /**
     * 格式化按小时取值结果，补充为空时间段数量
     * @param list 格式化list
     * @param time 终止时间
     * @return list
     * @Author liujingeng
     * @Date 2020/5/7
     */
    public static List<TimeAvgNum> avgResultFormat(List<AvgNum> list, Date time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(dateFormat.format(time));
        List<TimeAvgNum> resultList = new ArrayList<>();
        AvgNum avgNum;
        int p = 0;
        for (AvgNum num : list) {
            avgNum = num;
            while (p < avgNum.getTime()) {
                resultList.add(new TimeAvgNum(p + ":00", 0));
                p++;
            }
            resultList.add(new TimeAvgNum(p + ":00", avgNum.getNum()));
            p++;
        }
        while (p <= hour) {
            resultList.add(new TimeAvgNum(p + ":00", 0));
            p++;
        }
        return resultList;
    }

}
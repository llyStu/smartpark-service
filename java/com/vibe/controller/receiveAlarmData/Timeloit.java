package com.vibe.controller.receiveAlarmData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.service.receiveAlarmData.TimeloitAlarmData;
import com.vibe.service.receiveAlarmData.TimeloitService;

@Controller
public class Timeloit {
    @Autowired
    private TimeloitService timeloitService;

    @RequestMapping("/timeloit/alarm")
    @ResponseBody
    public String receiveTimeloitAlarmData(TimeloitAlarmData data) {
        try {
            timeloitService.receiveData(data);
            return "200";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "500";
        }


    }
}


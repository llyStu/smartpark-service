package com.vibe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.Response;
import com.vibe.pojo.TimeControlBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlLogBean;
import com.vibe.service.timecontrol.TimeControlService;
import com.vibe.util.ResponseResult;

@Controller
public class TimeControlController {
    @Autowired
    private TimeControlService timeControlService;

    @RequestMapping("/queryTimeControlList")
    @ResponseBody
    public List<TimeControlBean> queryTimeControlList() {
        return timeControlService.queryTimeControlList();
    }

    @RequestMapping("/insertTimeControl")
    @ResponseBody
    public TimeControlBean insertTimeControl(@RequestBody TimeControlBean timeControlBean) {
        return timeControlService.insertTimeControl(timeControlBean);
    }

    /**
     * 删除、启动、停止 三个功能的通用接口
     *
     * @param ids   被 删除、启动、停止的TimeControl的id
     * @param state -1代表删除 0代表停止 1代表启动
     * @return
     */
    @RequestMapping("/deleteTimeControl")
    @ResponseBody
    public Response deleteTimeControl(@RequestParam("id") int id) {
        try {
            timeControlService.deleteTimeControl(id);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }
    }

    @RequestMapping("/updateTimeControl")
    @ResponseBody
    public TimeControlBean updateTimeControl(@RequestBody TimeControlBean timeControlBean) {
        return timeControlService.updateTimeControl(timeControlBean);
    }

    @RequestMapping("/queryTimeControlLog")
    @ResponseBody
    public List<TimeControlLogBean> queryTimeControlLog(@RequestParam("id") int id) {
        return timeControlService.queryTimeControlLog(id);
    }
}

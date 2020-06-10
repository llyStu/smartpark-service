package com.vibe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.monitor.server.MonitorServer;
import com.vibe.pojo.Response;
import com.vibe.util.ResponseResult;

@Controller
public class ControlController {
    @Autowired
    private MonitorServer monitorServer;

    @RequestMapping("/execControlCommand")
    @ResponseBody
    public Response execControlCommand(@RequestParam("id") int id, @RequestParam("cmd") String cmd) {
        String execControlCommand = monitorServer.execControlCommand(id, cmd);
        Boolean result = execControlCommand.contains("failed") ? false : true;
        System.out.println("命令返回的结果：" + result);
        return ResponseResult.getANewResponse(result);
    }

}

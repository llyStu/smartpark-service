package com.vibe.controller.global;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.SystemSettingBean;
import com.vibe.service.global.SystemService;

@Controller
public class SystemController {
    @Autowired
    private SystemService systemService;

    @RequestMapping("/getSystemSettings")
    @ResponseBody
    public List<SystemSettingBean> getSystemSettings() {
        return systemService.getSystemSettings();
    }

}

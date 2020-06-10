package com.vibe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;

@Controller
public class ThirdPartyController {

    /**
     * 短信接口预留
     *
     * @return
     */
    @RequestMapping("/thirdparty/transmitter_note")
    @ResponseBody
    public FormJson transmitterNote() {
        return FormJsonBulider.fail("短信发送失败");
    }

}

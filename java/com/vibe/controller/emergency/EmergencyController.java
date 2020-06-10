package com.vibe.controller.emergency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.emergency.EmergencyEventType;
import com.vibe.pojo.emergency.EmergencyUser;
import com.vibe.service.emergency.EmergencyService;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Controller
public class EmergencyController {

    @Autowired
    private EmergencyService emergencyService;

    @RequestMapping("/emergency/findAll_EventType")
    @ResponseBody
    public List<EmergencyEventType> findAllEventType() {
        return emergencyService.findAllEventType();
    }

    @RequestMapping("/emergency/findAll_EventGrade")
    @ResponseBody
    public List<EmergencyEventType> findAllEventGrade() {
        return emergencyService.findAllEventGrade();
    }

    @RequestMapping("/emergency/findAll_TypeGrade_Lead")
    @ResponseBody
    public List<EmergencyUser> findAllTypeGradeOfLead(Integer etid, Integer egid) {
        return emergencyService.findAllTypeGradeOfLead(etid, egid);
    }

    @RequestMapping("/emergency/findAll_limit_lead")
    @ResponseBody
    public Page<EmergencyUser> findAllLimitUser(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int rows, EmergencyUser emergencyUser) {
        return emergencyService.findAllLimitUser(page, rows, emergencyUser);

    }

    @RequestMapping("/emergency/delete_lead")
    @ResponseBody
    public FormJson deleteLead(int[] ids) {
        return emergencyService.deleteLead(ids);
    }

    @RequestMapping("/emergency/add_lead")
    @ResponseBody
    public FormJson addLead(EmergencyUser emergencyUser) {
        int addLead = emergencyService.addLead(emergencyUser);
        if (1 == addLead) {
            return FormJsonBulider.success();
        }
        return FormJsonBulider.fail("人员添加失败");
    }

    @RequestMapping("/emergency/update_lead")
    @ResponseBody
    public FormJson updateLead(EmergencyUser emergencyUser) {
        return emergencyService.updateLead(emergencyUser);
    }

    @RequestMapping("/emergency/update_udefault")
    @ResponseBody
    public FormJson updateUdefault(EmergencyUser emergencyUser) {
        return emergencyService.updateUdefault(emergencyUser);
    }

}

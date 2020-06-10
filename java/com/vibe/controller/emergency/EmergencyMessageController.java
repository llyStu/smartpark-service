package com.vibe.controller.emergency;

import javax.servlet.http.HttpSession;

import com.vibe.utils.FormJsonBulider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vibe.pojo.user.User;
import com.vibe.pojo.emergency.EmergencyMessage;
import com.vibe.service.emergency.EmergencyMessageService;
import com.vibe.utils.FormJson;
import com.vibe.utils.Page;

@RestController
public class EmergencyMessageController {


    @Autowired
    private EmergencyMessageService emergencyMessageService;


    /**
     * 查询突发事件信息
     *
     * @param emergencyMessage
     * @return
     */
    @RequestMapping("/emergencyMessage/findAll_emergencyMessage")
    public Page<EmergencyMessage> findAllEmergencyMessage(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int rows, EmergencyMessage emergencyMessage) {
        return emergencyMessageService.findAllEmergencyMessage(page, rows, emergencyMessage);
    }

    @RequestMapping("/emergencyMessage/add_emergencyMessage")
    public FormJson addEmergencyMessage(EmergencyMessage emergencyMessage, HttpSession session) {
        if (emergencyMessage.getDescription().length() >= 300) {
            return FormJsonBulider.fail("请输入300字以内的内容描述");
        }
        User user = (User) session.getAttribute("loginUser");
        emergencyMessage.setRegistrant(user.getName());
        return emergencyMessageService.addEmergencyMessage(emergencyMessage);
    }

    @RequestMapping("/emergencyMessage/findOne_EmergencyMessage")
    public EmergencyMessage findOneEmergencyMessage(Integer id) {
        return emergencyMessageService.findOneEmergencyMessage(id);

    }

    @RequestMapping("/emergencyMessage/update_EmergencyMessage")
    public FormJson updateEmergencyMessage(EmergencyMessage emergencyMessage) {
        if (emergencyMessage.getDescription().length() >= 300) {
            return FormJsonBulider.fail("请输入300字以内的内容描述");
        }
        return emergencyMessageService.updateEmergencyMessage(emergencyMessage);
    }

    @RequestMapping("/emergencyMessage/delete_EmergencyMessage")
    public FormJson deleteEmergencyMessage(int[] ids) {
        return emergencyMessageService.deleteEmergencyMessage(ids);
    }
}

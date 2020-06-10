package com.vibe.controller.emergency;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.user.User;
import com.vibe.pojo.emergency.Emergency;
import com.vibe.pojo.emergency.EmergencyTask;
import com.vibe.pojo.emergency.EmergencyTaskDetail;
import com.vibe.pojo.emergency.EmergencyTaskDetailVo;
import com.vibe.pojo.emergency.EmergencyTaskVo;
import com.vibe.pojo.emergency.EmergencyType;
import com.vibe.pojo.emergency.EmergencyVo;
import com.vibe.service.emergency.EmergencyManagementService;
import com.vibe.utils.FormJson;
import com.vibe.utils.Page;
import com.vibe.utils.TreeNode;

@Controller
public class EmergencyManagementController {
    @Autowired
    private EmergencyManagementService ems;


    @RequestMapping("/emergency/setMonitors")
    @ResponseBody
    public FormJson setMonitors(Integer[] mid) {
        return ems.setMonitors(mid);
    }

    @RequestMapping("/emergency/getMonitors")
    @ResponseBody
    public List<TreeNode> getMonitors() {
        return ems.getMonitors();
    }

    @RequestMapping("/emergency/getMonitorsByMids")
    @ResponseBody
    public List<TreeNode> getMonitorsByMids(int[] mid) {
        return ems.getMonitorsByMids(mid);
    }

    @RequestMapping("/emergency/getEmergencyTypes")
    @ResponseBody
    public List<EmergencyType> getEmergencyTypes() {
        return ems.getEmergencyTypes();
    }

    @RequestMapping("/emergency/addEmergency")
    @ResponseBody
    public FormJson addEmergency(Emergency e, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        e.setUid(user.getId());
        return ems.addEmergency(e);
    }

    @RequestMapping("/emergency/deleteEmergency")
    @ResponseBody
    public FormJson deleteEmergency(int[] eid) {
        return ems.delEmergency(eid);
    }

    @RequestMapping("/emergency/updateEmergency")
    @ResponseBody
    public FormJson updateEmergency(Emergency e) {
        return ems.updateEmergency(e);
    }

    @RequestMapping("/emergency/findEmergency")
    @ResponseBody
    public Page<Emergency> findEmergency(EmergencyVo vo) {
        return ems.findEmergency(vo, vo.getPageNum(), vo.getPageSize());
    }

    @RequestMapping("/emergency/addEmergencyTask")
    @ResponseBody
    public FormJson addEmergencyTask(EmergencyTask e, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        e.setUid(user.getId());
        return ems.addEmergencyTask(e);
    }

    @RequestMapping("/emergency/updateEmergencyTask")
    @ResponseBody
    public FormJson updateEmergencyTask(EmergencyTask e) {
        return ems.updateEmergencyTask(e);
    }

    @RequestMapping("/emergency/deleteEmergencyTask")
    @ResponseBody
    public FormJson deleteEmergencyTask(int[] etid) {
        return ems.deleteEmergencyTask(etid);
    }

    @RequestMapping("/emergency/findEmergencyTask")
    @ResponseBody
    public Page<EmergencyTask> findEmergencyTask(EmergencyTaskVo vo) {
        return ems.findEmergencyTask(vo, vo.getPageNum(), vo.getPageSize());
    }

    @RequestMapping("/emergency/addEmergencyTaskDetail")
    @ResponseBody
    public FormJson addEmergencyTaskDetail(EmergencyTaskDetail e) {
        return ems.addEmergencyTaskDetail(e);
    }

    @RequestMapping("/emergency/deleteEmergencyTaskDetail")
    @ResponseBody
    public FormJson deleteEmergencyTaskDetail(int[] etdid) {
        return ems.delEmergencyTaskDetail(etdid);
    }

    @RequestMapping("/emergency/updateEmergencyTaskDetail")
    @ResponseBody
    public FormJson updateEmergencyTaskDetail(EmergencyTaskDetail e) {
        return ems.updateEmergencyTaskDetail(e);
    }

    @RequestMapping("/emergency/findEmergencyTaskDetail")
    @ResponseBody
    public Page<EmergencyTaskDetail> findEmergencyTaskDetail(EmergencyTaskDetailVo vo) {
        return ems.findEmergencyTaskDetail(vo, vo.getPageNum(), vo.getPageSize());
    }

    @RequestMapping("/emergency/getDisposeType")
    @ResponseBody
    public List<EmergencyType> getDisposeType() {
        return ems.getDisposeTypes();
    }

}

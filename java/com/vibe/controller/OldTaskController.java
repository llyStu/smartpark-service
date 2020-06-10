package com.vibe.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.pojo.MaintenaceVo;
import com.vibe.pojo.Task;
import com.vibe.service.task.OldTaskService;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.FormJson;

@Controller
public class OldTaskController {
    @Autowired
    private OldTaskService taskService;

    @Autowired
    private Application application;

    @RequestMapping("/task/maintenaceList")
    public @ResponseBody
    EasyUIJson queryMaintenaceList(Integer deviceId, @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer rows) {
        // 远程调用Service服务对象，获取员工列表数据
        EasyUIJson easyUIJson = taskService.queryMaintenaceListByPage(deviceId, page, rows);
        return easyUIJson;
    }

    @RequestMapping("/task/toMaintenaceAdd")
    public @ResponseBody
    String toMaintenaceAdd(MaintenaceVo maintenace, Integer deviceId) {
        IdGenerator<Integer> gen = application.getIntIdGenerator("task");

        // 获取维修记录数据，新增记录
        if (maintenace != null && maintenace.getTaskId() == null) {
            maintenace.setTaskId(gen.next());
        }
        //maintenace.setDeviceId(deviceId);

        taskService.addMaintenace(maintenace, deviceId);
        return "yes";
    }

    @RequestMapping("/task/toEditorMaintenace")
    public @ResponseBody
    MaintenaceVo toEditorMaintenace(MaintenaceVo maintenace) {
        MaintenaceVo vo = taskService.editorMaintenace(maintenace);
        return vo;


    }

    @RequestMapping("/task/deleteMaintenace")
    public @ResponseBody
    String deleteMaintenace(String ids) {
        // 用，号切成数组
        String[] ides = ids.split(",");
        //遍历数组
        for (String id : ides) {
            taskService.deleteMaintenace(Integer.parseInt(id));
        }

        return "200";
    }

    @RequestMapping("/task/maintenaceTypeList")
    public @ResponseBody
    List<DeptJson> maintenaceTypeList() {
        List<DeptJson> selectList = new ArrayList<DeptJson>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("维修");
        arrayList.add("清洁");
        arrayList.add("保养");
        for (int i = 0; i < arrayList.size(); i++) {
            DeptJson deptjson = new DeptJson();
            deptjson.setId(i + 1);
            deptjson.setText(arrayList.get(i));
            selectList.add(deptjson);
        }
        return selectList;
    }

    @RequestMapping("/task/maintenaceDateTypeList")
    public @ResponseBody
    List<DeptJson> maintenaceDateTypeList() {
        List<DeptJson> selectList = new ArrayList<DeptJson>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("每天");
        arrayList.add("每周");
        arrayList.add("每月");
        arrayList.add("每年");
        for (int i = 0; i < arrayList.size(); i++) {
            DeptJson deptjson = new DeptJson();
            deptjson.setId(i + 1);
            deptjson.setText(arrayList.get(i));
            selectList.add(deptjson);
        }
        return selectList;
    }

    /*任务管理模块*/
    @RequestMapping("/task/task_list")
    public @ResponseBody
    EasyUIJson queryTaskList(Task task, @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer rows) {
        // 远程调用Service服务对象，获取员工列表数据

        EasyUIJson easyUIJson = taskService.queryTaskListByPage(task, page, rows);
        return easyUIJson;
    }

    @RequestMapping("/task/other_task_add")
    public @ResponseBody
    String taskAdd(Task task, Model model) {
        try {
            IdGenerator<Integer> gen = application.getIntIdGenerator("task");
            task.setTaskId(gen.next());
            taskService.addOtherTask(task);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

    @RequestMapping("/task/to_other_task_edit")
    public @ResponseBody
    Task toTaskEdit(Integer id) {

        Task task = taskService.queryTaskById(id);
        return task;
    }


    @RequestMapping("/task/task_edit")
    public @ResponseBody
    List<FormJson> taskEdit(Task task) {
        List<FormJson> form = new ArrayList<FormJson>();
        FormJson json = new FormJson();
        try {
            taskService.editTaskById(task);
            json.setSuccess(true);
            json.setMessage("操作成功");
            form.add(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(true);
            json.setMessage("操作失败");
            form.add(json);
        }
        return form;
    }

    /*
     * 删除员工信息 请求：task/deletetask 方法：deletetask 参数：ids要删除的用户id,该部门id 返回值:String
     */
    @RequestMapping("/task/delete_task")
    public @ResponseBody
    String deleteTask(String ids) {
        taskService.deleteTask(ids);
        return "200";
    }


}

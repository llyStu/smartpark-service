package com.vibe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.PageResult;
import com.vibe.pojo.Response;
import com.vibe.scheduledtasks.CommonTaskBean;
import com.vibe.scheduledtasks.ScheduledTaskBean;
import com.vibe.service.task.TaskService;
import com.vibe.util.ResponseResult;

@Controller
public class TaskController {
    @Autowired
    private TaskService taskService;

    @RequestMapping("/queryScheduledTasks")
    @ResponseBody
    public PageResult<ScheduledTaskBean> queryScheduledTasks(@RequestBody(required = false) ScheduledTaskBean taskBean,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int rows) {
        PageHelper.startPage(page, rows);
        List<ScheduledTaskBean> scheduledTaskBeans = taskService.queryScheduledTasks(taskBean);
        PageResult<ScheduledTaskBean> pageResult = new PageResult<>();
        pageResult.setData(scheduledTaskBeans);
        pageResult.setTotal(((Page<ScheduledTaskBean>) scheduledTaskBeans).getTotal());
        return pageResult;
    }

    @RequestMapping("/updateScheduledTask")
    @ResponseBody
    public Response updateScheduledTask(@RequestBody ScheduledTaskBean taskBean) {
        try {
            taskService.updateScheduledTask(taskBean);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }
    }

    @RequestMapping("/insertScheduledTask")
    @ResponseBody
    public Response insertScheduledTask(@RequestBody ScheduledTaskBean taskBean) {


        try {
            taskService.insertScheduledTask(taskBean);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }
    }

    @RequestMapping("/deleteScheduledTask")
    @ResponseBody
    public Response deleteScheduledTask(@RequestParam("id") int id) {

        try {
            taskService.deleteScheduledTask(id);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }
    }

    @RequestMapping("/queryTasks")
    @ResponseBody
    public PageResult<CommonTaskBean> queryTasks(@RequestBody(required = false) CommonTaskBean taskBean, @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int rows) {
        PageHelper.startPage(page, rows);
        List<CommonTaskBean> taskBeans = taskService.queryTasks(taskBean);
        PageResult<CommonTaskBean> pageResult = new PageResult<>();
        pageResult.setData(taskBeans);
        pageResult.setTotal(((Page<CommonTaskBean>) taskBeans).getTotal());
        return pageResult;
    }

    @RequestMapping("/insertTask")
    @ResponseBody
    public Response insertTask(@RequestBody CommonTaskBean taskBean) {
        try {
            taskService.insertTask(taskBean);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }


    }

    @RequestMapping("/updateTask")
    @ResponseBody
    public Response updateTask(@RequestBody CommonTaskBean taskBean) {

        try {
            taskService.updateTask(taskBean);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }
    }

    @RequestMapping("/deleteTask")
    @ResponseBody
    public Response deleteTask(String ids) {
        try {
            String[] ides = ids.split(",");
            for (String id : ides) {
                taskService.deleteTask(Integer.parseInt(id));
            }
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }


    }


}

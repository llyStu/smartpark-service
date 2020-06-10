package com.vibe.controller.seawater;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.seawater.WorkPattern;
import com.vibe.pojo.seawater.WorkPeriod;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.User;
import com.vibe.service.seawater.WorkSimulatorService;
import com.vibe.service.user.UserService;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.FormJson;

@RestController
public class WorkSimulatorController {
    @Autowired
    private WorkSimulatorService workSimulatorService;
    @Autowired
    private UserService userService;

    /**
     * 添加工况
     *
     * @param request
     * @param workpattern
     * @return
     */
    @RequestMapping("/workSimulator/operationSimulatorWork")
    public FormJson operationSimulatorWork(HttpServletRequest request, WorkPattern workpattern) {
        User user = (User) request.getSession().getAttribute("loginUser");
        workpattern.setUser(user);
        return workSimulatorService.operationSimulatorWork(workpattern);
    }

    /**
     * 查询所有工况以及运行工况信息
     *
     * @param page
     * @param rows
     * @param workpattern
     * @param request
     * @return
     */
    @RequestMapping("/workSimulator/findAllSimulatorWork")
    public EasyUIJson findAllSimulatorWork(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int rows, WorkPattern workpattern, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("loginUser");
        List<Role> allRole = userService.getAllRole(user.getId());
        allRole.forEach((role) -> {
            if (role.getId() != 4) {
                workpattern.setUser(user);
                workpattern.setDelFalg(1);//非管理端查询隐藏数据
            }
        });
        return workSimulatorService.findAllSimulatorWork(page, rows, workpattern);
    }

    /**
     * 根据id删除工况
     *
     * @param ids
     * @return
     */
    @RequestMapping("/workSimulator/delSimulatorWork")
    public FormJson delSimulatorWork(int[] ids) {
        return workSimulatorService.delSimulatorWork(ids);
    }

    /**
     * 工况对比
     *
     * @param ids
     * @return
     */
    @RequestMapping("/workSimulator/findSimulatorWork")
    public List<WorkPattern> findSimulatorWork(@RequestParam(value = "ids", required = true) Integer[] ids) {//工况对比
        return workSimulatorService.findSimulatorWork(ids);
    }

    /**
     * 查询所有运行工况
     *
     * @param page
     * @param rows
     * @param workPeriod
     * @param request
     * @return
     */
    @RequestMapping("/workSimulator/findAllWorkPeriod")
    public EasyUIJson findAllWorkPeriod(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int rows, WorkPeriod workPeriod, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("loginUser");
        userService.getAllRole(user.getId()).forEach((role) -> {
            if (role.getId() != 4) {
                workPeriod.setUser(user);
                workPeriod.setFalg(1);//显示隐藏+正常数据
            }
        });
        return workSimulatorService.findAllWorkPeriod(page, rows, workPeriod);
    }

    /**
     * 管理端删除运行工况
     *
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping("/workSimulator/delWorkPeriod")
    public FormJson delWorkPeriod(int[] ids, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("loginUser");
        int falg = 2;
        List<Role> allRole = userService.getAllRole(user.getId());
        for (Role role : allRole) {
            if (role.getId() == 4) {
                falg = 1;
            }
        }
        return workSimulatorService.delWorkPeriod(ids, falg);
    }

    /**
     * 添加运行工况
     *
     * @param period
     * @return
     */
    @RequestMapping("/workSimulator/operationWorkPeriod")
    public FormJson operationWorkPeriod(WorkPeriod period) {
        return workSimulatorService.operationWorkPeriod(period);
    }

}

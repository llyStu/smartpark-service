package com.vibe.service.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.task.PageTaskMapper;
import com.vibe.mapper.user.UserMapper;
import com.vibe.pojo.MaintenaceVo;
import com.vibe.pojo.Task;
import com.vibe.pojo.user.User;
import com.vibe.utils.EasyUIJson;

@Service
public class OldTaskServiceimpl implements OldTaskService {
    @Autowired
    private PageTaskMapper taskMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据部门id，分页查询员工列表
     */
    @Override
    public EasyUIJson queryMaintenaceListByPage(Integer deviceId, Integer page, Integer rows) {
        List<Integer> taskIds = taskMapper.queryTaskIds(deviceId);
        if (taskIds == null || taskIds.size() == 0) {
            return null;
        }
        //设置分页参数
        PageHelper.startPage(page, rows);
        //调用接口查询数据
        List<MaintenaceVo> maintenaceList = taskMapper.queryMaintenaceList(taskIds);
        //封装任务信息
        if (maintenaceList != null) {
            for (MaintenaceVo maintenaceVo : maintenaceList) {
                Integer people = maintenaceVo.getResponsiblePeople();
                User user = userMapper.queryUserById(people);
                if (user != null) {
                    maintenaceVo.setResponsibleName(user.getName());
                }
                Integer type = maintenaceVo.getMaintenanceType();
                if (type == 1) {
                    maintenaceVo.setTypeName("维修");
                } else if (type == 2) {
                    maintenaceVo.setTypeName("清洁");
                } else {
                    maintenaceVo.setTypeName("保养");
                }
                Integer taskInterval = maintenaceVo.getTaskInterval();
                if (taskInterval == 1) {
                    maintenaceVo.setTaskIntervalName("每天");
                } else if (taskInterval == 2) {
                    maintenaceVo.setTaskIntervalName("每周");
                } else if (taskInterval == 3) {
                    maintenaceVo.setTaskIntervalName("每月");
                } else {
                    maintenaceVo.setTaskIntervalName("每年");
                }
            }
        }
        //创建PageInfo对象，获取分页信息
        PageInfo<MaintenaceVo> pageInfo = new PageInfo<MaintenaceVo>(maintenaceList);
        //创建EasyUIJson对象，封装查询结果
        EasyUIJson uiJson = new EasyUIJson();
        //设置查询总记录数
        uiJson.setTotal(pageInfo.getTotal());
        //设置查询记录
        uiJson.setRows(maintenaceList);

        return uiJson;
    }

    /**
     * 新增设备维护记录
     */
    @Override
    public void addMaintenace(MaintenaceVo maintenace, Integer id) {


        Integer taskId = maintenace.getTaskId();
        MaintenaceVo vo = taskMapper.findTaskById(taskId);
        if (vo != null) {
            //修改
            vo.setGrade(maintenace.getGrade());
            vo.setMaintenanceDate(maintenace.getMaintenanceDate());
            vo.setMaintenanceType(maintenace.getMaintenanceType());
            vo.setName(maintenace.getName());
            vo.setResponsiblePeople(maintenace.getResponsiblePeople());
            vo.setResultDesc(maintenace.getResultDesc());
            vo.setTaskInterval(maintenace.getTaskInterval());
            vo.setState(maintenace.getState());
            taskMapper.updateTask(vo);
            //taskMapper.updateMaintenance(vo);
        } else {
            //新增
            maintenace.setState(3);
            taskMapper.addTaskAndDevice(maintenace);
            taskMapper.addTask(maintenace);
            taskMapper.addMaintenace(maintenace);
        }

    }

    /*
     * 编辑页面，查询回显数据的方法
     */
    @Override
    public MaintenaceVo editorMaintenace(MaintenaceVo maintenace) {
        Integer taskId = maintenace.getTaskId();
        MaintenaceVo vo = taskMapper.findTaskById(taskId);
        if (vo != null) {
            Integer responsiblePeople = vo.getResponsiblePeople();
            User user = userMapper.queryUserById(responsiblePeople);
            if (user != null) {
                vo.setResponsibleName(user.getName());
            }
        }
        return vo;
    }

    /*
     * 删除记录
     */
    @Override
    public void deleteMaintenace(int taskId) {
        MaintenaceVo vo = taskMapper.findTaskById(taskId);
        if (vo != null) {
            //修改
            vo.setState(0);
            taskMapper.updateTask(vo);
        }
    }

    @Override
    public EasyUIJson queryTaskListByPage(Task task, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Task> taskList = taskMapper.queryTaskList(task);
        PageInfo<Task> pageInfo = new PageInfo<Task>(taskList);
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal(pageInfo.getTotal());
        uiJson.setRows(taskList);
        return uiJson;
    }

    @Override
    public void addOtherTask(Task task) {
        String now = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
        task.setCreateDate(now);
        task.setState(1);
        taskMapper.addTask(task);
    }

    @Override
    public Task queryTaskById(Integer id) {
        return taskMapper.queryTaskById(id);

    }

    @Override
    public void editTaskById(Task task) {
        Integer id = task.getTaskId();
        Task taskById = taskMapper.queryTaskById(id);
        taskById.setName(task.getName());
        String now = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
        taskById.setCreateDate(now);
        taskById.setFinishTime(task.getFinishTime());
        taskById.setTaskMemo(task.getTaskMemo());
        taskById.setGrade(task.getGrade());
        taskMapper.updateTask(taskById);
    }

    @Override
    public void deleteTask(String ids) {
        // 用，号切成数组
        String[] split = ids.split(",");
        //遍历数组
        for (String string : split) {
            int id = Integer.parseInt(string);
            Task taskById = taskMapper.queryTaskById(id);
            //修改状态

            taskMapper.deleteTaskById(taskById);
        }
    }
}

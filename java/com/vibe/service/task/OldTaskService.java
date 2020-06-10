package com.vibe.service.task;


import com.vibe.pojo.MaintenaceVo;
import com.vibe.pojo.Task;
import com.vibe.utils.EasyUIJson;

public interface OldTaskService {
    /**
     * 查询设备维护记录的信息
     *
     * @param rows
     * @param page
     * @param root
     * @return
     */
    public EasyUIJson queryMaintenaceListByPage(Integer deviceId, Integer page, Integer rows);

    /**
     * 新增设备维护记录
     *
     * @param maintenace
     * @param deviceId
     */
    public void addMaintenace(MaintenaceVo maintenace, Integer deviceId);

    /**
     * 编辑回显数据
     */
    public MaintenaceVo editorMaintenace(MaintenaceVo maintenace);

    /**
     * 根据id删除记录
     */
    public void deleteMaintenace(int parseInt);

    /*任务模块的增删改查*/
    public EasyUIJson queryTaskListByPage(Task task, Integer page, Integer rows);

    public void addOtherTask(Task task);

    public Task queryTaskById(Integer id);

    public void editTaskById(Task task);

    public void deleteTask(String ids);


}

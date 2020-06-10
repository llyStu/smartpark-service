package com.vibe.controller.global;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.vibe.pojo.*;
import com.vibe.util.constant.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.vibe.pojo.user.User;
import com.vibe.service.global.HomeService;
import com.vibe.util.ResponseResult;

@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @RequestMapping("/getHomes")
    public List<HomeBean> getHomes() {
        return homeService.getHomes();
    }

    @RequestMapping("/getUserHomes")
    public List<HomeBean> getUserHomes(HttpServletRequest request) {
        return homeService.getUserHomes(((User) request.getSession().getAttribute("loginUser")).getId());
    }

    @RequestMapping("/updateHomes")
    public Response updateHomes(HttpServletRequest request, @RequestBody List<Integer> userHomes) {
        try {
            homeService.updateHomes(((User) request.getSession().getAttribute("loginUser")).getId(), userHomes);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }

    }

    @RequestMapping("/homeInterfaceMonitor")
    public String homeInterfaceMonitor(@RequestParam("homeId") int homeId) {
        return homeService.homeInterfaceMonitor(homeId);
    }

    @RequestMapping("/homeInterfaceAlarm")
    public String homeInterfaceAlarm(@RequestParam("homeId") int homeId) {
        return homeService.homeInterfaceAlarm(homeId);
    }

    @RequestMapping("/homeInterfaceFault")
    public String homeInterfaceFault(@RequestParam("homeId") int homeId) {
        return homeService.homeInterfaceFault(homeId);
    }

    @RequestMapping("/homeInterfaceEnvironment")
    public String homeInterfaceEnvironment(@RequestParam("homeId") int homeId) {
        return homeService.homeInterfaceEnvironment(homeId);
    }

    @RequestMapping("/homeInterfaceTask")
    public String homeInterfaceTask(@RequestParam("homeId") int homeId) {
        return homeService.homeInterfaceTask(homeId);
    }

    @RequestMapping("/homeInterfaceAsset")
    public Map<Object, Object> homeInterfaceAsset(@RequestParam("codeName") String codeName, @RequestParam("type") int type) {
        return homeService.homeInterfaceAsset(codeName, type);
    }

    @RequestMapping("/homeInterfaceAssetDevice")
    public Map<Object, Object> homeInterfaceAssetDevice(@RequestParam("codeName") String codeName) {
        return homeService.homeInterfaceAsset(codeName, 2002);
    }

    @RequestMapping("/homeInterfaceAssetMonitor")
    public Map<Object, Object> homeInterfaceAssetMonitor(@RequestParam("codeName") String codeName) {
        return homeService.homeInterfaceAsset(codeName, 2001);
    }

    @RequestMapping("/alarmCount")
    public int alarmCount() {
        try {
            return homeService.alarmCount();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return 0;
    }

    @RequestMapping("/getEnergyModule")
    public Map getEnergyModule(EnergyModule energyModule) {
        return homeService.getEnergyModule(energyModule);
    }

    /**
     * 查询报警，通过模块
     * 通过模块查询报警信息:t_code 里 parent
     *
     * @param modules 模块id 多个用,分隔
     * @return json
     * @Author liujingeng
     * @Date 2020/2/13
     */
    @RequestMapping("/homeInterfaceAlarmByModule")
    public AlarmModuleAll homeInterfaceAlarmByModule(String modules) {
        return homeService.homeInterfaceAlarmByModule(modules);
    }

    /**
     * 查询报警，通过id
     * 通过id查询报警信息:t_code 里 id
     *
     * @param ids id 多个用,分隔
     * @return
     * @Author liujingeng
     * @Date 2020/2/13
     */
    @RequestMapping("/homeInterfaceAlarmById")
    public List<AlarmModule> homeInterfaceAlarmById(String ids) {
        return homeService.homeInterfaceAlarmById(ids);
    }

    @RequestMapping("/countAlarm")
    public int countAlarm() {
        return homeService.countAlarm();
    }

    @RequestMapping("/homeAssetHealth")
    public Map<String, Integer> homeAssetHealth(Integer spaceId, Integer kind) {
        return homeService.homeAssetHealth(spaceId, kind);
    }


    @RequestMapping("/deviceTypeAlarmProportion")
    public Map<String, Object> deviceTypeAlarmProportion() {
        return homeService.deviceTypeAlarmProportion();
    }

    @RequestMapping("/countMonitorStatus")
    public Map<String, List<Object>> countMonitorStatus(String ids) {
        return homeService.countMonitorStatus(ids);
    }

    @RequestMapping("/allInterface")
    public Map<Object, Object> allInterface(@RequestParam("url") String url) {
        try {
            url = url.replace("!", "&");
            return homeService.allInterface(url);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/homeCamera")
    public List<HomeCamera> homeCamera(@RequestParam("codeName") String codeName) {
        try {
            return homeService.homeCamera(codeName);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/allDeviceStateData")
    public Map<Object, Object> allDeviceStateData() {
        try {
            return homeService.allDeviceStateData();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/xiaofangCountData")
    public Map<Short, XiaofangCount> xiaofangCountData() {
        try {
            return homeService.xiaofangCountData();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

}

package com.vibe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.pojo.ApLocation;
import com.vibe.pojo.user.UserLocationData;
import com.vibe.service.localize.PositioningService;

@Controller
public class PositioningController {

    @Autowired
    private PositioningService positioningService;

    /**
     * 获取热点对应的所有数据
     */
    @RequestMapping("/location/queryApLocation")
    @ResponseBody
    public List<ApLocation> queryApLocation() {
        return positioningService.queryApLocation();
    }

    /**
     * 上传用户位置信息
     */
    @RequestMapping("/location/uploadUserLocation")
    @ResponseBody
    public void uploadUserLocation(@RequestParam("username") String username, @RequestParam("mac") String mac, @RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude, @RequestParam("time") String time) {
        if ("".equals(mac)) {
            mac = null;
        }
        if ("".equals(longitude)) {
            longitude = null;
        }
        if ("".equals(latitude)) {
            latitude = null;
        }
        positioningService.uploadUserLocation(username, mac, longitude, latitude, time);
    }

    /**
     * 获取所有的用户当前位置
     */
    @RequestMapping("/location/getUsersCurrentLocation")
    @ResponseBody
    public List<UserLocationData> getUsersCurrentLocation() {
        return positioningService.getUsersCurrentLocation();
    }

    /**
     * 获取某个特定用户某段时间的轨迹
     */
    @RequestMapping("/location/getUserLocation")
    @ResponseBody
    public List<UserLocationData> getUserLocation(@RequestParam("username") String username, @RequestParam("start") String start, @RequestParam("end") String end) {
        return positioningService.getUserLocation(username, start, end);
    }
}

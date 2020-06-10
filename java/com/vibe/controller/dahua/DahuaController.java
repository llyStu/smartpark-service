package com.vibe.controller.dahua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.dahua.ServerInfo;
import com.vibe.service.dahua.DahuaService;

/**
 * 大华停车场http第三方接口
 *
 * @Description 停车场
 * @Author lxx-nice@163.com
 * @Create 2019/12/25
 * @Module 智慧园区
 */
@RestController
@RequestMapping("/quary")
public class DahuaController {

    @Autowired
    private DahuaService dahuaService;

    /**
     * 收费接口
     * API描述：无
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param carNum     车牌号
     * @param ownerType
     * @param carType
     * @param ownerName
     * @param feeType    收费类型
     * @param operatorId
     * @return json
     * @Author: lxx-nice@163.com
     * @Description:
     * @Create: 13:55 2019/12/25
     **/
    @RequestMapping(value = "/getChargeInfo", produces = {"application/json;charset=UTF-8"})//获取停车场收费接口
    @ResponseBody
    public String getChargeInfo(@RequestParam(required = true) int pageNum,
                                @RequestParam(required = true) int pageSize,
                                @RequestParam(required = false) String startTime,//开始时间
                                @RequestParam(required = false) String endTime,//结束时间
                                @RequestParam(required = false, defaultValue = "") String carNum,//支持模糊查询
                                @RequestParam(required = false, defaultValue = "") String ownerType,//
                                @RequestParam(required = false, defaultValue = "") String carType,//
                                @RequestParam(required = false, defaultValue = "") String ownerName,//
                                @RequestParam(required = false) Integer feeType,//
                                @RequestParam(required = false) Integer operatorId) {//
        // 获取ip port
        ServerInfo info = dahuaService.getServer();
        return dahuaService.getChargeInfo(pageNum, pageSize, startTime, endTime, carNum, ownerType, carType, ownerName, feeType, operatorId, info);
    }


    @RequestMapping(value = "/getVisitor", produces = {"application/json;charset=UTF-8"})//获取访客管理访客接口
    @ResponseBody
    public String getVisitor(@RequestParam(required = true) int pageNum,
                             @RequestParam(required = true) int pageSize,
                             @RequestParam(required = false, defaultValue = "") String singleCondition,//访客姓名/访客单位/被访者姓名/被访者部门
                             //0:预约审批中;1:预约;2:在访;3:离访;4:预约取消
                             @RequestParam(required = false, defaultValue = "") String status) throws Exception {
        ServerInfo info = dahuaService.getServer();
        return dahuaService.getVisitor(pageNum, pageSize, singleCondition, status, info);
    }


    //停车场信息统计(按车场分组统计)
    @RequestMapping(value = "/getParkinglotInfo", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getParkinglotInfo(@RequestParam(required = false, defaultValue = "") String parkingLot,
                                    @RequestParam(required = false, defaultValue = "") String parkingLotCode) //支持模糊查询
            throws Exception {
        ServerInfo info = dahuaService.getServer();
        return dahuaService.getParkinglotInfo(parkingLotCode, parkingLot, info);
    }

    //过车记录

}

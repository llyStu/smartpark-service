/*
package com.vibe.controller.test;

import com.cizing.system.common.utils.ResponseModel;
import com.cizing.system.common.utils.ResultCode;
import com.vibe.pojo.TreeAlarmData;
import com.vibe.pojo.energy.Energy;
import com.vibe.service.alarm.AlarmService;
import com.vibe.service.energy.EnergyCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

*/
/**
 * @ClassName TestController
 * @Description
 * @
 * @Date 2019/9/12 14:18
 * @Version 1.0
 * @Author zhsili81@gmail.com
 *//*

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private EnergyCountService energyCountService;

    @Autowired
    private AlarmService alarmService;
    
    */
/**
     * @ClassName TestController
     * @Description 
     * @param  
     * @return com.vibe.util.constant.ResponseModel<java.lang.String>
     *
     * @Version 1.0
     * @Date 2019/9/12 16:13
     * @Author zhsili81@gmail.com
     *//*

    @GetMapping("/error")
    @ResponseBody
    public ResponseModel<String> testError() {
        //test
        return ResponseModel.failure("错误").code(ResultCode.ERROR);
    }
    */
/**
     * @ClassName TestController
     * @Description 
     * @param  
     * @return com.vibe.util.constant.ResponseModel<java.util.List<com.vibe.pojo.energy.Energy>>
     *
     * @Version 1.0
     * @Date 2019/9/12 16:13
     * @Author zhsili81@gmail.com
     *//*

    @GetMapping("/obj")
    @ResponseBody
    public ResponseModel<List<Energy>> testObject() {
        //test
        try {
            return ResponseModel.success(energyCountService.getEnergyA3Floorid()).code(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.failure("错误" + e.getMessage()).code(ResultCode.ERROR);
        }
    }
    */
/**
     * @ClassName TestController
     * @Description 
     * @param  
     * @return com.vibe.util.constant.ResponseModel<java.util.List<com.vibe.pojo.TreeAlarmData>>
     *
     * @Version 1.0
     * @Date 2019/9/12 15:45
     * @Author zhsili81@gmail.com
     *//*

    @GetMapping("/objList")
    @ResponseBody
    public ResponseModel<List<TreeAlarmData>> testList() {
        //test
        try {
            return ResponseModel.success(alarmService.getTreeAlarmRule()).code(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.failure("错误" + e.getMessage()).code(ResultCode.ERROR);
        }
    }
}
*/

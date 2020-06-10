package com.vibe.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.common.JSONUtil;
import com.vibe.pojo.BlueCardParkBean;
import com.vibe.service.bluecardpark.BlueCardParkService;

@Controller
public class BlueCardParkController {

    class TimeStamp {
        private String timeStamp;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }


    }

    class SuccessResult {
        private String status;
        private TimeStamp datas;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public TimeStamp getDatas() {
            return datas;
        }

        public void setDatas(TimeStamp datas) {
            this.datas = datas;
        }

    }

    class FailResult {
        private String status;
        private String errorCode;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

    }

    @Autowired
    private BlueCardParkService blueCardParkService;

    @RequestMapping("/blueCardPark_heartbeatInterface")
    @ResponseBody
    public String heartbeatInterface(@RequestBody BlueCardParkBean blueCardParkBean) {
        // TODO Auto-generated method stub
        System.out.println(JSONUtil.toJson(blueCardParkBean));
        try {
            blueCardParkService.heartbeatInterface(blueCardParkBean);
            SuccessResult successResult = new SuccessResult();
            successResult.setStatus("success");
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setTimeStamp(new Date().getTime() + "");
            successResult.setDatas(timeStamp);
            return JSONUtil.toJson(successResult);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            FailResult failResult = new FailResult();
            failResult.setStatus("fail");
            failResult.setErrorCode(e.getMessage());
            return JSONUtil.toJson(failResult);
        }
    }
}

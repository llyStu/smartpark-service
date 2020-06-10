package com.vibe.controller.global;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.common.id.IdGenerator;
import com.vibe.pojo.RecordLog;
import com.vibe.pojo.user.Department;
import com.vibe.service.global.RecordLogService;
import com.vibe.service.logAop.LogService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.service.logAop.Syslog;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.FormJson;

@Controller
public class OptLogController {
    @Autowired
    private LogService logService;
    @Autowired
    private RecordLogService recordLogService;

    @RequestMapping("/logList")
    public @ResponseBody
    EasyUIJson queryLogList(Syslog log, @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "20") Integer rows) {
        // 远程调用Service服务对象，获取员工列表数据

        EasyUIJson easyUIJson = logService.queryLogListByPage(log, page, rows);
        return easyUIJson;
    }

    @RequestMapping("/addLog")
    public @ResponseBody
    String aadLog(Syslog syslog) {
        // 远程调用Service服务对象，获取员工列表数据
        try {
            logService.insertLog(syslog);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "200";
        }

    }

    @RequestMapping("/log/recordLogPage")
    public EasyUIJson queryRecordLogListByPage(RecordLog log, Integer page, Integer rows) {
        return recordLogService.queryRecordLogListByPage(log, page, rows);
    }

    @RequestMapping("/log/addRecordLog")
    public @ResponseBody
    String queryRecordLogListByPage(RecordLog log) {
        try {
            recordLogService.addRecordLog(log);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

    @RequestMapping("/log/queryRecord")
    public @ResponseBody
    RecordLog queryRecord(Integer id) {
        RecordLog log = recordLogService.queryRecordLogById(id);
        return log;
    }

    @RequestMapping("/log/editRecord")
    public @ResponseBody
    String editRecord(RecordLog recordLog) {
        try {
            recordLogService.editRecordLogById(recordLog);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }

    }

    @RequestMapping("/log/deleteRecord")
    public @ResponseBody
    String deleteRecord(String ids) {
        try {
            recordLogService.deleteRecordLog(ids);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }
}

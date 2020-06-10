package com.vibe.service.global;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.global.LogMapper;
import com.vibe.pojo.RecordLog;
import com.vibe.utils.EasyUIJson;

@Service
public class RecordLogServiceImpl implements RecordLogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public EasyUIJson queryRecordLogListByPage(RecordLog log, Integer page, Integer rows) {

        PageHelper.startPage(page, rows);
        List<RecordLog> logList = logMapper.queryRecordLogList(log);
        PageInfo<RecordLog> pageInfo = new PageInfo<RecordLog>(logList);
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal(pageInfo.getTotal());
        uiJson.setRows(logList);
        return uiJson;
    }

    @Override
    public RecordLog queryRecordLogById(Integer id) {
        return logMapper.queryRecordLogById(id);
    }

    @Override
    public void addRecordLog(RecordLog RecordLog) {
        logMapper.addRecordLog(RecordLog);
    }

    @Override
    public void editRecordLogById(RecordLog RecordLog) {
        Integer id = RecordLog.getId();
        RecordLog log = logMapper.queryRecordLogById(id);
        log.setDetails(RecordLog.getDetails());
        log.setResult(RecordLog.getResult());
        log.setType(RecordLog.getType());
        log.setUid(RecordLog.getUid());

        logMapper.editRecordLogById(log);
    }

    @Override
    public void deleteRecordLog(String ids) {
        // 用，号切成数组
        String[] split = ids.split(",");
        //遍历数组
        for (String string : split) {
            int id = Integer.parseInt(string);
            RecordLog log = logMapper.queryRecordLogById(id);
            //修改状态
            log.setValid(0);
            logMapper.editRecordLogById(log);
        }
    }
}

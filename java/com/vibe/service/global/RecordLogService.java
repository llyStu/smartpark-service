package com.vibe.service.global;


import com.vibe.pojo.RecordLog;
import com.vibe.utils.EasyUIJson;

/**
 * 日志服务类
 * @author BL
 *
 */
public interface RecordLogService {

	EasyUIJson queryRecordLogListByPage(RecordLog log, Integer page, Integer rows);

	RecordLog queryRecordLogById(Integer id);

	void addRecordLog(RecordLog RecordLog);

	void editRecordLogById(RecordLog RecordLog);

	void deleteRecordLog(String ids);
	
	
	
	
}

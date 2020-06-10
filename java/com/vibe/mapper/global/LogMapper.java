package com.vibe.mapper.global;

import com.vibe.pojo.RecordLog;
import com.vibe.service.logAop.Syslog;

import java.util.List;

public interface LogMapper {
	
	public void insertLog(Syslog sysLog);
	 
	public List<Syslog> queryLogList(Syslog sysLog);
	 
	public List<RecordLog> queryRecordLogList(RecordLog log);

	public RecordLog queryRecordLogById(Integer id);

	public void addRecordLog(RecordLog recordLog);

	public void editRecordLogById(RecordLog log);
}

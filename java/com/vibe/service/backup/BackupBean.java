package com.vibe.service.backup;

import java.io.File;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BackupBean {
	
	private File file;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date time;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date backup_min;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date backup_max;
	private String fileName;
	private String desc;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}

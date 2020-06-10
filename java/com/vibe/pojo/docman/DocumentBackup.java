package com.vibe.pojo.docman;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class DocumentBackup extends DocumentDir {
	private Integer bid, buid;
	private String bdesc, buname, attach;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date backup;
	public Integer getBid() {
		return bid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public Date getBackup() {
		return backup;
	}
	public void setBackup(Date backup) {
		this.backup = backup;
	}
	public Integer getBuid() {
		return buid;
	}
	public void setBuid(Integer buid) {
		this.buid = buid;
	}
	public String getBdesc() {
		return bdesc;
	}
	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
	}
	public String getBuname() {
		return buname;
	}
	public void setBuname(String buname) {
		this.buname = buname;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
}

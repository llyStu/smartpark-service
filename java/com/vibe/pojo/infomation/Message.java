package com.vibe.pojo.infomation;

import java.util.Date;

/*
create table qs_test.t_message (
mid int(16) primary key auto_increment,
sender int(16),
receiver int(16),
sendtime date,
state int(8),
content varchar(1024)
)
 */
public class Message {
	private Integer mid;
	private Integer sender;
	private Integer receiver;
	private Date sendtime;
	private Integer state;
	private String content;
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public Integer getSender() {
		return sender;
	}
	public void setSender(Integer sender) {
		this.sender = sender;
	}
	public Integer getReceiver() {
		return receiver;
	}
	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Message [mid=" + mid + ", sender=" + sender + ", receiver=" + receiver + ", sendtime=" + sendtime
				+ ", state=" + state + ", content=" + content + "]";
	}
	
	
}

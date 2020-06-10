package com.vibe.pojo.user;

import java.io.Serializable;

/**
 * 用户权限的管理表
 * @author FLex3
 *
 */
public class UserPermission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6236379339750452512L;
	private Integer uid;//用户id
	private Integer pid;//权限id

	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
}

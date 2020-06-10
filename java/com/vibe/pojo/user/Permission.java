package com.vibe.pojo.user;

import java.io.Serializable;

/**
 * 权限表
 * @author FLex3
 *

idint(11) NOT NULL
namevarchar(63) NOT NULL
module_idint(11) NULL
p_descvarchar(63) NULL
 */
public class Permission implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5012433179911617601L;
	private Integer id;         //权限id
	private String name;        //权限名称
	private Integer menuId;  //模块id
	private Integer pType;  //权限类型  0 只读  1读写
	private String pDesc;      //权限说明
	private int src;
	public Permission() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getpDesc() {
		return pDesc;
	}
	public void setpDesc(String pDesc) {
		this.pDesc = pDesc;
	}
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
	public Integer getpType() {
		return pType;
	}
	public void setpType(Integer pType) {
		this.pType = pType;
	}
	
	
	
	
}

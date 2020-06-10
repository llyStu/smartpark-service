package com.vibe.pojo.user;

import java.io.Serializable;
import java.util.List;

/*
 * 用户的pojo类
 */
public class User implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 2759495086444618962L;


	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String login_id, String password) {
		super();
		this.login_id = login_id;
		this.password = password;
	}
	public User(Integer id, String login_id, String name, String password, String mail) {
		super();
		this.id = id;
		this.login_id = login_id;
		this.name = name;
		this.password = password;
		this.mail = mail;
	}
	private Integer id;          //标识, 高16位(bits)表示站点id
	private String login_id;     //登录标识
	private String loginId;
	private String name;         //姓名
	private String password;     //密码 (MD5加密)
	private String mail;         // 电子邮件
	private Integer valid;       //是否有效  默认有效1
	private Integer sequence;    //显示顺序  默认是1
	private Integer department;	//部门表id,所在部门
	private List<Permission> plist; //权限名称集合
	private List<Role> rlist; //角色名称集合
	private String deptName;
	private Integer rid; //一个用户只可以分配一个角色  先这样实现
	private String phone;
	private String flag = "out";  //in 内部登录不记录日志

	private String headUrl;

	private Integer leaderId;

	private String position;


	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}
	//数字园区用户外键
//	private String officeId;

//	public String getOfficeId() {
//		return officeId;
//	}

//	public void setOfficeId(String officeId) {
//		this.officeId = officeId;
//	}


	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	private int loginSrc = 1;  //登录来源  1 网页端，2三维客户端，3App端


	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public List<Permission> getPlist() {
		return plist;
	}
	public void setPlist(List<Permission> plist) {
		this.plist = plist;
	}
	public List<Role> getRlist() {
		return rlist;
	}
	public void setRlist(List<Role> rlist) {
		this.rlist = rlist;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getLoginSrc() {
		return loginSrc;
	}
	public void setLoginSrc(int loginSrc) {
		this.loginSrc = loginSrc;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}

}

package com.vibe.pojo.docman;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vibe.utils.DateYMDSerializer;

public class DocumentDir {
	@Override
	public String toString() {
		return "DocumentDir [id=" + id + ", name=" + name + ", number=" + number + ", filenames=" + filenames
				+ ", uname=" + uname + ", uid=" + uid + ", modified=" + modified + ", desc=" + desc + ", pid=" + pid
				+ ", visible=" + visible + ", childrenDir=" + childrenDir + "]";
	}
	private Integer id;
	private String name, number, filenames, uname;
	private Integer uid;

	@JsonSerialize(using = DateYMDSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date modified;

//	private String modifiedString;
	private String desc;
	private Integer pid;
	private Boolean visible;
	private String attach;
	public DocumentDir(Integer id, List<DocumentDir> childrenDir) {
		super();
		this.id = id;
		this.childrenDir = childrenDir;
	}
	public DocumentDir() {
		super();
	}
	private List<DocumentDir> childrenDir;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public List<DocumentDir> getNodes() {
		return childrenDir;
	}
	public void setNodes(List<DocumentDir> childrenDir) {
		this.childrenDir = childrenDir;
	}
	public DocumentDir withChildrenDir(List<DocumentDir> childrenDir) {
		this.childrenDir = childrenDir;
		return this;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilenames() {
		return filenames;
	}
	public void setFilenames(String filenames) {
		this.filenames = filenames;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}

	/*public String getModifiedString() {
		return modifiedString;
	}

	public void setModifiedString(String modifiedString) {
		this.modifiedString = modifiedString;
	}*/
}

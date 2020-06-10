package com.vibe.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	// 空间管理.模型 相关字段
	private String modelName, modelCoordinate, modelAngle, modelSize;

	// id,该节点的id
	private int id;
	// 树形菜单节点的名称
	private String text;
	// 分类
	private Integer kind;
	// 节点的状态
	private Integer status;

	private Integer grade;
	
	private String time;

	private Integer catalog;
	
	private String catalogName;

	private Object value;

	private String unit;

	private String valueStr;
	
	private String errorMsg;

	private String redio;

	private String kindStr;

	private String statusStr;

	private String monitorType;

	private String name;
	
	private String fullName;
	
	private Float minValue;
	
	private Float maxValue;
	// 摄像头的属性。
	private String username;

	private String password;

	private String host;

	private int port;

	private String rtspUrlPattern;
	
	//虚表树的属性parent itemizeType
	private Integer parent;
	
	private Integer itemizeType;
	
	private boolean bind;

	private List<TreeNode> nodes;
	
	private String healthIndex;
	
	private String dataType;
	
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Float getMinValue() {
		return minValue;
	}
	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}
	
	public Float getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}
	public boolean isBind() {
		return bind;
	}
	public void setBind(boolean bind) {
		this.bind = bind;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Integer getItemizeType() {
		return itemizeType;
	}
	public void setItemizeType(Integer itemizeType) {
		this.itemizeType = itemizeType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRtspUrlPattern() {
		return rtspUrlPattern;
	}

	public void setRtspUrlPattern(String rtspUrlPattern) {
		this.rtspUrlPattern = rtspUrlPattern;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

	public String getKindStr() {
		return kindStr;
	}

	public void setKindStr(String kindStr) {
		this.kindStr = kindStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getRedio() {
		return redio;
	}

	public void setRedio(String redio) {
		this.redio = redio;
	}

	public TreeNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCatalog() {
		return catalog;
	}

	public void setCatalog(Integer catalog) {
		this.catalog = catalog;
	}

	public TreeNode(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeNode> nodes) {
		this.nodes = nodes;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	// 添加子节点的方法
	public void addChild(TreeNode node) {
		if (this.nodes == null) {
			nodes = new ArrayList<TreeNode>();
			nodes.add(node);
		} else {
			nodes.add(node);
		}

	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelCoordinate() {
		return modelCoordinate;
	}

	public void setModelCoordinate(String modelCoordinate) {
		this.modelCoordinate = modelCoordinate;
	}

	public String getModelAngle() {
		return modelAngle;
	}

	public void setModelAngle(String modelAngle) {
		this.modelAngle = modelAngle;
	}

	public String getModelSize() {
		return modelSize;
	}

	public void setModelSize(String modelSize) {
		this.modelSize = modelSize;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getHealthIndex() {
		return healthIndex;
	}
	
	public void setHealthIndex(String healthIndex) {
		this.healthIndex = healthIndex;
	}
	

}

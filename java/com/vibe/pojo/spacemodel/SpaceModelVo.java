package com.vibe.pojo.spacemodel;

import com.vibe.monitor.asset.AssetKind;

public class SpaceModelVo {
	private Integer id;
	private String modelName, modelCoordinate, modelAngle, modelSize;
	private AssetKind kind;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public AssetKind getKind() {
		return kind;
	}
	public void setKind(AssetKind kind) {
		this.kind = kind;
	}

	public String getSrcTable() {
		switch (this.kind) {
		case DEVICE: return "t_device";
		case SPACE: return "t_space";
		case PROBE: return "t_probe";
		case CONTROL: return "t_control";
		default: break;
		}
		throw new IllegalArgumentException("不支持其他类型设置空间模型信息[" + this.id +":"+ this.kind +"]");
	}

}

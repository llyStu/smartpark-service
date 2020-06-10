package com.vibe.pojo.spacemodel;

public class SceneAsset {
	
	private Integer said,  assetId , status;
	
	private String path, elementName, position, rotation, scale, viewpoint, principleModelName, boardElementName, boardPosition,sceneId,caption;
	
	private Short catalog;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		if(null == sceneId){
			this.sceneId = null;
		}else{
			this.sceneId = sceneId.replaceAll("&gt;",">");
		}
	}
	public Integer getAssetId() {
		return assetId;
	}
	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRotation() {
		return rotation;
	}
	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getViewpoint() {
		return viewpoint;
	}
	public void setViewpoint(String viewpoint) {
		this.viewpoint = viewpoint;
	}
	public String getPrincipleModelName() {
		return principleModelName;
	}
	public void setPrincipleModelName(String principleModelName) {
		this.principleModelName = principleModelName;
	}
	public Integer getSaid() {
		return said;
	}
	public void setSaid(Integer said) {
		this.said = said;
	}
	public String getBoardElementName() {
		return boardElementName;
	}
	public void setBoardElementName(String boardElementName) {
		this.boardElementName = boardElementName;
	}
	public String getBoardPosition() {
		return boardPosition;
	}
	public void setBoardPosition(String boardPosition) {
		this.boardPosition = boardPosition;
	}
	public String getPath() {
		return path;
	}

	public Short getCatalog() {
		return catalog;
	}

	public void setCatalog(Short catalog) {
		this.catalog = catalog;
	}

	public void setPath(String path) {
		this.path = path.replaceAll("&gt;",">");
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}

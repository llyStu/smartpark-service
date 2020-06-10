package com.vibe.parse;

public class BacnetMonitor extends BaseMonitorBean {

	private String objectType;
	private String instanceNumber;

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(String instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(App.TEST){
			return super.toString();
		}else {
			return super.toString() + "insert into t_asset_prop(asset, name, value) values (" + getId()
				+ ", \"ObjectType\", \"" + getObjectType()
				+ "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"InstanceNumber\", \""
				+ getInstanceNumber() + "\");";
		}
		
	}

	@Override
	public String getAiType() {
		// TODO Auto-generated method stub
		return App.BACNET_AI_TYPE;
	}

	@Override
	public String getAoType() {
		// TODO Auto-generated method stub
		return App.BACNET_AO_TYPE;
	}

	@Override
	public String getDiType() {
		// TODO Auto-generated method stub
		return App.BACNET_DI_TYPE;
	}

	@Override
	public String getDoType() {
		// TODO Auto-generated method stub
		return App.BACNET_DO_TYPE;
	}

}

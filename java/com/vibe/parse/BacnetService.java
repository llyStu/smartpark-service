package com.vibe.parse;

public class BacnetService extends BaseService {
	
	private String ip;
	private String port;
	private String deviceId;

	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "insert into t_service(id, parent, type, name, caption)values (" + getId() + "," + getParent()
				+ ", \"BACnetServer\", \"" + getName() + "\" , \"" + getCaption()
				+ "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"Remotehost\", \""
				+ getIp() + "\");\ninsert into t_asset_prop(asset, name, value) values (" 
				+ getId() + ", \"DeviceId\", \"" + getDeviceId() + "\");\ninsert into t_asset_prop(asset, name, value) values (" 
				+ getId() + ", \"Remoteport\", \"" + getPort() + "\");";
	}
}

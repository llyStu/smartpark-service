package com.vibe.parse;

public class CameraDevice extends Device{
	
	private String username;
	private String password;
	private String host;
	private String port;
	

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


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return App.CAMERA_DEVICE_TYPE;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+ "insert into t_asset_prop(asset, name, value) values (" + getId()
		+ ", \"Host\", \"" + getHost()
		+ "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"UserName\", \""
		+ getUsername() + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"Password\", \""
		+ getPassword() + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"Port\", \""
		+ getPort() + "\");";
	}
}

package com.vibe.parse;

public class ModbusService extends BaseService {
	private String ip;
	private String port;
	private String unitId;
	private String useRtu;
	
	public String getUseRtu() {
		return useRtu;
	}
	public void setUseRtu(String useRtu) {
		this.useRtu = useRtu;
	}
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
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result =  "insert into t_service(id, parent, type, name, caption)values (" + getId() + "," + getParent()
		+ ", \"ModbusTcpMaster\", \"" + getName() + "\" , \"" + getCaption()
		+ "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"Host\", \""
		+ getIp() + "\");\ninsert into t_asset_prop(asset, name, value) values (" 
		+ getId() + ", \"Port\", \"" + getPort() + "\");\ninsert into t_asset_prop(asset, name, value) values (" 
		+ getId() + ", \"UnitId\", \"" + getUnitId() + "\");";
		if(getUseRtu()!=null && "true".equals(getUseRtu())){
			result = result+"\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"UseRtu\", \"" + getUseRtu() + "\");";
		}
		return result;
	}
	
}

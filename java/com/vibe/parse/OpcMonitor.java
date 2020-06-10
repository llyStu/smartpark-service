package com.vibe.parse;

public class OpcMonitor extends BaseMonitorBean {

	private String itemTag;


	public String getItemTag() {
		return itemTag;
	}

	public void setItemTag(String itemTag) {
		this.itemTag = itemTag;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(App.TEST){
			return super.toString();
		}else {
			return super.toString() + "insert into t_asset_prop(asset, name, value) values (" + getId()
				+ ", \"ItemTag\", \"" + getItemTag() + "\");";
		}
		
	}

	@Override
	public String getAiType() {
		// TODO Auto-generated method stub
		return App.OPC_AI_TYPE;
	}

	@Override
	public String getAoType() {
		// TODO Auto-generated method stub
		return App.OPC_AO_TYPE;
	}

	@Override
	public String getDiType() {
		// TODO Auto-generated method stub
		return App.OPC_DI_TYPE;
	}

	@Override
	public String getDoType() {
		// TODO Auto-generated method stub
		return App.OPC_DO_TYPE;
	}

}

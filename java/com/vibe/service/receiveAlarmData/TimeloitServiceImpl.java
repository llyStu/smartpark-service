package com.vibe.service.receiveAlarmData;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.AssetTypeManager;
import com.vibe.monitor.asset.type.ProbeType;
import com.vibe.monitor.drivers.timloit.TimloitAlarmBoolProbe;
import com.vibe.monitor.drivers.timloit.TimloitAlarmEnumsProbe;
import com.vibe.monitor.drivers.timloit.TimloitAlarmProbe;
import com.vibe.monitor.drivers.timloit.TimloitAlarmService;
import com.vibe.monitor.drivers.timloit.TimloitProbe;
import com.vibe.monitor.result.MonitorResult;
import com.vibe.monitor.server.MonitorServer;


@Service
public class TimeloitServiceImpl implements TimeloitService {

	@Autowired
	private AssetStore assetStore;

	@Autowired
	private MonitorServer monitorServer;


	@Override
	public void receiveData(TimeloitAlarmData data) {
		System.out.println("凌宇推送的消息："+data.toString());
		String address = data.getAddress();
		String alarmInfo = data.getAlarmInfo();
		String deviceCode = data.getDeviceCode();
		String gatewayCode = data.getGatewayCode();
		int type = data.getType();
		String val = data.getVal();

		Collection<Asset<ProbeType>> EnumsProbeType = getProbeType("TimloitAlarmEnumsProbe");
		if(EnumsProbeType != null){
			for (Asset<ProbeType> asset : EnumsProbeType) {
				setEnumsProbe(asset, address, alarmInfo, deviceCode, gatewayCode,val);
			}
		}

		Collection<Asset<ProbeType>> boolProbeType = getProbeType("TimloitAlarmBoolProbe");
		if(boolProbeType != null){
			for (Asset<ProbeType> asset : boolProbeType) {
				setBoolProbe(asset, address, alarmInfo,deviceCode, gatewayCode, val);
			}
		}

		Collection<Asset<ProbeType>> pgProbeType = getProbeType("TimloitAlarmPgProbe");
		if(pgProbeType != null){
			for (Asset<ProbeType> asset : pgProbeType) {
				setPGBoolProbe(asset, address, alarmInfo,deviceCode, type);
			}
		}

	}
	private Collection<Asset<ProbeType>> getProbeType(String typeName){
		AssetTypeManager<ProbeType> probeTypes = assetStore.getProbeTypes();
		if(probeTypes.size()>0){

			for (ProbeType probeType : probeTypes) {
				if(probeType.getName().equals(typeName)){
					return  assetStore.getAssets(probeType);

				}
			}
		}
		return null;
	}

	private void setEnumsProbe(Asset<?> asset, String address, String alarmInfo,
							   String deviceCode, String gatewayCode, String val) {
		TimloitAlarmEnumsProbe probe = (TimloitAlarmEnumsProbe)asset;
		if(gatewayCode == null )
			gatewayCode="";
		if(address == null){
			address ="";
		}
		String deviceKey = gatewayCode+deviceCode+address;
		if(val != null){
			String[] split = val.split(",");
			if(split.length>0){
				for (int i = 0; i < split.length; i++) {
					String key =deviceKey+i;
					if(probe.makeRegisterKey().equals(key)){
						if(alarmInfo != null){
							probe.setDesc(alarmInfo);
						}
						Integer value = Integer.valueOf(split[i], 16);
						monitorServer.getResultDispatcher().dispatch(new MonitorResult(probe,value));
					}
				}
			}
		}
	}

	private void setBoolProbe(Asset<?> asset, String address, String alarmInfo, String deviceCode,String gatewayCode, String val) {
		TimloitAlarmBoolProbe probe = (TimloitAlarmBoolProbe)asset;
		String key = probe.makeRegisterKey();
		if(gatewayCode == null )
			gatewayCode="";
		if(address == null){
			address ="";
		}
		String tsKey =  gatewayCode+deviceCode+address;
		if(key.equals(tsKey)){
			if(alarmInfo != null){
				probe.setDesc(alarmInfo);
			}
			monitorServer.getResultDispatcher().dispatch(new MonitorResult(probe,Integer.parseInt(val) == 1?true:false));
		}
	}
	private void setPGBoolProbe(Asset<?> asset, String address, String alarmInfo, String deviceCode, int type) {
		TimloitAlarmProbe probe = (TimloitAlarmProbe)asset;
		boolean isProbe = probe.getDeviceCode().equals(deviceCode);

		if(address !=null && !"".equals(address)){
			isProbe = isProbe && probe.getAddress().equals(address);
		}
		if(isProbe){
			probe.setDesc(alarmInfo);
			monitorServer.getResultDispatcher().dispatch(new MonitorResult(probe,type == 1?true:false));
		}
	}
}

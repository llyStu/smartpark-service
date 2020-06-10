package com.vibe.service.smartwall;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.drivers.genericpassive.probe.GenericPassiveProbe;
import com.vibe.monitor.drivers.genericpassive.service.GenericPassiveService;
import com.vibe.monitor.result.MonitorResult;
import com.vibe.monitor.server.MonitorServer;
import com.vibe.pojo.AlarmData;
import com.vibe.pojo.SmartWallData;

@Service
public class SmartWallServiceImpl implements SmartWallService {

	@Autowired
	private MonitorServer monitorServer;

	@Autowired
	private AssetStore assetStore;

	@Override
	public void smartWall(SmartWallData smartWallData) {
		// TODO Auto-generated method stub
		if ("ALARM".equals(smartWallData.getMethod())) {
			if (smartWallData.getObj() != null) {
				@SuppressWarnings("unchecked")
				List<AlarmData> alarmDatas = (List<AlarmData>) smartWallData.getObj();
				Collection<Asset<?>> genericPassiveServices = assetStore
						.getAssets(GenericPassiveService.class);
				GenericPassiveService smartWallService = null;
				for (Asset<?> asset : genericPassiveServices) {
					GenericPassiveService service = (GenericPassiveService) asset;
					if(service.getName().equals("smartWallService")){
						smartWallService = service;
						break;
					}
				}
				for (AlarmData alarmData : alarmDatas) {
					if (alarmData.getdSid() != null) {
						String alarmTypeKey = alarmData.getdSid() + "alarmType";
						String alarmStateKey = alarmData.getdSid() + "alarmState";
						GenericPassiveProbe alarmTypeMonitor = (GenericPassiveProbe) smartWallService
								.getMonitor(alarmTypeKey);
						GenericPassiveProbe alarmStateMonitor = (GenericPassiveProbe) smartWallService
								.getMonitor(alarmStateKey);
						monitorServer.getResultDispatcher()
								.dispatch(new MonitorResult(alarmTypeMonitor, alarmData.getAlarmType()));
						monitorServer.getResultDispatcher()
								.dispatch(new MonitorResult(alarmStateMonitor, alarmData.getAlarmState()));
					}
				}
			}
		}
	}

}

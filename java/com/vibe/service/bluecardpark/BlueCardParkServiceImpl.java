package com.vibe.service.bluecardpark;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.drivers.genericpassive.probe.GenericPassiveProbe;
import com.vibe.monitor.drivers.genericpassive.service.GenericPassiveService;
import com.vibe.monitor.result.MonitorResult;
import com.vibe.monitor.server.MonitorServer;
import com.vibe.pojo.BlueCardParkAreaBean;
import com.vibe.pojo.BlueCardParkBean;

@Service
public class BlueCardParkServiceImpl implements BlueCardParkService {
	@Autowired
	private MonitorServer monitorServer;

	@Autowired
	private AssetStore assetStore;

	@Override
	public void heartbeatInterface(BlueCardParkBean blueCardParkBean) {
		// TODO Auto-generated method stub
		if (blueCardParkBean != null) {
			if (blueCardParkBean.getAreaList() != null) {
				Collection<Asset<?>> genericPassiveServices = assetStore
						.getAssets(GenericPassiveService.class);
				GenericPassiveService blueCardParkService = null;
				for (Asset<?> asset : genericPassiveServices) {
					GenericPassiveService service = (GenericPassiveService) asset;
					if(service.getName().equals("BlueCardPark")){
						blueCardParkService = service;
						break;
					}
				}
				for (BlueCardParkAreaBean blueCardParkAreaBean : blueCardParkBean.getAreaList()) {
					String spaceCountKey = blueCardParkAreaBean.getAreaId() + "spaceCount";
					String lastSpaceCountKey = blueCardParkAreaBean.getAreaId() + "lastSpaceCount";
					GenericPassiveProbe spaceCountMonitor = (GenericPassiveProbe) blueCardParkService
							.getMonitor(spaceCountKey);
					GenericPassiveProbe lastSpaceCountMonitor = (GenericPassiveProbe) blueCardParkService
							.getMonitor(lastSpaceCountKey);
					monitorServer.getResultDispatcher()
							.dispatch(new MonitorResult(spaceCountMonitor, blueCardParkAreaBean.getSpaceCount()));
					monitorServer.getResultDispatcher().dispatch(
							new MonitorResult(lastSpaceCountMonitor, blueCardParkAreaBean.getLastSpaceCount()));
				}
			}
		}
	}

}

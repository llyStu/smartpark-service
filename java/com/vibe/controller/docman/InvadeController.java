package com.vibe.controller.docman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.drivers.websocket.securityalarm.service.SocketSecurityService;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;

@RestController
public class InvadeController {

	@Autowired
	private AssetStore assetStore;

	@RequestMapping("/invade/removieAlarm")
	public ResponseModel<String> removieAlarm() {
		boolean removeAlarm = false;
		for (Asset<?> asset : assetStore.getAssets()) {
			if (asset instanceof SocketSecurityService) {
				SocketSecurityService service = (SocketSecurityService) asset;
				if (!removeAlarm)
					removeAlarm = service.recoverAlarms();
			}
		}

		if(removeAlarm){
			return ResponseModel.success("").code(ResultCode.SUCCESS);
		}else{
			return ResponseModel.failure("暂无告警").code(ResultCode.ERROR);
		}
	}


}

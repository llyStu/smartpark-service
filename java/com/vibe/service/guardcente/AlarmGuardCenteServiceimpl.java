package com.vibe.service.guardcente;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.db.RecordInformationMapper;
import com.vibe.monitor.drivers.guardcentedahua.probe.DSSH8900IntProbe;
import com.vibe.monitor.drivers.guardcentedahua.probe.DSSH8900Probe;
import com.vibe.monitor.drivers.guardcentedahua.service.DSSH8900Service;
import com.vibe.monitor.result.MonitorResult;
import com.vibe.monitor.server.MonitorServer;
import com.vibe.monitor.util.UnitInterface;
import com.vibe.monitor.util.UnitUtil;
import com.vibe.pojo.guardcente.CallbackGeneralMsg;
import com.vibe.pojo.guardcente.CardLogData;
import com.vibe.pojo.guardcente.DeviceAlertData;

@Service
public class AlarmGuardCenteServiceimpl implements AlarmGuardCenteService {

    @Autowired
    private AssetStore assetStore;
    @Autowired
    private MonitorServer monitorServer;
    @Autowired
    private RecordInformationMapper recordInformationMapper;

    private HashMap<String, DSSH8900Probe> dSSH8900Probes = new HashMap<>();
    private final static String DEVICE_ALARM = "EVENT_DEVICE_ALARM_RECORD";
    private final static String SWING_CARD = "EVENT_SWING_CARD_RECORD";
    // private final static String PROBE_DOOR_INT_TYPE="DSSH8900DoorIntProbe";
    private final static String PROBE_INTRUDER_INT_TYPE = "DSSH8900IntruderIntProbe";
    private final static String DEFAULT_CARD = "未获取到该资源";
    private final static int DEFAULT_VALUE = 0;

    @Override
    public void acquireAlarm(CallbackGeneralMsg callbackGeneralMsg) {
        // TODO Auto-generated method stub
        if (dSSH8900Probes.size() <= 0) {
            for (Asset<?> asset : assetStore.getAssets()) {
                if (asset instanceof DSSH8900Service) {
                    // if(dSSH8900Probe.getType().equals(PROBE_DOOR_INT_TYPE))
                    DSSH8900Service dSSH8900Service = (DSSH8900Service) asset;
                    for (Monitor<?> monitor : dSSH8900Service.getMonitors()) {
                        DSSH8900IntProbe dSSH8900IntProbe = (DSSH8900IntProbe) monitor;
                        dSSH8900Probes.put(dSSH8900IntProbe.getChannelCode(), dSSH8900IntProbe);
                        monitorServer.getResultDispatcher().dispatch(new MonitorResult(dSSH8900IntProbe, DEFAULT_VALUE));
                    }
                }
            }
        }

        JSONObject DatetoJson = (JSONObject) JSONObject.toJSON(callbackGeneralMsg.getData());
        switch (callbackGeneralMsg.getInterfaceCode()) {
            case DEVICE_ALARM:
                DeviceAlertData deviceAlertData = JSON.toJavaObject(DatetoJson, DeviceAlertData.class);
                DSSH8900IntProbe deviceProbe = (DSSH8900IntProbe) dSSH8900Probes.get(deviceAlertData.getAlarmChannelCode());
                if (null != deviceProbe && deviceProbe.getType().equals(PROBE_INTRUDER_INT_TYPE))
                    monitorServer.getResultDispatcher()
                            .dispatch(new MonitorResult(deviceProbe, deviceAlertData.getAlarmStat()));
                break;
            case SWING_CARD:
                CardLogData cardLogData = JSON.toJavaObject(DatetoJson, CardLogData.class);
                DSSH8900IntProbe doorProbe = (DSSH8900IntProbe) dSSH8900Probes.get(cardLogData.getAcsChannelCode());
                if (null != doorProbe) {
                    monitorServer.getResultDispatcher().dispatch(new MonitorResult(doorProbe, Integer.parseInt(cardLogData.getOpenType())));
                    String cardNumber = cardLogData.getCardNumber();// 卡号
                    String personName = cardLogData.getPersonName();// 人员名称
                    final String cardMark = null == cardNumber || "".equals(cardNumber) ? DEFAULT_CARD : cardNumber;
                    final String cardName = null == personName || "".equals(personName) ? DEFAULT_CARD : personName;
                    String unit = doorProbe.getUnit();
                    UnitUtil.unitParse(unit, cardLogData.getOpenType(), new UnitInterface() {
                        @Override
                        public void parseResult(String result) {
                            cardLogData.setUnit(result);
                        }

                    });
                    recordInformationMapper.insertRecord(cardMark, cardName, cardLogData.getSwingTime(), cardLogData.getUnit(),
                            doorProbe.getId());
                }
                break;
        }
    }

}

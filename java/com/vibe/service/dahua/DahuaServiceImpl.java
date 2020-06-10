package com.vibe.service.dahua;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ibm.icu.text.MessageFormat;
import com.vibe.client.DahuaClient;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.drivers.guardcentedahua.service.DSSH8900Service;
import com.vibe.pojo.dahua.ServerInfo;
import com.vibe.util.HttpClientPoolUtil;
import com.vibe.util.constant.Constants;

@Service
public class DahuaServiceImpl implements DahuaService {

    @Autowired
    private AssetStore assetStore;

    private static Map<String, String> headMap = new HashMap<String, String>();


    //	private final static String ip="10.10.1.17";
//	private final static String port= "8314";
//	private final static String userName="system";
//	private final static String password="admin123";
    static {
        headMap.put("Content-Type", "application/json;charset=UTF-8");
    }

    @Override
    public ServerInfo getServer() {
        ServerInfo info = new ServerInfo();
        for (Asset<?> asset : assetStore.getAssets()) {
            if (asset instanceof DSSH8900Service) {
                // if(dSSH8900Probe.getType().equals(PROBE_DOOR_INT_TYPE))
                DSSH8900Service dSSH8900Service = (DSSH8900Service) asset;
                info.setIp(dSSH8900Service.getIp());
                info.setPort(dSSH8900Service.getPort());
                info.setUserName(dSSH8900Service.getUserName());
                info.setPassword(dSSH8900Service.getPassword());
            }
        }
        return info;
    }


    public String getToken(ServerInfo info) {
        //获取登录token
        String value = "";
        try {
            value = DahuaClient.login(info.getIp(), info.getPort(), info.getUserName(), info.getPassword());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String token = "";
        if (!"".equals(value)) {
            Map mapToken = (Map) JSON.parse(value);
            token = (String) mapToken.get("token");
        }
        return token;
    }

    public void getAccessToken(ServerInfo info) {
        //获取accessToken   停车场内部调用鉴权
        String accessToken = "";
        String value = "";
        headMap.put("Authorization", getToken(info));
        String url = MessageFormat.format(Constants.GET_ACCESS_TOKEN, info.getIp(), info.getPort()) + "?token=" + headMap.get("Authorization") + "&userName=" + info.getUserName();
        try {
            value = DahuaClient.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(value)) {
            Map mapValue = (Map) JSON.parse(value);
            String date = mapValue.get("data").toString();
            Map mapToken = (Map) JSON.parse(date);
            accessToken = mapToken.get("accessToken").toString();
        }
        headMap.put("accessToken", accessToken);
    }

    @Override
    public String getVisitor(int pageNum, int pageSize, String singleCondition, String status, ServerInfo info) throws Exception {
        String value = "";
        String dataStr = "";
        //headMap.put("token", getToken());
        String tk = getToken(info);
        String url = MessageFormat.format(Constants.GET_VISITOR, info.getIp(), info.getPort()) + "?token=" + tk;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pageNum", pageNum);
        param.put("pageSize", pageSize);
        param.put("singleCondition", singleCondition);
        param.put("status", status);
        try {
            value = HttpClientPoolUtil.post(url, JSON.toJSONString(param), headMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new Exception("获取数据失败");
        }
        return value;
    }

    @Override
    public String getChargeInfo(int pageNum, int pageSize, String startTime, String endTime, String carNum,
                                String ownerType, String carType, String ownerName, Integer feeType, Integer operatorId, ServerInfo info) {
        String value = "";
        getAccessToken(info);
        String atk = headMap.get("accessToken");
        String tk = headMap.get("Authorization");
        String url = MessageFormat.format(Constants.GET_CHARGE, info.getIp(), info.getPort()) + "?accessToken=" + atk + "&token=" + tk +
                "&pageNum=" + pageNum +
                "&pageSize=" + pageSize;
//																		"&startTime="+startTime+"&endTime="+endTime;
        if (!"".equals(carNum)) {
            url = url + "&carNum=" + carNum;
        }
        if (!"".equals(ownerType)) {
            url = url + "&ownerType=" + ownerType;
        }
        if (!"".equals(carType)) {
            url = url + "&carType=" + carType;
        }
        if (!"".equals(ownerName)) {
            url = url + "&carType=" + ownerName;
        }
        if (feeType != null) {
            url = url + "&feeType=" + feeType;
        }
        try {
            value = DahuaClient.get(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //解析json数据
        return value;
    }


    @Override
    public String getParkinglotInfo(String parkingLotCode, String parkingLot, ServerInfo info) {
        String value = "";
        getAccessToken(info);
        String atk = headMap.get("accessToken");
        String tk = headMap.get("Authorization");
        String url = MessageFormat.format(Constants.GET_PARKINGLOT, info.getIp(), info.getPort()) + "?accessToken=" + atk + "&token=" + tk + "&parkingLot=" + parkingLot + "&parkingLotCode=" + parkingLotCode;
        try {
            value = DahuaClient.get(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }


    public void main(String[] args) {
        try {
//			System.out.println(new DahuaServiceIpml().getVisitor(1,20,"",""));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

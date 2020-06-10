package com.vibe.service.energy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.energy.HandInputMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.db.ResultDAO;
import com.vibe.monitor.drivers.input.probe.InputProbe;
import com.vibe.monitor.server.MonitorServer;
import com.vibe.pojo.HandInputData;
import com.vibe.pojo.HandInputProbe;
import com.vibe.pojo.energy.ProbeMeter;
import com.vibe.scheduledtasks.AssetStoreSqlSession;
import com.vibe.utils.time.TimeUtil;

@Service
public class HandInputServiceImpl implements HandInputService {
	@Autowired
	private HandInputMapper mapper;
	@Autowired
	private MonitorServer monitorServer;
	private ResultDAO resultDAO = new ResultDAO(AssetStoreSqlSession.getSession());
	@Override
	public void handInputData(List<HandInputData> datas) {
		SimpleDateFormat SDF = new SimpleDateFormat(TimeUtil.DATETIME_FORMAT_STR, Locale.getDefault());
		for(HandInputData handInputData : datas){
			mapper.addHandInputData(handInputData);
			monitorServer.inputFloatProbeData(handInputData.getMonitor(), handInputData.getValue(),TimeUtil.string2Date(handInputData.getLookTime(), SDF));
		}
	}
	@Override
	public void updateHandInput(HandInputData data) {
		// TODO Auto-generated method stub
		SimpleDateFormat SDF = new SimpleDateFormat(TimeUtil.DATETIME_FORMAT_STR, Locale.getDefault());
		HandInputData handInputData = mapper.findAHandInput(data.getId()+"");
		resultDAO.updateFloat(handInputData.getMonitor(), TimeUtil.string2Date(handInputData.getLookTime(),SDF),
				data.getMonitor(), TimeUtil.string2Date(data.getLookTime(),SDF),data.getValue());
		mapper.updateHandInput(data);
	}
	@Override
	public List<HandInputData> findHandInput(String start,String end) {
		// TODO Auto-generated method stub
		return mapper.findHandInput(start,end);
	}
	@Override
	public void deleteHandInput(String idStr) {
		// TODO Auto-generated method stub
		SimpleDateFormat SDF = new SimpleDateFormat(TimeUtil.DATETIME_FORMAT_STR, Locale.getDefault());
		String[] idStrs = idStr.split(",");
		for(String idStrItem : idStrs){
			HandInputData handInputData = mapper.findAHandInput(idStrItem);
			resultDAO.deleteData("t_sample_float",handInputData.getMonitor(), TimeUtil.string2Date(handInputData.getLookTime(),SDF));
			mapper.deleteHandInput(idStrItem);
		}
	}
	@Override
	public List<HandInputProbe> getHandInputProbe() {
		// TODO Auto-generated method stub
		Collection<Asset<?>> data =  AssetStoreSqlSession.getAssetStore().getAssets(InputProbe.class);
		List<HandInputProbe> list = new ArrayList<HandInputProbe>();
		for(Asset<?> item : data){
			InputProbe inputProbe = (InputProbe)item;
			HandInputProbe handInputProbe = new HandInputProbe();
			handInputProbe.setName(inputProbe.getCaption());
			handInputProbe.setCatlog(inputProbe.getMonitorKind());
			handInputProbe.setMonitorId(inputProbe.getId());
			list.add(handInputProbe);
		}
		return list;
	}
	@Override
	public List<ProbeMeter> getProbe(int energyType, int subitemType, int type) {
		return mapper.getProbe(energyType,subitemType,type);
	}

	@Override
	public HandInputData findHandInputById(int id) {

		HandInputData handInputData= mapper.findHandInputById(id);
		if(handInputData.getParentId() == 0) {
			handInputData.setParentId(handInputData.getChildId());
			handInputData.setParentName(handInputData.getChildName());
			handInputData.setChildName("");
			handInputData.setChildId(null);

		}
		return mapper.findHandInputById(id);
	}
	
	
}

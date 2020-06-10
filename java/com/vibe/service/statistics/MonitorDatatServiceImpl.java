package com.vibe.service.statistics;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vibe.util.ScaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.statistics.MonitorDataMapper;
import com.vibe.pojo.MonitorData;
import com.vibe.util.TableDataType;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.time.TimeUtil;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.asset.Probe;
import com.vibe.monitor.asset.Space;

@Service
public class MonitorDatatServiceImpl implements MonitorDataService {

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	private MonitorDataMapper mapper;
	
	@Autowired
	private AssetStore assetStore;
	

	public MonitorDatatServiceImpl() {
		// assetStore = AssetStore.getInstance();
	}

	@Override
	public Map<String, Object> getRecentData(int monitorId, int count) {
		Asset<?> asset = assetStore.findAsset(monitorId);
		
		//System.out.println(asset);
		if (asset == null)
			return null;

		if (!asset.isMonitor())
			return null;

		// TODO: local date time to UTC
		Monitor<?> monitor = (Monitor<?>) asset;
		final long seconds = monitor.getSavingInterval().toSeconds() * count;
		LocalDateTime startTime = LocalDateTime.now().minusSeconds(seconds);

		String dataTypeName = monitor.getDataTypeName();
		String tableName = TableDataType.getTableName(dataTypeName);
		List<MonitorData> loadRecently = mapper.loadRecently(tableName,monitorId, startTime, null);
		//System.out.println(loadRecently);
		List<Object> xAxis = new LinkedList<Object>();
		List<Object> values = new LinkedList<Object>();
		for (MonitorData data : loadRecently) {
			values.add(data.getValue() == null ? "_" : data.getValue());
			xAxis.add(dateFormatter.format(data.getMoment()));
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xAxis", xAxis);
		map.put("values", values);
		return map;
	}

	@Override
	public Map<String, Object> queryMonitorDataListByPage(CommonMonitorDataVo vo, Integer page, Integer rows) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		LocalDateTime startTime = LocalDateTime.now().minusHours(2);
		if (vo.getStartTime() != null) {
			startTime = TimeUtil.timestampStrToLocalDateTime(vo.getStartTime());
					//LocalDateTime.parse(vo.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		LocalDateTime lastTime = LocalDateTime.now();
		if (vo.getLastTime() != null) {
			lastTime = TimeUtil.timestampStrToLocalDateTime(vo.getLastTime());
					//LocalDateTime.parse(vo.getLastTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		//System.out.println(startTime +"=============="+ startTime);
		Asset<?> asset = assetStore.findAsset(vo.getMonitorId());
		if(!asset.isMonitor()){
			return map;
		}
		Monitor<?> monitor = (Monitor<?>)asset;
		String dataTypeName = monitor.getDataTypeName();
		String tableName = TableDataType.getTableName(dataTypeName);
		PageHelper.startPage(page, rows);
		List<MonitorData> loadRecently = mapper.loadRecently(tableName,vo.getMonitorId(), startTime, lastTime);

		if (loadRecently != null && loadRecently.size() > 0) {
			
			for (MonitorData monitorData : loadRecently) {
				Date moment = monitorData.getMoment();
				if(moment != null){
					String xaxi=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(moment).substring(0,16);
					monitorData.setName(xaxi);
				}
				Object value = monitorData.getValue();
				if (value == null) {
					monitorData.setValue(monitorData.getError());
				}else{
					
						String unit=monitorData.getUnit();
						 Double dvalue =Double.parseDouble(value.toString());
						 String doubleValue= dvalue.toString();//new UseCode().getValue(value,(short)Code.PROBE,(short)(int)vo.getCatalog());
						 monitorData.setValue(doubleValue+(unit== null?"":unit));
					
				}
			}
		}
		PageInfo<MonitorData> pageInfo = new PageInfo<MonitorData>(loadRecently);
		EasyUIJson uiJson = new EasyUIJson();
		uiJson.setTotal(pageInfo.getTotal());
		uiJson.setRows(loadRecently);

		List<Object> xAxis = new LinkedList<Object>();
		List<Object> values = new LinkedList<Object>();
		for (MonitorData data : loadRecently) {
			values.add(data.getValue() == null ? "_" : data.getValue());
			// xAxis.add(data.getMoment());
			xAxis.add(data.getName());//时间的字符串
		}
		
		
		
		//获取监测值单位
  		String unit1 = mapper.getUnit(vo.getMonitorId()); 		

  		map.put("unit1", unit1);
		
		map.put("xAxis", xAxis);
		map.put("values", values);
		map.put("monitorListData", uiJson);
		return map;
	}
//
//	@Override
//	public int subscribeUpdates(int parentAssetId, int depth) {
//		return server.subscribeAssetUpdates(parentAssetId, depth);
//	}
//
//	@Override
//	public List<AssetUpdate> getUpdates(int subscribeId) {
//		return server.getAssetUpdates(subscribeId);
//	}
/**
 * 获取监测器最新的一条数据
 */
	@SuppressWarnings("unused")
	@Override
	public MonitorData getOneRecentData(int monitorId) {
		
		Asset<?> asset = assetStore.findAsset(monitorId);
		if (asset == null)
			return null;

		if (!asset.isMonitor())
			return null;

		// TODO: local date time to UTC
		Monitor<?> monitor = (Monitor<?>) asset;
            Long tiem=System.currentTimeMillis()-60*60*1000;
            Timestamp ts = new Timestamp(tiem);
		LocalDateTime startTime = TimeUtil.timestampStrToLocalDateTime(ts.toString());
		String tabeName ="db_vibe_data.t_sample_float";
		List<MonitorData> loadRecently = mapper.loadRecently(tabeName,monitorId, startTime, null);
		MonitorData data = null;
		if(loadRecently !=null && loadRecently.size()>0 ){
			data=loadRecently.get(0);
		}
		//System.out.println(data.getValue());
		return data;
	}
	/**
	 * 获取该节点以及一级子节点
	 */
	@Override
	public  List<Asset<?>> getAllSpace(Integer parentId){
		Integer titleId = getTitleID(parentId);
		Asset<?> root = assetStore.findAsset(titleId);
	    List<Asset<?>> assetList=new ArrayList<Asset<?>>();
	    assetList.add(root);
		if(root != null &&root.isCompound()){
			 CompoundAsset<?> parent = (CompoundAsset<?>)root;
			 Collection<Asset<?>> children = parent.children();
		
			 for (Asset<?> asset : children) {
				 
				if(asset != null && asset.isCompound() ){
					 CompoundAsset<?> aseetParent = (CompoundAsset<?>)asset;
					 Collection<Asset<?>> assetChildren = aseetParent.children();
					for (Asset<?> asset2 : assetChildren) {
					if(asset2 instanceof Probe){
							if(asset instanceof Space){
								 Space space=(Space)asset;
								//System.out.println(space.getArea());
									 assetList.add(space);
									 break;
							}
						}
					}
		      }
		     }
		}
		return assetList;
    }
	/**
     * 获取所有空间下监测的各类型数据，catalogId是类型的id
     * @param list
     * @param catalogId
     * @return
     */
  /*  public List<EnergyData> energyDatac(int parentId,int catalogId){
    	List<EnergyData> list = new ArrayList<EnergyData>();
    	List<Asset<?>> alist = getAllSpace(parentId);
    	
    	if(alist != null && alist.size()>0){
    		for (Asset<?> space : alist) {
    			 if(space.isCompound()){
    				 CompoundAsset<?> parent = (CompoundAsset<?>)space;
    				 Collection<Asset<?>> children = parent.children();
    				 for (Asset<?> asset : children) {
    					 if(asset instanceof Probe){
    						 Probe probe=(Probe)asset;
    						 short catalog = probe.getM();
    						 
    						 if(catalog == catalogId){
    							 EnergyData data= getEnergyData(space,catalogId);
    							 list.add(data);
    						 }
    					 }
    				 }
    			 }
    	    }
       }
    	return list;
    }*/
	/**
	 * 获取root子节点id
	 * @param parentId
	 * @return
	 */
	public Integer getTitleID(Integer parentId) {
		if(parentId ==0){
			 Asset<?> root = assetStore.findAsset(parentId);
			 if(root != null && root.isCompound()){
				 CompoundAsset<?> parent = (CompoundAsset<?>)root;
				 Collection<Asset<?>> children = parent.children();
					Iterator<Asset<?>> iterator = children.iterator();
					Asset<?> asset = iterator.next();
					return asset.getId();
			 }	
		}
		return parentId;
	}
	/*public Map<String,List<MonitorData>> selectTotalData(Integer parentId){
    
    	Map<String,List<MonitorData>> map=new HashMap<String,List<MonitorData>>();
    	Asset<?> root = assetStore.findAsset(parentId);
    	if(root != null &&root.isCompound()){
			 CompoundAsset<?> parent = (CompoundAsset<?>)root;
			 Collection<Asset<?>> children = parent.children();
			ArrayList<MonitorData> water = new ArrayList<MonitorData>();
			ArrayList<MonitorData> electric = new ArrayList<MonitorData>();
			ArrayList<MonitorData> naturalGas = new ArrayList<MonitorData>();
			 for (Asset<?> asset : children) {
				 if(asset!= null && asset instanceof Probe){
					 Probe probe=(Probe)asset;
					 MonitorData monitorData = new MonitorData();
					 
					 short catalog = probe.getCatalog();
					 CommonSelectOption data = selectOptionService.getSelectListData((int)catalog);
					 if(data != null){
						 monitorData.setName(data.getName());
						 monitorData.setUnit(data.getUnit());
						 monitorData.setValue(probe.getValue());
						 monitorData.setId(probe.getId());
						 int id = data.getParentId();
						 if(id == 5){
							 electric.add(monitorData);
						 }else if(id == 6){
							 naturalGas.add(monitorData);
						 }else if(id == 7){
							 water.add(monitorData);
						 }
						 
					 }
					 
				 }
			 }
			 
			map.put("water", water); 
			map.put("electric", electric); 
			map.put("naturalGas", naturalGas); 
		}
    	
    	return map;
    }*/
	/*public EnergyData getEnergyData(Asset<?> space,Integer catalogId) {
		EnergyData energyData = new EnergyData();
		 
		 if(space.isCompound()){
			 CompoundAsset<?> parent = (CompoundAsset<?>)space;
			 Collection<Asset<?>> children = parent.children();
			 for (Asset<?> asset : children) {
				 if(asset instanceof Probe){
					 Probe probe=(Probe)asset;
					 short catalog = probe.getCatalog();
					 if(catalog == catalogId){
						 MonitorData data=getOneRecentData(probe.getId());
						 if(data != null && data.getValue() != null){*/
//							 Date moment = data.getMoment();
//							 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							 String date = simpleDateFormat.format(moment);
						/*	 String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.getMoment());
							 energyData.setDateTime(date==null?"":date);
							 Double value =Double.parseDouble(data.getValue().toString());
							 String doubleValue=NumberFormat.formatDouble(value);
							 energyData.setValue(doubleValue==null?"0":doubleValue);
							 energyData.setProbeId(probe.getId());
							 energyData.setSpaceId(space.getId());
							 energyData.setSpaceName(space.getCaption());
						 }
					 }
			       }
			 }
		 }
	   return energyData; 
}	*/
	//相关性分析
	@Override
	public List<Map<String, Object>> queryMonitorComparedListByPage(Integer monitorId1, Integer monitorId2, String startTime,
			String lastTime, Integer page, Integer rows) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
	     
        List<Integer> monitorIds = new ArrayList<Integer>();
        monitorIds.add(monitorId1);
        monitorIds.add(monitorId2);
        
        for(Integer monitorId: monitorIds){
        	Map<String, Object> map = new HashMap<String, Object>();
        	List<MonitorData> loadRecently = new LinkedList<MonitorData>();
            PageHelper.startPage(page, rows);	
          
            loadRecently = mapper.loadRecently_hour(monitorId,startTime,lastTime);
           
            if(loadRecently !=null && loadRecently.size()>0 ){
            	for (MonitorData monitorData : loadRecently) {
    				Object value = monitorData.getValue();
    				if(value ==null){
    					monitorData.setValue(monitorData.getError());
    				}
    			}
            }
    		PageInfo<MonitorData> pageInfo = new PageInfo<MonitorData>(loadRecently);
    		EasyUIJson uiJson = new EasyUIJson();
    		uiJson.setTotal(pageInfo.getTotal());
    		uiJson.setRows(loadRecently);
    		    		
    		 List<Object> xAxis = new LinkedList<Object>();  
    			List<String> values = new LinkedList<String>();
    			for (MonitorData data : loadRecently) {
    				values.add(data.getValue() == null ? "_" : data.getValue().toString());
    				//xAxis.add(data.getMoment());
    				xAxis.add(data.getMoment());
    			}
    			    			     			
    			map.put("xAxis", xAxis);
    			map.put("values", values);
    			
    			map.put("monitorListData",uiJson);
    			result.add(map);
        }       
		return result;
	}
	
	//趋势对比
	@Override
	public Map<String,Object>  queryMonitorComparedDataByPage(int monitorId1, int monitorId2,
			String startTime2, String lastTime2, Integer page, Integer rows) {
		// TODO Auto-generated method stub
		//List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();

		List<MonitorData> loadRecently1 = mapper.loadRecently_hour(monitorId1,startTime2,lastTime2);
        List<MonitorData> loadRecently2 = mapper.loadRecently_hour(monitorId2,startTime2,lastTime2);
                
        List<String> xAxis = new LinkedList<String>();
        List<Object> values_1 = new LinkedList<Object>();
        List<Object> values_2 = new LinkedList<Object>();
        
        for(MonitorData item:loadRecently1){
        	xAxis.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getMoment()));
      	
        	values_1.add(item.getValue());
        }
        for(MonitorData item:loadRecently2){
        	values_2.add(item.getValue());
        }
        int max_size = Math.max(values_1.size(), values_2.size());
        if(values_1.size()<max_size){
        	for(int i = values_1.size() + 1; i<= max_size; i++){
        		values_1.add("/");
        	}
        }
        if(values_2.size()<max_size){
        	for(int i = values_2.size() + 1; i<= max_size; i++){
        		values_2.add("/");
        	}
        }

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("legend", new String[]{monitorId1+"",monitorId2+""});  
        result.put("name_1", monitorId1);
        result.put("name_2", monitorId2);
        result.put("xAxis", xAxis);
        result.put("values_1", values_1);
        result.put("values_2", values_2);    
        
        //获取监测值单位
  		String unit1 = getUnit(monitorId1);
  		String unit2 = getUnit(monitorId2);

  		result.put("unit1", unit1);
  		result.put("unit2", unit2);
        
		return result;
	}
	
	//统计对比
	@Override
	public Map<String, Object> getMonitorComparedDataByPage(String filterType, int monitorId1, int monitorId2, String startTime,
			String lastTime, Integer page, Integer rows) {
		// TODO Auto-generated method stub
		CommonMonitorDataVo monitorDataVo1 = new CommonMonitorDataVo();
		monitorDataVo1.setMonitorId(monitorId1);
		monitorDataVo1.setStartTime(startTime);
		monitorDataVo1.setLastTime(lastTime);
		monitorDataVo1.setFilterType(filterType);
		CommonMonitorDataVo monitorDataVo2 = new CommonMonitorDataVo();
		monitorDataVo2.setMonitorId(monitorId2);
		monitorDataVo2.setStartTime(startTime);
		monitorDataVo2.setLastTime(lastTime);
		monitorDataVo2.setFilterType(filterType);
		Map<String,Object>map1 = querySingleMonitorData(monitorDataVo1,page,rows);
		Map<String,Object>map2 = querySingleMonitorData(monitorDataVo2,page,rows);
		Map<String,Object>result = new HashMap<String, Object>();
		result.put("xAxis", map1.get("xAxis"));
		result.put("minValues_1", map1.get("minValues"));
		result.put("maxValues_1", map1.get("maxValues"));
		result.put("avgValues_1", map1.get("avgValues"));
		result.put("stdValues_1", map1.get("stdValues"));	
	
		result.put("minValues_2", map2.get("minValues"));
		result.put("maxValues_2", map2.get("maxValues"));
		result.put("avgValues_2", map2.get("avgValues"));
		result.put("stdValues_2", map2.get("stdValues"));	
		
		//获取监测值单位
  		String unit1 = getUnit(monitorId1);
  		String unit2 = getUnit(monitorId2);

  		result.put("unit1", unit1);
  		result.put("unit2", unit2);
		
		return result;
		
	}
	
	//查询单个监测器统计数值（柱状图）
	@Override
	public Map<String, Object> querySingleMonitorData(CommonMonitorDataVo vo, Integer page, Integer rows) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<MonitorData> loadRecently = new LinkedList<MonitorData>();
		PageHelper.startPage(page, rows);
		//System.out.println(vo.getFilterType());
		// 根据filterType判断向哪个mapper接口发请求
		if ("0".equals(vo.getFilterType())) {

			loadRecently = mapper.loadRecently_hour(vo.getMonitorId(), vo.getStartTime(), vo.getLastTime());
		} else {
			if ("4".equals(vo.getFilterType())) {

				loadRecently = mapper.loadRecentlyByYear(vo.getMonitorId(), vo.getStartTime(), vo.getLastTime());
			
			} else if ("3".equals(vo.getFilterType())) {
				loadRecently = mapper.loadRecentlyByMonth(vo.getMonitorId(), vo.getStartTime(), vo.getLastTime());
			} else if ("2".equals(vo.getFilterType())) {
				loadRecently = mapper.loadRecentlyByDay(vo.getMonitorId(), vo.getStartTime(), vo.getLastTime());
			}

		}
		if (loadRecently != null && loadRecently.size() > 0) {
			for (MonitorData monitorData : loadRecently) {
				Object value = monitorData.getValue();
				if (value == null) {
					monitorData.setValue(monitorData.getError());
				}
			}
		}

		List<Object> xAxis = new LinkedList<Object>();
		List<String> minValues = new LinkedList<String>();
		List<String> maxValues = new LinkedList<String>();
		List<String> avgValues = new LinkedList<String>();
		List<String> stdValues = new LinkedList<String>();
		for (MonitorData data : loadRecently) {

			//xAxis.add(new SimpleDateFormat("yyyy-MM-dd").format(data.getStr_moment()).substring(0,10));			
			xAxis.add(data.getStr_moment());
			minValues.add(String.valueOf(data.getMinData()));
			maxValues.add(String.valueOf(data.getMaxData()));
			avgValues.add(String.valueOf(data.getAvgData()));
			stdValues.add(String.valueOf(data.getStdData()));
		}
		map.put("title", "监测数据统计分析柱状图");

		//获取监测值单位
  		String unit1 = getUnit(vo.getMonitorId());     		
  		map.put("unit1", unit1);
		
		map.put("xAxis", xAxis);
		map.put("minValues", minValues);
		map.put("maxValues", maxValues);
		map.put("avgValues", avgValues);
		map.put("stdValues", stdValues);
		return map;
	}
		
	//不同状态的设备详情、不同类型监测器的详情
		public Map<String,List<Object>> getInfoByMonitorState(String filter, int index){
			
			List<Object>monitorIds = new ArrayList<Object>();
			List<Object>captions = new ArrayList<Object>();
			List<Asset<?>> asset_states = new ArrayList<Asset<?>>();
			for (Asset<?> asset : this.assetStore.getAssets()) {
				//System.out.println(asset.getId()+"==="+(asset.getId()>>16)+"=="+((asset.getId()>>16) == 1028));
				//if (AssetKind.PROBE.equals(asset.getKind())) {
					//if((asset.getId()>>16) == 1028){
					asset_states.add(asset);
					//}
				}
			//}
						
			if("state".equals(filter)){
				//如果页面中相应状态和枚举类的状态顺序不一致，使用map进行映射
				//Map<String,Integer>re_map = new HashMap<String,Integer>();
//				re_map.put("0", 1);
//				re_map.put("1", 2);
//				re_map.put("2", 3);	
				for(Asset<?> item:asset_states){	
					//System.out.println(item.getKind()+"=="+"PROBE".equals(item.getKind()));
					if(item.getKind() == AssetKind.PROBE){
											
						if(item.getState().ordinal() == index){					
							monitorIds.add(item.getId());
							captions.add(item.getCaption());					
						}
					}
				}
			}
			else{
				//如果页面中相应状态和枚举类的状态顺序不一致，使用map进行映射			
				List<Map<String, Object>> maps = mapper.queryByType(index);
				for(Map<String, Object> map:maps){
					monitorIds.add((Integer) map.get("monitorId"));
					captions.add((String) map.get("caption"));				
				}
			}
			Map<String, List<Object>> result = new HashMap<String, List<Object>>();
			result.put("monitorIds", monitorIds);
			result.put("captions", captions);
			return result;
			
		}

		//根据传感器id查询监测值单位
		public String getUnit(int monitorId){
			return mapper.getUnit(monitorId);
		}		
	//获取单个监测器自定义时间段监测值
		@Override
		public Map<String, Object> queryMonitorData(CommonMonitorDataVo vo, Integer page, Integer rows) {
			 Map<String, Object> map = new HashMap<String, Object>();
		      
		        List<MonitorData> loadRecently = new LinkedList<MonitorData>();
		        PageHelper.startPage(page, rows);	
		              
		        loadRecently = mapper.loadRecently_str(vo.getMonitorId(),vo.getStartTime(),vo.getLastTime());
		       
		        if(loadRecently !=null && loadRecently.size()>0 ){
		        	for (MonitorData monitorData : loadRecently) {
//						monitorData.setValue(ScaleUtil.roundFloat(monitorData.getValue()));
						monitorData.setValue(monitorData.getValue());
						Object value = monitorData.getValue();
						if(value ==null){
							monitorData.setValue(monitorData.getError());
						}
					}
		        }
				PageInfo<MonitorData> pageInfo = new PageInfo<MonitorData>(loadRecently);
				EasyUIJson uiJson = new EasyUIJson();
				uiJson.setTotal(pageInfo.getTotal());
				uiJson.setRows(loadRecently);
							
				 List<Object> xAxis = new LinkedList<Object>();  
					List<String> values = new LinkedList<String>();
					for (MonitorData data : loadRecently) {
//						values.add(data.getValue() == null ? "_" : ScaleUtil.roundObject(data.getValue()));
						values.add(data.getValue() == null ? "_" : data.getValue().toString());
						//xAxis.add(data.getMoment());
						xAxis.add(data.getMoment());
					}
					
					String unit = mapper.getUnit(vo.getMonitorId());
					List<String> format_values = new LinkedList<String>();
					for (String value : values){
						format_values.add(value);
					}
					map.put("unit", unit);
					map.put("xAxis", xAxis);
					map.put("values", format_values);
					map.put("monitorListData",uiJson);
				return map;
		}

	@Override
	public List<MonitorData> queryIntMonitorValues(int monitorId, String startTime, String lastTime) {
		// TODO Auto-generated method stub
		return mapper.queryIntMonitorValues(monitorId,startTime,lastTime);
	}

	@Override
	public List<Object> getEnvironmentAvgByCodeDay(Integer code, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return mapper.getEnvironmentAvgByCodeDay(code, dateFormat.format(date));
	}

	@Override
	public List<Map<String , Object>> getMonitorCodeName() {
		return mapper.getMonitorCodeName();
	}

	@Override
	public List<MonitorData> queryFloatMonitorValues(int monitorId, String startTime, String lastTime) {
		// TODO Auto-generated method stub
		return mapper.queryFloatMonitorValues(monitorId,startTime,lastTime);
	}

	@Override
	public List<MonitorData> queryBoolMonitorValues(int monitorId, String startTime, String lastTime) {
		// TODO Auto-generated method stub
		return mapper.queryBoolMonitorValues(monitorId,startTime,lastTime);
	}

}

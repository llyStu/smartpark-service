package com.vibe.controller;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.monitor.asset.Asset;
import com.vibe.pojo.AssetUpdate;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.service.statistics.MonitorDataService;
import com.vibe.util.POITool;


@Controller
public class MonitorDataController {
	
	@Autowired
	private MonitorDataService service;

	/**
	 * Get recently monitor result list. 
	 * @param monitorId
	 * @return null if no result is found.
	 */
	@RequestMapping(value="/monitor_data/recent_data/{monitorId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getRecentData(@PathVariable("monitorId") int monitorId) {
		Map<String, Object> results = service.getRecentData(monitorId, 20);
		System.out.println(results);
		return results;
	}		

	@RequestMapping(value="/monitor_data/recent_chart/{monitorId}", method = RequestMethod.GET)
	public String getRecentChart(ModelMap model, @PathVariable("monitorId") int monitorId) {
	    model.addAttribute("monitorId", monitorId);
	    return "monitor/data_chart";
	}		
	   
	@RequestMapping(value="/monitor_data/monitorDataList")
	public @ResponseBody Map<String,Object> getRecentLineValues(CommonMonitorDataVo vo, @RequestParam(defaultValue = "1") Integer page,
                                                                @RequestParam(defaultValue = "20") Integer rows){
		Map<String,Object> lines = service.queryMonitorDataListByPage(vo, page, rows);
		return lines;
	}	

	/**
	 * Subscribe update data of an asset and its children.
	 * @param assetId
	 * @param depth the depth of asset sub-tree.
	 * 		Default value 1 means no children data.
	 * @param monitorCatalog filter data by monitor catalog.
	 * @return a subscription id for retrieving data later.
	 */
	@RequestMapping(value="/monitor_data/subscribe_updates")
	public @ResponseBody int subscribeUpdates (
			@RequestParam(value="asset_id", required=true) Integer assetId,
			@RequestParam(value="depth", defaultValue="1") Integer depth,
			@RequestParam(value="monitor_catalog") Integer monitorCatalog) {
		return 0; //TODO: server.subscribeAssetUpdates(assetId, depth);
	}	
	
	/**
	 * Get subscribed update data。
	 * @param subscribeId
	 * @return a list of asset update data. 
	 */
	@RequestMapping(value="/monitor_data/get_updates")
	public @ResponseBody List<AssetUpdate> getUpdates(
			@RequestParam(value="subscribe_id", required=true) Integer subscribeId) {
		return null; //TODO: return service.getAssetUpdates(subscribeId);
	}	
	/**
	 *获取父和子的监测数据
	 * @param parentId,catalogId
	 * @return List<EnergyData> 
	 */
	/*@RequestMapping(value="/monitor_data/energy_data")
	public @ResponseBody List<EnergyData> energyDatac(
			@RequestParam(defaultValue="0") Integer parentId,Integer catalogId) {
		 List<EnergyData> data=  service.energyDatac(parentId, catalogId);
		 return data;
	 }*/	
	 /**
	  * 能源管理，总体的页面列表
	  * @param asset
	  * @return
	  */
	@RequestMapping("/energy/get_all_space")
	public @ResponseBody List<Asset<?>> totalLeftSpace(@RequestParam(defaultValue="0") Integer parentId){
		
		return service.getAllSpace(parentId);
	}
	
	
    
    /**
     * 能源管理总体页面
     * 
     */
   /* @RequestMapping("/energy/selectTotalData")
    public @ResponseBody Map<String,List<MonitorData>> selectTotalData(@RequestParam(defaultValue="0") Integer parentId){
    	parentId = service.getTitleID(parentId);
    	return service.selectTotalData(parentId);
    }
    */
  /*
   * 获取自定义时间段两监测器的相关性分析
   * */
  	@RequestMapping(value = "/monitor_data/monitorDataRegression")
  	public @ResponseBody Map<String, Object> getMonitorDataRegression(Integer monitorId1, Integer monitorId2,
  			String startTime, String lastTime, @RequestParam(defaultValue = "1") Integer page,
  			@RequestParam(defaultValue = "20") Integer rows) {
  		List<Map<String, Object>> lines = service.queryMonitorComparedListByPage(monitorId1, monitorId2,
  				startTime, lastTime, page, rows);
  		Map<String, Object> result = new HashMap<String, Object>();
  				
  		List<String> list =  (List<String>) lines.get(0).get("values");
  		
  		List<String> list2 = (List<String>) lines.get(1).get("values");

  		result.put("x", list);
  		result.put("y", list2);
  		List<Object> points = new LinkedList<Object>();
  		/*for(int i=0;i<list.size();i++){
  			points.add(new double[]{
  					Double.parseDouble(new DecimalFormat(".##").format(list.get(i))),
  					Double.parseDouble(new DecimalFormat(".##").format(list2.get(i)))});
  			
  		}*/
  		int minListSize = Math.min(list.size(), list2.size());
  		for(int i=0;i<minListSize;i++){
  			points.add(new double[]{
  					Double.parseDouble(new DecimalFormat(".##").format(Double.valueOf(list.get(i)))),
  					Double.parseDouble(new DecimalFormat(".##").format(Double.valueOf(list2.get(i))))});
  			
  		}
  		result.put("points", points);
  		//获取监测值单位
  		String unit1 = service.getUnit(monitorId1);
  		String unit2 = service.getUnit(monitorId2);

  		result.put("unit1", unit1);
  		result.put("unit2", unit2);
  		
  		return result;
  	}
    
  /*获取自定义时间段两个监测器监测数据曲线图
   * */  	 
	@RequestMapping(value = "/monitor_data/monitorComparedDataCal")
	public @ResponseBody Map<String, Object> getComparedValues(int monitorId1, int monitorId2, String filterType,
			String startTime, String lastTime, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows) {
		
		Map<String, Object> lines = service.queryMonitorComparedDataByPage(monitorId1, monitorId2, startTime, lastTime,
				page, rows);
		return lines;
	}
	
	/*获取两个同类设备监测数据统计
	 * */
	@RequestMapping(value = "/monitor_data/monitorDataCalCompared")
	public @ResponseBody Map<String, Object> getCalComparedData(String filterType, int monitorId1, int monitorId2,
			String startTime, String lastTime, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows) {
		Map<String, Object> lines = service.getMonitorComparedDataByPage(filterType, monitorId1, monitorId2,
				startTime, lastTime, page, rows);
		return lines;
	}
	
	/*获取单个设备监测数据统计
	 * */
	@RequestMapping(value = "/monitor_data/monitorDataCal")
	public @ResponseBody Map<String, Object> querySingleMonitorData(CommonMonitorDataVo vo,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer rows) {

		Map<String, Object> lines = service.querySingleMonitorData(vo, page, rows);
		return lines;
	}
	
	// 获取自定义时间段单个设备监测数据
		@RequestMapping(value = "/monitor_data/getSingleData")
		public @ResponseBody Map<String, Object> getMonitorLineValues(CommonMonitorDataVo vo,
				@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer rows) {

			Map<String, Object> lines = service.queryMonitorData(vo, page, rows);
			return lines;
		}
		//导出
		@RequestMapping("/exportMonitorData")
		public String exportMonitorData(
				@RequestParam("dataType")Integer dataType, 
				@RequestParam(value="filterType",required=false)String filterType, 
				@RequestParam(value="monitorId",required=false)Integer monitorId,
				@RequestParam(value="monitorId1",required=false)Integer monitorId1, 
				@RequestParam(value="monitorId2",required=false)Integer monitorId2,
				@RequestParam(value="startTime",required=false)String startTime, 
				@RequestParam(value="lastTime",required=false)String lastTime, 
				@RequestParam(defaultValue = "1") Integer page,
				@RequestParam(defaultValue = "20") Integer rows,
				HttpServletResponse response){
			
			String fileName = "";
			//两个监测器柱状图数据导出
			if(dataType == 1){
				Map<String, Object> lines = service.getMonitorComparedDataByPage(filterType, monitorId1, monitorId2,
						startTime, lastTime, page, rows);
				fileName = "监测数据统计对比";
				String curr = new SimpleDateFormat("yyyy-MM-dd-HH时mm分ss秒").format(new Date());
				fileName += curr;
				POITool.mapToExcel2(lines, "C:/download", fileName + ".xls");	
				POITool.exportToDisk(fileName);
				return "statistics/classified_statistic";
			}
			//两个监测器曲线数据导出
			else if(dataType == 2){
				Map<String,Object> lines = service.queryMonitorComparedDataByPage(monitorId1, monitorId2, 
						startTime, lastTime, page, rows);
				fileName = "监测值对比";
				String curr = new SimpleDateFormat("yyyy-MM-dd-HH时mm分ss秒").format(new Date());
				fileName += curr;
				POITool.mapToExcel2(lines, "C:/download", fileName + ".xls");
				POITool.exportToDisk(fileName);
				return "statistics/data_contrast";
			}
			//两个监测器相关性分析数据导出
			else if(dataType == 3){
				Map<String,Object> lines = 
						this.getMonitorDataRegression(monitorId1, monitorId2, startTime, lastTime, page, rows);
				fileName = "相关性分析";
				String curr = new SimpleDateFormat("yyyy-MM-dd-HH时mm分ss秒").format(new Date());
				fileName += curr;
				POITool.mapToExcel2(lines, "C:/download", fileName + ".xls");
				POITool.exportToDisk(fileName);
				return "statistics/monitor_pertinence";
			}
			//单个监测器的柱状图
			else if(dataType == 4){
				Map<String,Object> lines = 
						service.querySingleMonitorData(new CommonMonitorDataVo(startTime,lastTime,monitorId,filterType)
								, page, rows);				
				/*Map<String,Object> lines = 
						service.queryMonitorDataListByPage(new MonitorDataVo(startTime,lastTime,monitorId,filterType)
								, page, rows);*/							
				fileName = "单个监测器统计数据";
				
				String curr = new SimpleDateFormat("yyyy-MM-dd-HH时mm分ss秒").format(new Date());
				fileName += curr;

				POITool.mapToExcel2(lines, "C:/download", fileName + ".xls");							
				POITool.exportToDisk(fileName);
				return "statistics/monitor_history";
			}	
			//单个监测器曲线
			else{

				Map<String,Object> lines = 
						service.queryMonitorDataListByPage(new CommonMonitorDataVo(startTime,lastTime,monitorId,filterType)
								, page, rows);
				fileName = "实时监测";
				
				String curr = new SimpleDateFormat("yyyy-MM-dd-HH时mm分ss秒").format(new Date());
				fileName += curr;
				POITool.mapToExcel2(lines, "C:/download", fileName + ".xls");
							
				POITool.exportToDisk(fileName);
				return "statistics/singleDetection";
			}
		}
	
}
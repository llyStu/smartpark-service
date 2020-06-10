package com.vibe.controller.energy;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetException;
import com.vibe.parse.ExcelSheetPO;
import com.vibe.pojo.energy.MeterRelation;
import com.vibe.service.energy.EnergyMeterService;
import com.vibe.service.energy.Meter;
import com.vibe.service.logAop.MethodLog;
import com.vibe.util.ExcelUtil;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.FormJson;
import com.vibe.utils.TreeNode;

@Controller
public class EnergyMeterController {
	
    @Autowired
    private EnergyMeterService meterService;
	@Autowired
	private Application application;
    /*
     * 获取实表树
     * meterType  表的类型 0实表 1虚表 2累加表
     * parentMeter 实表的父节点，根节点是0
     */
    @RequestMapping("/energy/meter_tree")
    public  @ResponseBody  List<TreeNode> queryEnergyDataListByPage(Meter meter){
    	List<TreeNode> meterTree = meterService.getMeterTree(meter);
    	return meterTree;
    }
    @RequestMapping("/energy/queryMeter")
    public  @ResponseBody
    Meter queryEnergyMarer(Integer id){
    	Meter meter = meterService.queryEnergyMeterByProbeId(id);
    	return meter;
    }
    /*
     * parentMeter
     * 参数catalogId  分类电、水、热的
     * 搜索条件参数
     * 查询实表的分页列表
     */
    @RequestMapping("/energy/queryMeterList")
	public @ResponseBody EasyUIJson queryMeterList(Meter meter,
			@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="10")Integer rows) {
		EasyUIJson uiJson = meterService.meterList(rows, page, meter);                                                                                                          
		return uiJson;
	}
    /*
     * 参数catalogId  分类电、水、热的
     * 搜索条件参数
     * 添加实表
     */
    @RequestMapping("/energy/addMeter")
    @MethodLog(remark="add",option="添加计量表")
	public @ResponseBody String addMeter(Meter meter) {
    	try {
    		/*IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
			int id = gen.next();
			meter.setId(id);*/
			meterService.addMeterGrade(meter);
			meterService.addEnergyProbe(meter);
			return "200";
		} catch (AssetException e) {
			e.printStackTrace();
			return "500";
		}       
	}
    @RequestMapping("/energy/updateMeter")
    @MethodLog(remark="edit",option="更新表的信息")
	public @ResponseBody String updateMeter(Meter meter) {
		try {
			meterService.updateEnergyMeter(meter);
			return "200";
		} catch (AssetException e) {
			e.printStackTrace();
			return "500";
		}
	}
    /*删除资产*/
	@RequestMapping("/energy/deleteMeter")
	@MethodLog(remark="edit",option="删除表信息")
	public @ResponseBody String deleteMeter(String ids)  {//0,不删，1是删

		try {
			meterService.deleteMeter(ids);
			return "200";
		} catch (AssetException e) {
			e.printStackTrace();
			return "500";
		}
	}
	/*
     * 获取虚表带空间树
     * catalogId 能源的类型catalog 水、电、热
     */
    @RequestMapping("/energy/dummy_meter")
    public  @ResponseBody  List<TreeNode> queryDummyMeter(Integer catalogId){
    	List<TreeNode> meterTree = meterService.queryDummyMeter(catalogId);
    	return meterTree;
    }
    @RequestMapping("/energy/addDummyMeter")
   	public @ResponseBody String addDummyMeter(Meter meter){
       	try {
       		/*IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
   			int id = gen.next();
   			meter.setId(id);*/
   			meterService.addEnergyProbe(meter);
   			return "200";
   		} catch (AssetException e) {
   			e.printStackTrace();
   			return "500";
   		}       
   	} 
    /*
     * Integer parentId,Integer itemizeType
     */
    @RequestMapping("/energy/queryChildMeter")
    public @ResponseBody List<Meter> queryChildMeter(Meter meter){
    	return meterService.queryChildMeter(meter);
    }
    /**
	 * 读取excel文件中的用户信息，保存在数据库中
	 * /energy/imEnergyMeter
	 */
	@RequestMapping("/energy/imEnergyMeter")
	  @MethodLog(remark="input",option="导入计量表")
	public @ResponseBody ResponseModel imEnergyMeter(@RequestParam(value = "file") MultipartFile file) {
		ResponseModel responseModel = new ResponseModel();
		try {
			List<ExcelSheetPO> excelSheet = ExcelUtil.readExcel(file);
			meterService.imEnergyMeter(excelSheet);
			
			// 封装父子结构的id

			responseModel.setSuccessful(true);
			responseModel.setMessage("操作成功");
			responseModel.setCode(ResultCode.SUCCESS);
		} catch (Exception e) {
			/* Logger.info("读取excel文件失败", e); */
			e.printStackTrace();
			if (e instanceof DateTimeParseException) {
				responseModel.setSuccessful(false);
				responseModel.setMessage("日期格式错误,操作失败");
				responseModel.setCode(ResultCode.ERROR);
			} else {
				responseModel.setSuccessful(false);
				responseModel.setMessage("操作失败");
				responseModel.setCode(ResultCode.ERROR);
			}

		}

		return responseModel;
	}
	
	@RequestMapping("/energy/queryRelationMeterList")
		public @ResponseBody EasyUIJson queryRelationMeterList(MeterRelation meterRelation, 
			@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="15")Integer rows) {
			EasyUIJson uiJson = meterService.relationMeterList(rows, page, meterRelation);                                                                                                          
		return uiJson;
	}
}

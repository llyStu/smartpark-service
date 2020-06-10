package com.vibe.service.device;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.poiutil.ExportDevice;
import com.vibe.poiutil.ExportExcel;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.user.Department;
import com.vibe.pojo.user.User;
import com.vibe.service.asset.AssetService;
import com.vibe.service.dept.DeptService;
import com.vibe.service.user.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vibe.pojo.Catalog;
import com.vibe.pojo.FiledDesc;
@Service
public class DeviceServiceImpl implements DeviceService{
	@Autowired
	private AssetService assetService;

	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;

	@Override
	public Map<String, Object> getPreparData(Integer id) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object>  result = getToAddPage(map);
		if(id != null){
			map = getToEditorOrDetail(result);
		}
		List<Catalog> catalogs= getCatalogTable();
		map.put("catalogs", catalogs);
		
		return map;
	}
	//获取分类点表的数据
	private List<Catalog> getCatalogTable() {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String, Object> getToEditorOrDetail(Map<String, Object> result) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> getToAddPage(Map<String, Object> map) {
		List<FiledDesc> feild = getEveryFeild();
		map.put("feild", feild);
		return null;
	}
	public List<FiledDesc> getEveryFeild() {
		
		return null;
	}
	public List<AssetVo> getDevices(String ids){
			List<AssetVo> devices = new ArrayList<AssetVo>();
			if (ids != null && ids != "") {
				// 用，号切成数组
				String[] ides = ids.split(",");
				// 遍历数组
				List<Integer> deviceIds = new ArrayList<Integer>();
				for (String id : ides) {
					deviceIds.add(Integer.parseInt(id));
				}
				devices = assetService.findDeviceByIds(deviceIds);
			} else {
				devices = assetService.findAllDevice();
			}
			return devices;
	}

	public  List<ExportDeviceAll> getDataset(List<AssetVo> devices) throws AssetException {
		List<ExportDeviceAll> dataset = new ArrayList<ExportDeviceAll>();
		if (devices != null) {
			for (AssetVo device : devices) {
				/**
				 * {"","","","","资产类别", "增加方式", "制造工厂","国标编码", "详细配置"
				 ,"制造日期", "使用部门","使用状态","设备类型", "存放地点", "保管人员","数量", "单位","单价", "金额"
				 , "购买时间", "启用时间","维修间隔月", "原值","使用年限", "残值率","残值", "折旧方法","备注"};
				 */
				ExportDeviceAll expDevice = new ExportDeviceAll();
				expDevice.setNumber(device.getId()); //设备编号
				expDevice.setDeviceTitle( device.getCaption()); //设备标题
				expDevice.setDeviceName(device.getName()); //设备名称
//				expDevice.setCaption(space.getName());
				expDevice.setSpecification(device.getSpecification()); //规格型号
				expDevice.setModels(device.getModels()); //资产类别
				expDevice.setIncrese_way(device.getIncrese_way()); //增加方式
				expDevice.setVendor(device.getVendor()); //制造工厂
				expDevice.setInternational_code(device.getInternational_code()); //国标编码
				expDevice.setDetail_config(device.getDetail_config()); //详细配置
				expDevice.setProduct_date(device.getProduction_date()); //制造日期
				expDevice.setUsing_state(device.getIs_using() == null || device.getIs_using() == 1 ? "正常使用":"存放状态");//使用状态
				expDevice.setDevice_type(device.getDevice_type()); //设备类型
				expDevice.setQuantity(device.getQuantity()); //数量
				expDevice.setDeviceUnit(device.getDeviceUnit());  //单位
				expDevice.setPrice(device.getPrice()); //价格
				expDevice.setAmount(device.getAmount());  //金额
				expDevice.setPurchase_date(device.getPurchase_date()); //采购日期
				expDevice.setEnabing_date(device.getEnabing_date()); //启用时间
				expDevice.setMaintenance_interval(device.getMaintenance_interval()); //维修间隔月
				expDevice.setOriginal_value(device.getOriginal_value()); //原值
				expDevice.setUse_year(device.getUse_year()); //使用年限
				expDevice.setSalvage(device.getSalvage()); //残值率
				expDevice.setSalvage_value(device.getSalvage_value()); //残值
				expDevice.setDepreciation_method(device.getDepreciation_method()); //折旧方法
				expDevice.setDesc(device.getMemo()); //备注
				Integer did = device.getDepartId();
				String department = "";
				if(null != did){
					Department dept = deptService.queryDeptById(did);
					department = dept.getName();
				}
				expDevice.setUsing_department(department);
				Integer localtionId = device.getLocation();
				String space = "";
				if(null != localtionId) {
					Asset root = assetService.findAssetByID(localtionId, AssetKind.SPACE.toString());
					space = root.getCaption();
				}
				expDevice.setLocation(space); //存放地点
				Integer uid = device.getUserId();
				String userName = "";
				if(null != localtionId) {
					User user = userService.queryUserById(uid);
					userName = user.getName();
				}
				expDevice.setKeepers(userName); //保管人员
				dataset.add(expDevice);
			}
		}
		return dataset;
	}
	public ByteArrayOutputStream getOutputStream(String ids) throws Exception {
		List<AssetVo> devices = getDevices(ids);
		List<ExportDeviceAll> dataset = getDataset(devices);
	/* 生成xlsx 直接写到响应体下载 */
		ExportExcel<ExportDeviceAll> ex = new ExportExcel<ExportDeviceAll>();
		String[] headers = {"设备编号","设备标题","设备名称","规格型号","资产类别", "增加方式", "制造工厂","国标编码", "详细配置"
				,"制造日期", "使用部门","使用状态","设备类型", "存放地点", "保管人员","数量", "单位","单价", "金额"
				, "购买时间", "启用时间","维修间隔月", "原值","使用年限", "残值率","残值", "折旧方法","备注"};
		HSSFWorkbook workbook = ex.exportExcel("设备", headers, dataset);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		return out;
	}
	@Override
	public ResponseEntity<byte[]> printDevice(String ids) throws Exception {
		ByteArrayOutputStream out = getOutputStream(ids);
		HttpHeaders headers1 = new HttpHeaders();
		//headers1.setContentDispositionFormData("attachment", "file.xls");
		headers1.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.xls\"");
		headers1.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		ResponseEntity<byte[]> filebyte = new ResponseEntity<byte[]>(out.toByteArray(), headers1, HttpStatus.OK);
		out.close();
		return filebyte;
	}
}

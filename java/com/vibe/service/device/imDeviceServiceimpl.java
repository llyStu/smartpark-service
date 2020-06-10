package com.vibe.service.device;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vibe.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.mapper.dept.DeptMapper;
import com.vibe.mapper.monitor.AssetRepeatMapper;
import com.vibe.mapper.user.UserMapper;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.user.Department;
import com.vibe.pojo.user.User;
import com.vibe.pojo.user.UserVo;
import com.vibe.service.device.ImDeviceService;
import com.vibe.service.statistics.MonitorDataService;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.AssetState;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Space;
import com.vibe.monitor.asset.type.DeviceType;
import com.vibe.monitor.asset.type.SpaceType;
import com.vibe.poiutil.POIUtil;

@Service
public class imDeviceServiceimpl implements ImDeviceService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private AssetRepeatMapper assetMapper;
	@Autowired
	private MonitorDataService monitorDataService;
	
	private Map<String, User> userMap = new HashMap<String, User>();
	private Map<String, Department> deptMap = new HashMap<String, Department>();
	private Map<String, Space> spaceMap = new HashMap<String, Space>();

	@Override
	public List<User> loadUser() {
		return userMapper.queryUserList(new UserVo());
	}

	@Override
	public List<Department> loadDept() {
		return deptMapper.queryDeptList(null);
	}

	@Override
	public List<AssetVo> loadSpace(AssetVo assetVo) {
		 List<AssetVo> list=assetMapper.findSpace(assetVo);
		 return list;
	}

	@Override
	public void saveDepttoDB(Department dept) {
	     deptMapper.saveDepttoDB(dept);
		
	}

	@Override
	public void saveUsertoDB(User user) {
		userMapper.addUser(user);
		
	}

	@Override
	public void saveSpacetoDB(Space space2) {
		assetMapper.addSpace(space2);
	}

	@Override
	public void saveDevice(Device device) {
		assetMapper.addDevice(device);
		assetMapper.addDeviceDetail(device);
		
	}
	@Autowired
	private AssetStore assetStore;
	@Autowired
	private Application application;
	@Override
	public void saveDeviceList(Workbook workbook) throws AssetException {
		// TODO Auto-generated method stub

		IdGenerator<Integer> assetId = application.getIntIdGenerator("asset");
		Sheet sheet = workbook.getSheetAt(0);
		// 获得当前sheet的开始行
		int firstRowNum = sheet.getFirstRowNum();
		// 获得当前sheet的结束行
		int lastRowNum = sheet.getLastRowNum();

		List<Device> deviceList = new ArrayList<>();
		for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
			// 获得当前行
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				break;
			}
			Device device = new Device();
			// 获得当前行的开始列
			Cell cell1 = row.getCell(0);
			String name = POIUtil.getCellValue(cell1);

			Cell cell2 = row.getCell(1);
			String caption = POIUtil.getCellValue(cell2);
			device.setCaption(caption);

			Cell cell3 = row.getCell(2);
			String specification = POIUtil.getCellValue(cell3);
			device.setSpecification(specification);
			// 资产类型
			Cell cell4 = row.getCell(3);
			String models = POIUtil.getCellValue(cell4);
			device.setModels(models);

			Cell cell5 = row.getCell(4);
			String increse_way = POIUtil.getCellValue(cell5);
			device.setIncrese_way(increse_way);
			;

			Cell cell6 = row.getCell(5);
			String vendor = POIUtil.getCellValue(cell6);
			device.setVendor(vendor);
			;

			Cell cell7 = row.getCell(6);
			String international_code = POIUtil.getCellValue(cell7);
			device.setInternational_code(international_code);
			;

			Cell cell8 = row.getCell(7);
			String detail_config = POIUtil.getCellValue(cell8);
			device.setDetail_config(detail_config);
			;

			Cell cell9 = row.getCell(8);
			String production_date = POIUtil.getCellValue(cell9);
			if (StringUtils.isNotBlank(production_date) && !StringUtils.equals(production_date,"")){
				device.setProduction_date(LocalDate.parse(production_date));
			}
			// 使用部门
			Cell cell10 = row.getCell(9);
			String using_department = POIUtil.getCellValue(cell10);
			Department dept = getDept(using_department);
			if (null != dept){
				device.setUsing_department(dept.getId());// 需要改成对象
			}
			// 使用状态
			Cell cell11 = row.getCell(10);
			String is_using = POIUtil.getCellValue(cell11);
			if (is_using != null && is_using.equals("正常使用")) {
				device.setIs_using(1);
			} else {
				device.setIs_using(0);
			}

			// 设备类型
			Cell cell12 = row.getCell(11);
			String device_type = POIUtil.getCellValue(cell12);
			device.setDevice_type(device_type);
			// 存放地点
			Cell cell13 = row.getCell(12);
			String location = POIUtil.getCellValue(cell13);
			if (location != null && location.equals("")) {
				location = "其他";
			}
			Space space = getSpace(location, assetId);
			if (null != space){
				device.setParentId(space.getId());
				device.setLocation(space.getId());// 可以改成space，目前是死的
			}
			// 保管人员
			Cell cell14 = row.getCell(13);
			String keepers = POIUtil.getCellValue(cell14);
			User user = getUser(keepers);
			if (null != user){
				device.setKeepers(user.getId());// 需要改成对象
			}
			// 数量
			Cell cell15 = row.getCell(14);
			String quantity = POIUtil.getCellValue(cell15);
			if (StringUtils.isNotBlank(quantity) && !StringUtils.equals("",quantity)){
//				space.setPrice(Double.valueOf(quantity));
				device.setQuantity(Integer.valueOf(quantity));
			}
			// 单位
			Cell cell16 = row.getCell(15);
			String deviceUnit = POIUtil.getCellValue(cell16);
			device.setDeviceUnit(deviceUnit);
			;
			// 单价
			Cell cell17 = row.getCell(16);
			String price = POIUtil.getCellValue(cell17);
			if (StringUtils.isNotBlank(price) && !StringUtils.equals("",price)){
				device.setPrice(Double.valueOf(price));
			}
			;
			// 金额
			Cell cell18 = row.getCell(17);
			String amount = POIUtil.getCellValue(cell18);
			if (StringUtils.isNotBlank(amount) && !StringUtils.equals("",amount)){
				device.setAmount(Double.valueOf(amount));
			}
			;

			// 购买时间,日期和时间
			Cell cell19 = row.getCell(18);
			String purchaseDate = POIUtil.getCellValue(cell19);
			if (StringUtils.isNotBlank(purchaseDate) && !StringUtils.equals("",purchaseDate)){
				device.setPurchaseDate(
						LocalDateTime.parse(purchaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
			// 启用时间,日期和时间
			Cell cell20 = row.getCell(19);
			String enabing_date = POIUtil.getCellValue(cell20);
			if (StringUtils.isNotBlank(enabing_date) && !StringUtils.equals("",enabing_date)){
				LocalDate date = LocalDate.parse(enabing_date);
				device.setEnabing_date(date);
			}
			// 维修间隔月
			Cell cell21 = row.getCell(20);
			String interval = POIUtil.getCellValue(cell21);
			if (StringUtils.isNotBlank(interval) && !StringUtils.equals("",interval)){
				device.setMaintenance_interval(Integer.valueOf(interval));
			}

			// 原值
			Cell cell22 = row.getCell(21);
			String original_value = POIUtil.getCellValue(cell22);
			if (StringUtils.isNotBlank(original_value) && !StringUtils.equals("",original_value)){
				device.setOriginal_value(Integer.valueOf(original_value));
			}
			// 使用年限
			Cell cell23 = row.getCell(22);
			String use_year = POIUtil.getCellValue(cell23);
			if (StringUtils.isNotBlank(use_year) && !StringUtils.equals("",use_year)){
				device.setUse_year(Integer.valueOf(use_year));
			}
			// 残值率
			Cell cell24 = row.getCell(23);
			String salvage = POIUtil.getCellValue(cell24);
			if (StringUtils.isNotBlank(salvage) && !StringUtils.equals("",salvage)){
				device.setSalvage(Double.valueOf(salvage));
			}
			// 残值
			Cell cell25 = row.getCell(24);
			String salvageValue = POIUtil.getCellValue(cell25);
			if (StringUtils.isNotBlank(salvageValue) && !StringUtils.equals("",salvageValue)){
				device.setSalvage_value(Double.valueOf(salvageValue));
			}
			// 折旧方法
			Cell cell26 = row.getCell(25);
			String depreciation_method = POIUtil.getCellValue(cell26);
			device.setDepreciation_method(depreciation_method);
			// 备注
			Cell cell27 = row.getCell(26);
			String desc = POIUtil.getCellValue(cell27);
			device.setDesc(desc);

			DeviceType type = assetStore.getDeviceTypes().find("GenericDevice");
			device.setType(type);
			Integer deviceId = assetId.next();

			device.setName(name + "(" + deviceId+")");
			device.setShow_in_client(2);
			device.loadState(AssetState.NORMAL);
			device.setId(deviceId);
			System.out.println("device的id" + deviceId);
			deviceList.add(device);
			saveDevice(device);
			System.out.println("存入device，第" + rowNum + "个");
		}

		for (Device item : deviceList) {
			assetStore.addAsset(item.getParentId(), item);
		}
		/*
		 * if(deviceList != null){ for (Device space : deviceList) {
		 * if(space !=null){ imDeviceService.saveDevice(space); } } }
		 */
	
	}
	

	public Space getSpace(String name, IdGenerator<Integer> assetId) throws AssetException {
		setSpaceMap();
		Space space = spaceMap.get(name);
		if (space != null) {
			return space;
		} else {
			Space space2 = new Space();
			SpaceType type = assetStore.getSpaceTypes().find("3DSpace");
			space2.setType(type);
			space2.setId(assetId.next());
			Integer titleID = monitorDataService.getTitleID(0);
			space2.setParentId(titleID);
			space2.setName(name);
			space2.setCaption(name);
			System.out.println("space的id" + space2.getId());
			saveSpacetoDB(space2);
			assetStore.addAsset(space2.getParentId(), space2);
			return space2;
		}
	}

	public User getUser(String name) {
		IdGenerator<Integer> userId = application.getIntIdGenerator("user");
		setUserMap();
		User user = userMap.get(name);
		if (user != null) {
			return user;
		} else {
			User user2 = new User();
			user2.setId(userId.next());
			user2.setName(name);
			user2.setLogin_id(name);
			user2.setPassword("123456");
			System.out.println("userId" + user2.getId());
			saveUsertoDB(user2);
			return user2;
		}
	}

	public Department getDept(String name) {
		IdGenerator<Integer> userId = application.getIntIdGenerator("user");
		setDeptMap();
		Department department = deptMap.get(name);
		if (department != null) {
			return department;
		} else {
			Department dept = new Department();
			dept.setId(userId.next());
			dept.setName(name);
			dept.setParent(1);
			// 存到数据库返回
			System.out.println("deptId ==" + dept.getId());
			saveDepttoDB(dept);
			return dept;
		}

	}

	public void setDeptMap() {
		List<Department> loadDept = loadDept();
		if (loadDept != null && loadDept.size() > 0) {
			for (Department dept : loadDept) {
				this.deptMap.put(dept.getName(), dept);
			}
		}

	}

	public void setUserMap() {
		List<User> loadUser = loadUser();
		if (loadUser != null && loadUser.size() > 0) {
			for (User user : loadUser) {
				this.userMap.put(user.getName(), user);
			}
		}
	}

	public void setSpaceMap() {
		short siteId = application.getSiteId();
		AssetVo vo = new AssetVo();
		vo.setSiteId((int) siteId);
		List<AssetVo> loadSpace = loadSpace(vo);
		if (loadSpace != null && loadSpace.size() > 0) {
			for (AssetVo assetVo : loadSpace) {
				Space space = new Space();
				space.setId(assetVo.getId());
				space.setCaption(assetVo.getCaption());
				space.setDesc(assetVo.getMemo());
				space.setName(assetVo.getName());
				space.setParentId(assetVo.getParentId());
				this.spaceMap.put(space.getCaption(), space);
			}
		}

	}
	
	

}

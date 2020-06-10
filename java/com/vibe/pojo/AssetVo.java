package com.vibe.pojo;

import java.util.List;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import com.vibe.monitor.asset.AssetProperty;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.pojo.user.Department;
import com.vibe.pojo.user.User;
import com.vibe.util.RegixCut;

public class AssetVo implements Comparable<AssetVo> {

	public AssetVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * 资产的公共属性
	 * 
	 */
	
	public AssetVo(Integer id, int seqence) {
		super();
		this.id = id;
		this.seqence = seqence;
	}
	public AssetVo(Integer id) {
		super();
		this.id = id;
	}
	private String fullName;
	private String valueStr;
	private String kindStr;
	private Integer siteId;
	@NumberFormat(style=Style.NUMBER)
	private Integer id;//资产编号
	private String typeName;
	@NumberFormat(style=Style.NUMBER)
	private Integer parentId;//父资产编号
	private String parentCaption;
	private String name;//资产的名称
	private String caption;//显示名称
	private String memo;//描述
	private String state;//状态
	private String error;//错误信息
	@NumberFormat(style=Style.NUMBER)
	private Integer enabled;
	private String typeCaption;
	private CompoundAsset<?> parent;//父
	private String kind;//分类
	private Integer removed;//是否删除
	private List<AssetProperty> valueList ;//asset的属性
	/*
	 *moniter的属性 
	 */
	private String value;//监测器     值
	private String serviceCaption;//服务    名称
	private String time_interval;//监测器   间隔时间
	private String save_interval;//监测器   间隔时间
	private String unit;//单位
	private String time_unit;
	@NumberFormat(style=Style.NUMBER)
	private Integer refresh_delay;
	private String warn_cond;// 监测器    警告表达式
	private String transform;//结果变换
	@NumberFormat(style=Style.NUMBER)
	private Integer source;//服务id
	@NumberFormat(style=Style.NUMBER)
	private Integer catalogId;
	private String minValue;//监测器     值
	private String maxValue;
	/*
	 * 设备的属性
	 */
	private String vendor;//设备 生产厂家
	private String purchase_date;//设备   采购日期
	private String warranty_date;// 设备 保修日期
	private String specification;//规格型号
	private String models;//资产类型
	private String increse_way;//增加方式
	private String international_code;//国际编码
	private String detail_config;//详情配置
	private String production_date;//制造日期
	private Department using_department;//使用部门
	@NumberFormat(style=Style.NUMBER)
	private Integer departId;
	
	
	private String using_state;//使用状态
	private String device_type;//设备类型
	@NumberFormat(style=Style.NUMBER)
	private Integer location;//存放地点 spaceId
	private String spaceCaption;
	private User keepers;//保管人员
	private String userName;
	@NumberFormat(style=Style.NUMBER)
	private Integer userId;
	@NumberFormat(style=Style.NUMBER)
	private Integer quantity;//数量
	private String deviceUnit;//单位
	@NumberFormat(style=Style.CURRENCY)
	private Double price;//单价
	@NumberFormat(style=Style.CURRENCY)
	private Double amount;//金额
	private String enabing_date;//启用时间
	@NumberFormat(style=Style.NUMBER)
	private Integer maintenance_interval;//维修间隔月
	@NumberFormat(style=Style.NUMBER)
	private Integer original_value;//原值
	@NumberFormat(style=Style.NUMBER)
	private Integer use_year;//使用年限
	@NumberFormat(style=Style.PERCENT)
	private Double  salvage;//残值率
	@NumberFormat(style=Style.CURRENCY)
	private Double salvage_value;//残值
	private String depreciation_method;//折旧方法
	private String maintenance_time;//维修时间
	private String maintenance_people;//维修人
	private Integer scrap;//是否报废标志 0-报废 非0－没报废
	private Integer is_using;//是否使用标志 0-没使用 非0－使用
	private String qrcode;
	
	//摄像机的属性
	private String username;
	private String password;
	private String host;
	@NumberFormat(style=Style.NUMBER)
	private int port;
	private String rtspUrlPattern;
	private String huifangurl;
	@NumberFormat(style=Style.NUMBER)
	private int spaceId;
	
	private String monitorType;


	public String getSave_interval() {
		return save_interval;
	}

	public void setSave_interval(String save_interval) {
		this.save_interval = save_interval;
	}
	
	public String getHuifangurl() {
		return huifangurl;
	}

	public void setHuifangurl(String huifangurl) {
		this.huifangurl = huifangurl;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}
	private int seqence;
	
	
	public int getSeqence() {
		return seqence;
	}

	public void setSeqence(int seqence) {
		this.seqence = seqence;
	}

	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

	public String getKindStr() {
		return kindStr;
	}

	public void setKindStr(String kindStr) {
		this.kindStr = kindStr;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getRtspUrlPattern() {
		return rtspUrlPattern;
	}
	
	public void setRtspUrlPattern(String rtspUrlPattern) {
		this.rtspUrlPattern = rtspUrlPattern;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getSpaceCaption() {
		return spaceCaption;
	}
	public void setSpaceCaption(String spaceCaption) {
		this.spaceCaption = spaceCaption;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getLocation() {
		return location;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	/*@Override
	public String toString() {
		return "AssetVo [id=" + id + ", typeName=" + typeName + ", parentId=" + parentId + ", name=" + name
				+ ", caption=" + caption + ", memo=" + memo + ", value=" + value + ", state=" + state
				+ ", serviceCaption=" + serviceCaption + ", time_interval=" + time_interval + ", unit=" + unit
				+ ", time_unit=" + time_unit + ", refresh_delay=" + refresh_delay + ", warn_cond=" + warn_cond
				+ ", vendor=" + vendor + ", purchase_date=" + purchase_date + ", warranty_date=" + warranty_date
				+ ", parent=" + parent + ", kind=" + kind + ", source=" + source + ", valueList=" + valueList + "]";
	}*/
	public Integer getDepartId() {
		return departId;
	}
	public String getUserName() {
		if(keepers != null){
			userName=keepers.getName();
		}
		
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setDepartId(Integer departId) {
		this.departId = departId;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getModels() {
		return models;
	}
	public void setModels(String models) {
		this.models = models;
	}
	public String getIncrese_way() {
		return increse_way;
	}
	public void setIncrese_way(String increse_way) {
		this.increse_way = increse_way;
	}
	public String getInternational_code() {
		return international_code;
	}
	public void setInternational_code(String international_code) {
		this.international_code = international_code;
	}
	public String getDetail_config() {
		return detail_config;
	}
	public void setDetail_config(String detail_config) {
		this.detail_config = detail_config;
	}
	public String getProduction_date() {
		return production_date;
	}
	public void setProduction_date(String production_date) {
		this.production_date = production_date;
	}
	
	public void setUsing_state(String using_state) {
		this.using_state = using_state;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public Department getUsing_department() {
		return using_department;
	}

	public void setUsing_department(Department using_department) {
		this.using_department = using_department;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public User getKeepers() {
		return keepers;
	}

	public void setKeepers(User keepers) {
		this.keepers = keepers;
	}

	public String getUsing_state() {
		return using_state;
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getDeviceUnit() {
		return deviceUnit;
	}
	public void setDeviceUnit(String deviceUnit) {
		this.deviceUnit = deviceUnit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getEnabing_date() {
		return enabing_date;
	}

	public void setEnabing_date(String enabing_date) {
		this.enabing_date = enabing_date;
	}

	public Integer getMaintenance_interval() {
		return maintenance_interval;
	}
	public void setMaintenance_interval(Integer maintenance_interval) {
		this.maintenance_interval = maintenance_interval;
	}
	public Integer getOriginal_value() {
		return original_value;
	}
	public void setOriginal_value(Integer original_value) {
		this.original_value = original_value;
	}
	public Integer getUse_year() {
		return use_year;
	}
	public void setUse_year(Integer use_year) {
		this.use_year = use_year;
	}
	public Double getSalvage() {
		return salvage;
	}
	public void setSalvage(Double salvage) {
		this.salvage = salvage;
	}
	public Double getSalvage_value() {
		return salvage_value;
	}
	public void setSalvage_value(Double salvage_value) {
		this.salvage_value = salvage_value;
	}
	public String getDepreciation_method() {
		return depreciation_method;
	}
	public void setDepreciation_method(String depreciation_method) {
		this.depreciation_method = depreciation_method;
	}
	public String getMaintenance_time() {
		return maintenance_time;
	}
	public void setMaintenance_time(String maintenance_time) {
		this.maintenance_time = maintenance_time;
	}
	public String getMaintenance_people() {
		return maintenance_people;
	}
	public void setMaintenance_people(String maintenance_people) {
		this.maintenance_people = maintenance_people;
	}
	public Integer getScrap() {
		return scrap;
	}
	public void setScrap(Integer scrap) {
		this.scrap = scrap;
	}
	public Integer getIs_using() {
		return is_using;
	}
	public void setIs_using(Integer is_using) {
		this.is_using = is_using;
	}
	
	public String getParentCaption() {
		return parentCaption;
	}
	public void setParentCaption(String parentCaption) {
		this.parentCaption = parentCaption;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public String getTypeCaption() {
		return typeCaption;
	}
	public void setTypeCaption(String typeCaption) {
		this.typeCaption = typeCaption;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTime_unit() {
		return time_unit;
	}
	public void setTime_unit(String time_unit) {
		this.time_unit = time_unit;
	}
	public Integer getRefresh_delay() {
		return refresh_delay;
	}
	public void setRefresh_delay(Integer refresh_delay) {
		this.refresh_delay = refresh_delay;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTransform() {
		return transform;
	}
	public void setTransform(String transform) {
		this.transform = transform;
	}
	public Integer getRemoved() {
		return removed;
	}
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getServiceCaption() {
		return serviceCaption;
	}
	public void setServiceCaption(String serviceCaption) {
		this.serviceCaption = serviceCaption;
	}
	public String getDetact_interval() {
		if(time_interval == null){
			return "";
		}
		List<String> list=RegixCut.regixCutToList(time_interval);
		String number="";
		String unit="";
	        if(list != null && list.size()>1){
	        	 number=list.get(0);
	        	 unit = list.get(1);
	        	if(unit.equals("分")){
	        		unit="m";
	        	}else if(unit.equals("秒")){
	        		unit="s";
	        	}
	        }
		return number+unit;
	}
	public String getTime_interval(){
		return time_interval;
	}
	public void setTime_interval(String time_interval) {
		this.time_interval = time_interval;
	}
	public String getWarn_cond() {
		return warn_cond;
	}
	public void setWarn_cond(String warn_cond) {
		this.warn_cond = warn_cond;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getPurchase_date() {
		return purchase_date;
	}
	public void setPurchase_date(String purchase_date) {
		this.purchase_date = purchase_date;
	}
	public String getWarranty_date() {
		return warranty_date;
	}
	public void setWarranty_date(String warranty_date) {
		this.warranty_date = warranty_date;
	}
	public CompoundAsset<?> getParent() {
		return parent;
	}
	public void setParent(CompoundAsset<?> parent) {
		this.parent = parent;
	}
	

	public List<AssetProperty> getValueList() {
		return valueList;
	}
	public void setValueList(List<AssetProperty> valueList) {
		this.valueList = valueList;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	@Override
	public int compareTo(AssetVo o) {
       return this.getSeqence()-o.getSeqence();
	}

}

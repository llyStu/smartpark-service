package com.vibe.service.device;

import org.springframework.format.annotation.NumberFormat;

public class ExportDeviceAll {


	/**
	 * "", "","","", "", "","", ""
	 ,"", "","","", "", "","", "","", ""
	 , "购买时间", "","", "","", "","", "",""
	 */
	@NumberFormat(style= NumberFormat.Style.NUMBER)
	private Integer number;// 设备编号
	private String deviceTitle;//设备标题
	private String deviceName;
//	private String caption; //设备名称
	private String specification;//规格型号
	private String models;//资产类型
	private String increse_way;//增加方式

	public String getDeviceTitle() {
		return deviceTitle;
	}

	public void setDeviceTitle(String deviceTitle) {
		this.deviceTitle = deviceTitle;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Integer getNumber() {

		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	private String vendor;//设备 生产厂家
	private String international_code;//国际编码
	private String detail_config;//详情配置
	private String product_date;// 生产制造日期
	private String using_department;//使用部门
	private String using_state;//使用状态
	private String device_type;//设备类型
	@NumberFormat(style= NumberFormat.Style.NUMBER)
	private String location;//存放地点
	private String keepers;//保管人员
	@NumberFormat(style= NumberFormat.Style.NUMBER)
	private Integer quantity;//数量
	private String deviceUnit;//单位
	@NumberFormat(style= NumberFormat.Style.CURRENCY)
	private Double price;//单价
	@NumberFormat(style= NumberFormat.Style.CURRENCY)
	private Double amount;//金额
	private String purchase_date;//设备   采购日期
	private String enabing_date;//启用时间
	@NumberFormat(style= NumberFormat.Style.NUMBER)
	private Integer maintenance_interval;//维修间隔月

	/*public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}*/

	@NumberFormat(style= NumberFormat.Style.NUMBER)

	private Integer original_value;//原值
	@NumberFormat(style= NumberFormat.Style.NUMBER)
	private Integer use_year;//使用年限
	@NumberFormat(style= NumberFormat.Style.PERCENT)
	private Double  salvage;//残值率
	@NumberFormat(style= NumberFormat.Style.CURRENCY)
	private Double salvage_value;//残值
	private String depreciation_method;//折旧方法
	private String desc;//描述
/*
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}*/

	/*public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}*/

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

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
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

	public String getProduct_date() {
		return product_date;
	}

	public void setProduct_date(String product_date) {
		this.product_date = product_date;
	}

	public String getUsing_department() {
		return using_department;
	}

	public void setUsing_department(String using_department) {
		this.using_department = using_department;
	}

	public String getUsing_state() {
		return using_state;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getKeepers() {
		return keepers;
	}

	public void setKeepers(String keepers) {
		this.keepers = keepers;
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

	public String getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(String purchase_date) {
		if(null != purchase_date && purchase_date.endsWith(".0")){
			purchase_date = purchase_date.substring(0,purchase_date.length()-2);
		}
		this.purchase_date = purchase_date;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}

package com.vibe.pojo;

import java.io.Serializable;

public class AssetName implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1574241701755720122L;

    public AssetName() {
        super();
        // TODO Auto-generated constructor stub
    }

    /*
     * 资产的公共属性
     */
    private String cid;//资产编号
    private String cparent;//父类名
    private String cname;//资产的名称
    private String ccaption;//显示名称
    private String cmemo;//描述
    private String cstate;//状态
    private Integer cenabled;
    private String ctypeCaption;
    private String ckind;//分类


    /*
     *moniter的属性
     */
    private String cvalue;//监测器     值
    private String cservice;//服务    名称
    private String ctime_interval;//监测器   间隔时间

    private String crefresh_delay;//刷新延迟时间
    private String cwarn_cond;// 监测器    警告表达式
    private String ctransform;//结果变换
    /*
     * 设备的属性
     */
    private String cvendor;//设备 生产厂家
    private String cpurchase_date;//设备   采购日期
    private String cwarranty_date;// 设备 保修日期
    private String cspecification;//规格型号
    private String cmodels;//资产类型
    private String cincrese_way;//增加方式
    private String cinternational_code;//国际编码
    private String cdetail_config;//详情配置
    private String cproduction_date;//制造日期
    private String cusing_department;//使用部门
    private String cusing_state;//使用状态
    private String cdevice_type;//设备类型
    private String clocation;//存放地点
    private String ckeepers;//保管人员
    private String cquantity;//数量
    private String cdeviceUnit;//单位
    private String cprice;//单价
    private String camount;//金额
    private String cenabing_date;//启用时间
    private String cmaintenance_interval;//维修间隔月
    private String coriginal_value;//原值
    private String cuse_year;//使用年限
    private String csalvage;//残值率
    private String csalvage_value;//残值
    private String cdepreciation_method;//折旧方法
    private String cmaintenance_time;//维修时间
    private String cmaintenance_people;//维修人
    private String cscrap;//是否报废标志 0-报废 非0－没报废
    private String cis_using;//是否使用标志 0-没使用 非0－使用

    public final String getCid() {
        return cid;
    }

    public final void setCid(String cid) {
        this.cid = cid;
    }

    public final String getCparent() {
        return cparent;
    }

    public final void setCparent(String cparent) {
        this.cparent = cparent;
    }

    public final String getCname() {
        return cname;
    }

    public final void setCname(String cname) {
        this.cname = cname;
    }

    public final String getCcaption() {
        return ccaption;
    }

    public final void setCcaption(String ccaption) {
        this.ccaption = ccaption;
    }

    public final String getCmemo() {
        return cmemo;
    }

    public final void setCmemo(String cmemo) {
        this.cmemo = cmemo;
    }

    public final String getCstate() {
        return cstate;
    }

    public final void setCstate(String cstate) {
        this.cstate = cstate;
    }

    public final Integer getCenabled() {
        return cenabled;
    }

    public final void setCenabled(Integer cenabled) {
        this.cenabled = cenabled;
    }

    public final String getCtypeCaption() {
        return ctypeCaption;
    }

    public final void setCtypeCaption(String ctypeCaption) {
        this.ctypeCaption = ctypeCaption;
    }

    public final String getCkind() {
        return ckind;
    }

    public final void setCkind(String ckind) {
        this.ckind = ckind;
    }

    public final String getCvalue() {
        return cvalue;
    }

    public final void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    public final String getCservice() {
        return cservice;
    }

    public final void setCservice(String cservice) {
        this.cservice = cservice;
    }

    public final String getCtime_interval() {
        return ctime_interval;
    }

    public final void setCtime_interval(String ctime_interval) {
        this.ctime_interval = ctime_interval;
    }

    public final String getCrefresh_delay() {
        crefresh_delay = "刷新延迟时间";
        return crefresh_delay;
    }

    public final void setCrefresh_delay(String crefresh_delay) {
        this.crefresh_delay = crefresh_delay;
    }

    public final String getCwarn_cond() {
        cwarn_cond = "警告表达式";
        return cwarn_cond;
    }

    public final void setCwarn_cond(String cwarn_cond) {
        this.cwarn_cond = cwarn_cond;
    }

    public final String getCtransform() {
        ctransform = "结果变换";
        return ctransform;
    }

    public final void setCtransform(String ctransform) {
        this.ctransform = ctransform;
    }

    public final String getCvendor() {
        cvendor = "生产厂家";
        return cvendor;
    }

    public final void setCvendor(String cvendor) {
        this.cvendor = cvendor;
    }

    public final String getCpurchase_date() {
        cpurchase_date = "设备采购日期";
        return cpurchase_date;
    }

    public final void setCpurchase_date(String cpurchase_date) {
        this.cpurchase_date = cpurchase_date;
    }

    public final String getCwarranty_date() {
        cwarranty_date = "设备保修日期";
        return cwarranty_date;
    }

    public final void setCwarranty_date(String cwarranty_date) {
        this.cwarranty_date = cwarranty_date;
    }

    public final String getCspecification() {
        cspecification = "规格型号";
        return cspecification;
    }

    public final void setCspecification(String cspecification) {
        this.cspecification = cspecification;
    }

    public final String getCmodels() {
        cmodels = "资产类型";
        return cmodels;
    }

    public final void setCmodels(String cmodels) {
        this.cmodels = cmodels;
    }

    public final String getCincrese_way() {
        cincrese_way = "增加方式";
        return cincrese_way;
    }

    public final void setCincrese_way(String cincrese_way) {
        this.cincrese_way = cincrese_way;
    }

    public final String getCinternational_code() {
        cinternational_code = "国际编码";
        return cinternational_code;
    }

    public final void setCinternational_code(String cinternational_code) {
        this.cinternational_code = cinternational_code;
    }

    public final String getCdetail_config() {
        cdetail_config = "详情配置";
        return cdetail_config;
    }

    public final void setCdetail_config(String cdetail_config) {
        this.cdetail_config = cdetail_config;
    }

    public final String getCproduction_date() {
        cproduction_date = "使用日期";
        return cproduction_date;
    }

    public final void setCproduction_date(String cproduction_date) {
        this.cproduction_date = cproduction_date;
    }

    public final String getCusing_department() {
        cusing_department = "使用部门";
        return cusing_department;
    }

    public final void setCusing_department(String cusing_department) {
        this.cusing_department = cusing_department;
    }

    public final String getCusing_state() {
        cusing_state = "使用状态";
        return cusing_state;
    }

    public final void setCusing_state(String cusing_state) {
        this.cusing_state = cusing_state;
    }

    public final String getCdevice_type() {
        cdevice_type = "设备类型";
        return cdevice_type;
    }

    public final void setCdevice_type(String cdevice_type) {
        this.cdevice_type = cdevice_type;
    }

    public final String getClocation() {
        clocation = "存放地点";
        return clocation;
    }

    public final void setClocation(String clocation) {
        this.clocation = clocation;
    }

    public final String getCkeepers() {
        ckeepers = "保管人员";
        return ckeepers;
    }

    public final void setCkeepers(String ckeepers) {
        this.ckeepers = ckeepers;
    }

    public final String getCquantity() {
        cquantity = "数量";
        return cquantity;
    }

    public final void setCquantity(String cquantity) {
        this.cquantity = cquantity;
    }

    public final String getCdeviceUnit() {
        cdeviceUnit = "单位";
        return cdeviceUnit;
    }

    public final void setCdeviceUnit(String cdeviceUnit) {
        this.cdeviceUnit = cdeviceUnit;
    }

    public final String getCprice() {
        cprice = "单价";
        return cprice;
    }

    public final void setCprice(String cprice) {
        this.cprice = cprice;
    }

    public final String getCamount() {
        camount = "金额";
        return camount;
    }

    public final void setCamount(String camount) {
        this.camount = camount;
    }

    public final String getCenabing_date() {
        cenabing_date = "启用时间";
        return cenabing_date;
    }

    public final void setCenabing_date(String cenabing_date) {
        this.cenabing_date = cenabing_date;
    }

    public final String getCmaintenance_interval() {
        cmaintenance_interval = "维修间隔月";
        return cmaintenance_interval;
    }

    public final void setCmaintenance_interval(String cmaintenance_interval) {
        this.cmaintenance_interval = cmaintenance_interval;
    }

    public final String getCoriginal_value() {
        coriginal_value = "原值";
        return coriginal_value;
    }

    public final void setCoriginal_value(String coriginal_value) {
        this.coriginal_value = coriginal_value;
    }

    public final String getCuse_year() {
        cuse_year = "使用年限";
        return cuse_year;
    }

    public final void setCuse_year(String cuse_year) {
        this.cuse_year = cuse_year;
    }

    public final String getCsalvage() {
        csalvage = "残值率";
        return csalvage;
    }

    public final void setCsalvage(String csalvage) {
        this.csalvage = csalvage;
    }

    public final String getCsalvage_value() {
        csalvage_value = "残值";
        return csalvage_value;
    }

    public final void setCsalvage_value(String csalvage_value) {
        this.csalvage_value = csalvage_value;
    }

    public final String getCdepreciation_method() {
        cdepreciation_method = "维修方法";
        return cdepreciation_method;
    }

    public final void setCdepreciation_method(String cdepreciation_method) {
        this.cdepreciation_method = cdepreciation_method;
    }

    public final String getCmaintenance_time() {
        cmaintenance_time = "维修时间";
        return cmaintenance_time;
    }

    public final void setCmaintenance_time(String cmaintenance_time) {
        this.cmaintenance_time = cmaintenance_time;
    }

    public final String getCmaintenance_people() {
        cmaintenance_people = "维修人";
        return cmaintenance_people;
    }

    public final void setCmaintenance_people(String cmaintenance_people) {
        this.cmaintenance_people = cmaintenance_people;
    }

    public final String getCscrap() {
        cscrap = "是否报废";
        return cscrap;
    }

    public final void setCscrap(String cscrap) {
        this.cscrap = cscrap;
    }

    public final String getCis_using() {
        cis_using = "是否使用";
        return cis_using;
    }

    public final void setCis_using(String cis_using) {
        this.cis_using = cis_using;
    }


}

package com.vibe.mapper.monitor;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vibe.pojo.AssetVo;
import com.vibe.pojo.DeviceCatalogSpace;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetProperty;
import com.vibe.monitor.asset.Device;

import com.vibe.monitor.drivers.modbus.service.ModbusTcpMaster;


/**
 * 增删改资产的接口
 * @author FLex3
 *
 */

public interface AssetRepeatMapper {
	/*
	 * 实现编辑资源
	 * 方法名称：editProbeById
	 * 参数 类型:Probe
	 * 返回值类型: void
	 */
	public void editProbeById(Asset<?> asset);
	public void editDeviceById(Asset<?> asset);
	public void editDeviceDetailById(Asset<?> asset);
	public void editControlById(Asset<?> asset);
	public void editSpaceById(Asset<?> asset);
	public void editServiceById(Asset<?> asset);
	public void editAssetPropertyById(AssetProperty property);
	
	/*
	 *新增资源
	 *方法名称：addProbe
	 *参数 ：Probe
	 */
	public void addService(Asset<?> asset);
	public void addProbe(Asset<?> asset);
	public void addDevice(Asset<?> asset);
	public void addDeviceDetail(Asset<?> asset);
	public void addnewControl(Asset<?> asset);
	public void addSpace(Asset<?> asset);
	public void addAssetProperty(AssetProperty property);
	/*
	 * 根据类型查询服务列表
	 */
	public List<ModbusTcpMaster> findServiceByType(String name,int siteId);
	/*
	 *根据id查出服务的实例
	 */
	public Asset<?> findServiceById(Integer source);
	/*
	 *搜索的接口 
	 */
	public List<AssetVo> findService(AssetVo assetVo);
	public List<AssetVo> findProbe(AssetVo assetVo);
	public List<AssetVo> findDevice(AssetVo assetVo);
	public List<AssetVo> findControl(AssetVo assetVo);
	public List<AssetVo> findSpace(AssetVo assetVo);
	public AssetVo findDeviceDetail(int id);
	public Device findDeviceById(int parseInt);
	//查询导出打印二维码的设备
	public List<AssetVo> findDeviceByIds(List<Integer> ids);
	
	public List<Integer> findAssetGroup(@Param("codeCatalogId") int codeCatalogId,@Param("assetId") int assetId);
	public List<Integer> findDeviceLikeNameAndCaption(@Param("name")String name, @Param("caption")String caption);
	public List<Integer> findProbeLikeNameAndCaption(@Param("name")String name, @Param("caption")String caption);
	public String findProbeEnergyItemizeType(@Param("id")int id);
	public List<AssetVo> findDeviceByCatalogsAndSpaces(DeviceCatalogSpace dcs);
	}

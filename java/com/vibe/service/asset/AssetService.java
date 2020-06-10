package com.vibe.service.asset;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.vibe.monitor.asset.*;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.monitor.asset.type.AssetTypeProperty;
import com.vibe.parse.DeviceIdResult;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.DeviceCatalogSpace;
import com.vibe.utils.AllTreeNode;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.EasyUITreeNode;
import com.vibe.utils.TreeNode;

public interface AssetService {
	/**
	 * 打印资产树的递归方法
	 * @param root
	 * @return
	 */
	public AllTreeNode printAssetTree(final Asset<?> root,String flag,Integer catlog);
	
	public List<AllTreeNode> initAssetTree(String flag,Integer catlog);
	/**
	 * 资产列表数据
	 * @param rows
	 * @param page
	 * @param root
	 * @return
	 */
	public EasyUIJson assetList(Integer rows, Integer page, Asset<?> root,int workId);
	/**
	 * 资产详情
	 * @param root
	 * @param kind 
	 * @return
	 */
	public AssetVo assetDteail( Asset<?> root, String kind);
	/**
	 * 修改资产
	 * @param assetVo
	 * @param root
	 * @throws AssetException 
	 */
	public void assetEdit(AssetVo assetVo) throws AssetException;
	/**
	 * 修改页面显示此类型的服务列表
	 */
	public List<DeptJson> serviceList(AssetStore store,AssetType assetType);
	/**
	 * 保存数据库
	 * @param store
	 * @param alist
	 * @throws AssetException 
	 */
	public void addParentId(AssetStore store, List<Asset<?>> alist) throws AssetException;
	
	/**
	 * 修改数据
	 * @param asset
	 */
	public void editAsset(Asset<?> asset);
	/**
	 * 封装单个asset
	 * @param assetVo
	 * @param root
	 * @return
	 */
	public Asset<?> setAsset(AssetVo assetVo, Asset<?> root);
	/**
	 * 新增单个的资产
	 * @param setAsset
	 * @param valueList
	 * @throws AssetException 
	 */
	public void addAsset(AssetStore store, Asset<?> asset, List<AssetProperty> valueList) throws AssetException;
	//新增时封装属性，设置默认值
	public void setPropertyValue(Asset<?> asset, Object value, AssetTypeProperty property);
	/**
	 * 搜索资产信息，分页显示
	 * @param assetStore 
	 * @param assetVo
	 * @param rows
	 * @param page
	 * @return
	 */
	public EasyUIJson findAssetList(AssetStore assetStore, AssetVo assetVo, Integer rows, Integer page);
	public void deleteAsset(Asset<?> asset);
	
	/**
	 * 移动端，扫描二维码加载设备详情
	 * @param vo
	 * @return
	 */
	public AssetVo findDevice(AssetVo vo);
	/**
	 * 查询是否生成二维码设备的信息
	 * @param assetVo
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> queryQrcodeListByPage(AssetVo assetVo, Integer page, Integer rows);
	/**
	 * 生成二维码
	 * @param parseInt
	 * @param name 
	 * @param realPath 
	 * @param savePath 
	 * @throws IOException 
	 * @throws Exception 
	 */
	public void addDeviceQrcode( String id,String savePath, String realPath) throws IOException, Exception;
	public Asset<?> findAssetByID(Integer id,String kind) throws AssetException;
	public List<AssetVo> findDeviceByIds(List<Integer> ids);
	/**
	 * 查询所有设备信息
	 * @return
	 */
	public List<AssetVo> findAllDevice();
	/**
	 * 根据parentId 查询子节点的列表
	 * @param parent
	 * @return
	 */
	public List<EasyUITreeNode> querySpaceTreeData(Integer parent);
	public void addAssetToDB(Asset<?> asset);
	public List<AssetVo> findSpaceByParentId(Integer parentId);
	public AssetVo findProbeByPanrentAndCatalog(AssetVo assetVo);

	public List<TreeNode> initAssetAllTree(String flag, Integer locationRoot, Integer depth,Integer catlog);

	public List<TreeNode> getAllSpaceTree(int parent);

	void addAssetByKind(AssetVo assetVo) throws AssetException;

	public List<AssetProperty> getPropertyDefaultValue(String typeName, String kind);

	public List<AssetVo> findSpace(AssetVo assetVo);

	public DeviceIdResult getFirstDeviceIdByAssetId(Integer id);

	public List<AllTreeNode> selectAssetTree(Integer catlog);

	public List<Integer> findDeviceLikeNameAndCaption(String name, String caption);

	public List<Integer> findProbeLikeNameAndCaption(String name, String caption);

	public List<AssetVo> queryAssetAddEnergyDeviceType(Asset<?> root, int workId);

	public void potSpace(List<Asset<?>> alist, Map<String, Object> assets, Integer parentId) throws AssetException;

	public void potControl(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList,
			Integer parentId)throws AssetException;

	public void potDevice(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList,
			Integer parentId)throws AssetException;

	public void potProbe(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList,
			Integer parentId)throws AssetException;

	public void potService(List<Asset<?>> alist, Map<String, Object> assets, List<AssetProperty> proList,
			Integer parentId)throws AssetException;

	public EasyUIJson findDeviceList(AssetStore assetStore, DeviceCatalogSpace dcs, Integer rows, Integer page);


	public List<TreeNode> queryDeviceBySpaceId(Integer spaceId);
}

package com.vibe.controller.asset;

import com.vibe.monitor.asset.*;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.monitor.asset.type.MonitorType;
import com.vibe.monitor.drivers.generic.GenericDevice;
import com.vibe.monitor.drivers.iec104.probe.IEC104FloatProbe;
import com.vibe.parse.DeviceIdResult;
import com.vibe.poiutil.POIUtil;
import com.vibe.pojo.AssetVo;
import com.vibe.service.asset.AssetService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.util.StringUtils;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
public class AssetController {

	@Autowired
	private AssetService assetService;

	@Autowired
	private AssetStore assetStore;

	/*@Autowired
	private MonitorServer monitorServer;


	@RequestMapping("/inputFloatProbeData")
	public @ResponseBody Response inputFloatProbeData(@RequestParam("id") int id,@RequestParam("data") float data) {
		monitorServer.inputFloatProbeData(id,data);
		return ResponseResult.getANewResponse();
	}*/

	public AssetController() {
	}

	/*static class Data{
		private String id;
		private double wei;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public double getWei() {
			return wei;
		}
		public void setWei(double wei) {
			this.wei = wei;
		}


	}
	@RequestMapping("/weikai")
	public @ResponseBody List<Data> weikai() {
		List<Data> list = new ArrayList<Data>();
		Data data = new Data();
		data.setId("CEG11720008*");
		data.setWei(89.23);
		list.add(data);
		return list;
	}*/
	/*
	 * 加载资产tree
	 * flag是编辑返回tree的内容 ，device 只显示设备
	 */
	@RequestMapping("/asset/initAssetTree")
	public @ResponseBody List<AllTreeNode> initAssetTree(String flag,Integer catlog) {
		return assetService.initAssetTree(flag,catlog);
	}
	/*新页面的tree
	 * flag 标记 device 设备  space 空间  total 所有 sensor 摄像机
	 * locationRoot 位置的根Id  1 空间
	 * depth 树的层级 数
	 * */
	@RequestMapping("/asset/initAssetAllTree")
	public @ResponseBody List<TreeNode> initAssetAllTree(String flag,
														 Integer locationRoot,Integer depth,Integer catlog,HttpServletResponse response) {
		return assetService.initAssetAllTree(flag,locationRoot,depth,catlog);
	}
	/*
	 * 获取所有监测器树
	 *
	 */
	@RequestMapping("/asset/initProbeTree")
	public @ResponseBody List<AllTreeNode> initProbeTree(Integer catlog) {
		return assetService.selectAssetTree(catlog);
	}
	/*
	 * 获取所有摄像头设备接口
	 *
	 */
	@RequestMapping("/asset/camera")
	public @ResponseBody List<AllTreeNode> camera(String flag) {
		return assetService.initAssetTree(flag,null);
	}

	/**
	 * 请求：/asset/toAssetList/12 方法：toAssetList 参数：id 返回：String
	 */
	// 跳转到资产列表
	@RequestMapping("/asset/toAssetList/{id}")
	public String toUserList(@PathVariable Integer id, Model model) {
		model.addAttribute("id", id);
		return "/asset/assetList";
	}
	/*异步获取站点id
	@RequestMapping("/asset/getSiteId")
	public @ResponseBody String getSiteId(){
		int siteId = application.getSiteId() << 16;
		return siteId+"";
	}*/
	@RequestMapping("/asset/queryAssetList/{id}")
	public @ResponseBody EasyUIJson queryAssetList(@PathVariable Integer id,
												   @RequestParam(defaultValue="0")int workId,@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="10")Integer rows) {
		// 调用内存对象数据
		Asset<?> root = assetStore.findAsset(id);

		EasyUIJson uiJson = assetService.assetList(rows, page, root, workId);
		return uiJson;
	}

	@RequestMapping("/asset/queryAssetAddEnergyDeviceType")
	public @ResponseBody List<AssetVo> queryAssetAddEnergyDeviceType(String fullName,@RequestParam(defaultValue = "0") int workId) {
		Asset<?> root = assetStore.findAsset(fullName,">");
		List<AssetVo> list = assetService.queryAssetAddEnergyDeviceType(root, workId);
		return list;
	}

	/**
	 * 请求：/asset/assetDetail/"+id; 方法：assetDetail 参数：id 返回：String
	 * @throws AssetException
	 */
	// 跳转到资产列表
	@RequestMapping("/asset/assetDetail/{id}")
	public String assetDetail(@PathVariable Integer id,String flag,String kind,Model model) throws AssetException {
		Asset<?> root = assetStore.findAsset(id);
		if(root == null){
			root=assetService.findAssetByID(id,kind);
		}
		AssetVo vo = assetService.assetDteail(root,kind);
		model.addAttribute("assetVo", vo);
		int parentId = root.parentId;
		model.addAttribute("id", parentId);
		model.addAttribute("kind", kind);

		if("find".equals(flag)){
			return "/find/findAsset";
		}else{
			if("device".equals(flag)){
				return "/device/deviceList";
			}else{
				return "/asset/assetList";
			}
		}
	}
	@SuppressWarnings("null")
	@RequestMapping("/asset/toDeviceEdit")
	public @ResponseBody AssetVo toDeviceEdit(Integer id,String fullName) throws AssetException {
		Asset<?> root = null;
		int parentId = 0;
		if(null !=id){
			root= assetStore.findAsset(id);
			parentId = root.getParent().getId();
		} else if(null !=fullName){
			root = assetStore.findAsset(fullName,">");
			parentId = root.getParent().getId();
		}
		if(root == null){
			root=assetService.findAssetByID(root.getId(),"DEVICE");
			parentId = root.getParentId();
		}
		AssetVo vo = assetService.assetDteail(root,"DEVICE");
		vo.setParentId(parentId);
		vo.setKind("DEVICE");
		return vo;
	}

	@RequestMapping("/asset/toAssetLikeNameAndCaption")
	public @ResponseBody List<AssetVo> toAssetLikeNameAndCaption(String name,String caption,@RequestParam String king) throws UnsupportedEncodingException {
		List<AssetVo> assetVos=new ArrayList<>();
		king = king.toUpperCase();
		if("DEVICE".equals(king)){
			Collection<Asset<?>> assets = assetStore.getAssets(GenericDevice.class);
			for (Asset<?> asset : assets) {
				try {
					if(!"".equals(caption) && asset.getCaption().contains(caption)) {
						assetVos.add(toassetVo(asset.getId(),"DEVICE"));
					}
					if(!"".equals(name) && asset.getName().contains(name)){
						assetVos.add(toassetVo(asset.getId(),"DEVICE"));
					}
				} catch (AssetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if("PROBE".equals(king)){
			Collection<Asset<?>> assets = assetStore.getAssets(IEC104FloatProbe.class);
			for (Asset<?> asset : assets) {
				try {
					if(!"".equals(caption) && asset.getCaption().contains(caption)) {
						assetVos.add(toassetVo(asset.getId(),"PROBE"));
					}
					if(!"".equals(name) && asset.getName().contains(name)){
						assetVos.add(toassetVo(asset.getId(),"PROBE"));
					}
				} catch (AssetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return assetVos;
	}

	/**
	 * 查询该空间下所有设备(不含摄像头)
	 * @param spaceId
	 * @return
	 */
	@RequestMapping("/asset/queryDeviceBySpaceId")
	public @ResponseBody List<TreeNode> queryDeviceBySpaceId(@RequestParam(name="spaceId")Integer spaceId)  {
		return assetService.queryDeviceBySpaceId(spaceId);
	}

	private AssetVo toassetVo(Integer id,String king) throws AssetException {
		Asset<?> root = assetStore.findAsset(id);
		if(root == null){
			root=assetService.findAssetByID(id,king);
		}
		AssetVo vo = assetService.assetDteail(root,king);
		int parentId = root.parentId;
		vo.setParentId(parentId);
		vo.setKind(king);
		return vo;
	}

	/**
	 * 获取数据跳到编辑页面
	 *
	 * @param id
	 * @param kind
	 * @param flag
	 * @return
	 * @throws AssetException
	 */
	@RequestMapping("/asset/toAssetEdit/{id}")
	public @ResponseBody ResponseModel<Map<String,Object>> toAssetEdit(@PathVariable Integer id, String kind, String flag) throws AssetException {
		Asset<?> root = assetStore.findAsset(id);

		if(root == null){
			root=assetService.findAssetByID(id,kind);
		}
		AssetVo vo = assetService.assetDteail(root,kind);
		int parentId = root.parentId;
		vo.setParentId(parentId);
		Map<String,Object> maps = new HashMap<>(124);
		maps.put("assetVo", vo);
		maps.put("kind", kind);
		maps.put("flag", flag);
		return ResponseModel.success(maps).code(ResultCode.SUCCESS);
//		if("DEVICE".equals(kind)){
//			return "/device/deviceEdit";
//		}if("space".equals(flag)){
//			return "/space/spaceEdit";
//		}else{
//			return "/asset/assetEdit";
//		}

	}
	@RequestMapping("/asset/toAssetEdit")
	@MethodLog(remark="edit",option="修改监控器信息")
	public @ResponseBody AssetVo toAssetEditor(Integer id,String kind){
		Asset<?> root = assetStore.findAsset(id);
		if(root == null){
			try {
				root=assetService.findAssetByID(id,kind);
			} catch (AssetException e) {
				e.printStackTrace();
			}
		}
		AssetVo vo = assetService.assetDteail(root,kind);
		return vo;
	}

	@RequestMapping("/asset/getFirstDeviceIdByAssetId")
	public @ResponseBody DeviceIdResult getFirstDeviceIdByAssetId(Integer id){
		return assetService.getFirstDeviceIdByAssetId(id);
	}

	// 修改，返回结果
	@RequestMapping("/asset/assetEdit")
	public @ResponseBody ResponseModel<String> assetEdit(AssetVo assetVo) {

		try {
			assetService.assetEdit(assetVo);
			return ResponseModel.success("").code(ResultCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.failure("操作失败").code(ResultCode.ERROR);
		}
	}

	/*
	 * 修改中，加载该类型下的服务列表
	 */
	@RequestMapping("/asset/serviceList")
	public @ResponseBody ResponseModel<List<DeptJson>> serviceList(Integer id) {
		Asset<?> root = assetStore.findAsset(id);
		if(null == root){
			return ResponseModel.failure("无服务ID").code(ResultCode.ERROR);
		}
		AssetType type = root.getType();
		List<DeptJson> serviceList = assetService.serviceList(assetStore, type);
		if(null == serviceList){
			return ResponseModel.failure("无服务列表").code(ResultCode.ERROR);
		}
		return ResponseModel.success(serviceList).code(ResultCode.SUCCESS);
	}

	/*
	 * 新增，加载分类列表
	 */
	@RequestMapping("/asset/assetKindList")
	public @ResponseBody ResponseModel<List<DeptJson>> assetKindList() {
		List<DeptJson> list = new ArrayList<DeptJson>();
		DeptJson deptJson1 = new DeptJson();
		deptJson1.setId(10);
		deptJson1.setText("-- 请选择 --");
		deptJson1.setSelected(true);
		list.add(deptJson1);
		for (AssetKind kind : AssetKind.values()) {
			DeptJson deptJson = new DeptJson();
			deptJson.setId(kind.ordinal());
			/*if (kind.toString() == "SPACE") {
				deptJson.setText("空间");
			} else if (kind.toString() == "DEVICE") {
				deptJson.setText("设备");
			} else */if (kind.toString() == "CONTROL") {
				deptJson.setText("控制器");
			} else if (kind.toString() == "PROBE") {
				deptJson.setText("监测器");
			} /*else if (kind.toString() == "SERVICE") {
				deptJson.setText("服务");
			}*/
			list.add(deptJson);
		}
		// 只显示监测器和控制器,其他暂时移除；
		list.removeAll(list.subList(1, 4));
		return ResponseModel.success(list).code(ResultCode.SUCCESS);
	}

	/*
	 * 新增，加载类型列表
	 */
	@RequestMapping("/asset/assetTypeList")
	public @ResponseBody ResponseModel<List<DeptJson>> assetTypeList(Integer id) {
		List<DeptJson> list = new ArrayList<DeptJson>();
		DeptJson json1 = new DeptJson();
		json1.setId(0);
		json1.setText("-- 请选择 --");
		list.add(json1);
		if(id == 10){
			return null;
		}
		AssetTypeManager<AssetType> assetTypes = assetStore.getAssetTypes(AssetKind.values()[id]);
		if (assetTypes != null) {
			int i = 1;
			for (AssetType assetType : assetTypes) {
				if (!assetType.isAbstract()) {
					DeptJson json = new DeptJson();
					json.setName(assetType.getName());
					json.setId(i);
					json.setText(assetType.getCaption());
					list.add(json);
					i++;
				}
			}
		}
		return ResponseModel.success(list).code(ResultCode.SUCCESS);
	}
	/*
	 * 跳到新增页面，传递参数
	 * /asset/toAssetAdd
	 * kind 1,2,3等
	 * typeName 类型的名称
	 * parentId 父类id
	 *
	 */
	@RequestMapping("/asset/toAssetAdd")
	public @ResponseBody ResponseModel<AssetVo> toAssetAdd(Integer kind,String typeName,Integer parentId,Model model){
		AssetVo assetVo = new AssetVo();
		if(kind != null){
			AssetKind assetKind = AssetKind.values()[kind];
			assetVo.setKind(assetKind.toString());
		}
		if(parentId != null){
			assetVo.setParentId(parentId);
		}
		assetVo.setTypeName(typeName);

		return ResponseModel.success(assetVo).code(ResultCode.SUCCESS);
	}

	/**
	 * 通过kind查找资产类型信息
	 * @param kind
	 * @return
	 */
	@RequestMapping("/asset/getAssetTypeList")
	public @ResponseBody ResponseModel getAssetTypeList(String kind) {
		AssetTypeManager<?> assetTypeManager = null;
		if ("PROBE".equals(kind)) {
			assetTypeManager = assetStore.getProbeTypes();
		} else if ("CONTROL".equals(kind)) {
			assetTypeManager = assetStore.getControlTypes();
		}
		Iterator iterator = assetTypeManager.iterator();
		List<AssetVo> list = new ArrayList<>();
		// 由于Boolean类型typeinfo为空
		while (iterator.hasNext()) {
			//取出里面的对象，并赋值给obj
			MonitorType monitorType = (MonitorType) iterator.next();
			AssetVo assetVo = new AssetVo();
			BeanUtils.copyProperties(monitorType,assetVo);
			if(StringUtils.isNotEmpty(monitorType.getKind().toString())){
				assetVo.setKind(monitorType.getKind().toString());
			}
			list.add(assetVo);
		}
		return ResponseModel.success(list).code(ResultCode.SUCCESS);

	}

	/*
	 * 新增，加载该类型下的服务列表
	 */
	@RequestMapping("/asset/addServiceList")
	public @ResponseBody ResponseModel<List<DeptJson>> addServiceList(String typeName, String kind) {
		List<DeptJson> serviceList = null;
		AssetTypeManager<?> assetTypeManager = null;
		if ("PROBE".equals(kind)) {
			assetTypeManager = assetStore.getProbeTypes();
		} else if ("CONTROL".equals(kind)) {
			assetTypeManager = assetStore.getControlTypes();
		}
		if (null != assetTypeManager) {
			AssetType assetType = assetTypeManager.find(typeName);
			serviceList = assetService.serviceList(assetStore, assetType);
		}

		return ResponseModel.success(serviceList).code(ResultCode.SUCCESS);

	}

	/**
	 * 新增,回显获取默认的属性值
	 */
	@RequestMapping("/asset/toPropertyDefaultValue")
	public @ResponseBody ResponseModel<List<AssetProperty>> toPropertyDefaultValue(String typeName, String kind, Model model) {
		List<AssetProperty> propertyDefaultValues = assetService.getPropertyDefaultValue(typeName, kind);
		return ResponseModel.success(propertyDefaultValues).code(ResultCode.SUCCESS);
	}
	/*
	 * 添加，资产信息
	 *
	 * @param assetVo
	 */
	@RequestMapping("/asset/assetAdd")
	@MethodLog(remark="add",option="添加监控器")
	public @ResponseBody
	ResponseModel<String> assetAdd(AssetVo assetVo) {

		try {
			//Math.abs((excelData.get(i).get(0) + excelData.get(i).get(2)).hashCode()) + ""
			/*IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
			int id = gen.next();
			assetVo.setId(id);*/
			assetService.addAssetByKind(assetVo);
			return ResponseModel.success("").code(ResultCode.SUCCESS);
		} catch (AssetException e) {
			e.printStackTrace();
			return ResponseModel.failure("操作失败").code(ResultCode.ERROR);
		}

	}


	/*删除资产*/
	@RequestMapping("/asset/deleteAsset")
	@MethodLog(remark="detete",option="删除监控器")
	public @ResponseBody ResponseModel<String> deleteAsset(String ids){
		try{
			// 用，号切成数组
			String[] ides = ids.split(",");
			//遍历数组
			for (String id : ides) {
				Asset<?> asset = assetStore.findAsset(Integer.parseInt(id));
				//将内存中tree的节点删除
				if(asset != null){
					assetStore.removeAsset(asset.getId());
					assetService.deleteAsset(asset);
				}

			}
			return ResponseModel.success("").code(ResultCode.SUCCESS);
		}catch (Exception e){
			return ResponseModel.failure("操作失败").code(ResultCode.ERROR);
		}
	}

	@RequestMapping("/asset/find_probe")
	public @ResponseBody AssetVo findProbe(AssetVo assetVo){

		return 	assetService.findProbeByPanrentAndCatalog(assetVo);

	}

	/**
	 * 读取excel文件中的用户信息，保存在数据库中
	 *
	 * @param excelFile
	 */
	@RequestMapping("/asset/exlData")
	public @ResponseBody List<FormJson> exlData(@RequestParam(value = "excelFile") MultipartFile excelFile,Integer parentId) {
		//IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
		List<FormJson> form = new ArrayList<FormJson>();
		FormJson json = new FormJson();
		try {
			// 获取到exl的数据
			List<Map<String, Object>> assetList = POIUtil.readExcel(excelFile);
			List<Asset<?>> alist = new ArrayList<Asset<?>>();
			List<AssetProperty> proList = new ArrayList<AssetProperty>();

			for (int i = 0; i < assetList.size(); i++) {


				Map<String, Object> assets = assetList.get(i);
				Object kind = assets.get("kind");
				if (kind.toString().equals("空间")) {
					assetService.potSpace(alist, assets,parentId);
				} else if (kind.toString().equals("控制器")) {
					assetService.potControl(alist, assets, proList,parentId);
				} else if (kind.toString().equals("设备")) {
					assetService.potDevice(alist, assets, proList,parentId);
				} else if (kind.toString().equals("监测器")) {
					assetService.potProbe(alist, assets, proList,parentId);
				} else if (kind.toString().equals("服务")) {
					assetService.potService(alist, assets, proList,parentId);
				}
			}

			// 封装父子结构的id
			assetService.addParentId(assetStore, alist);
			json.setSuccess(true);
			json.setMessage("操作成功");
			form.add(json);
		} catch (Exception e) {
			/* Logger.info("读取excel文件失败", e); */
			e.printStackTrace();
			json.setSuccess(true);
			json.setMessage("操作失败");
			form.add(json);
		}

		return form;
	}



}
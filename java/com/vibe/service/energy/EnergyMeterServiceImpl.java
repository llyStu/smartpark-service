package com.vibe.service.energy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vibe.pojo.AssetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.mapper.energy.EnergyMeterMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetProperty;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.AssetTypeManager;
import com.vibe.monitor.asset.CompoundAsset;
import com.vibe.monitor.asset.Space;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.parse.ExcelSheetPO;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.energy.MeterRelation;
import com.vibe.service.asset.AssetService;
import com.vibe.service.classification.Code;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.TreeNode;

@Service
public class EnergyMeterServiceImpl implements EnergyMeterService {
	@Autowired
	private EnergyMeterMapper meterMapper;
	@Autowired
	private AssetService assetService;
	@Autowired
	private SelectOptionService selelctOption;
	@Autowired
	private AssetStore assetStore;
	@Autowired
	private Application application;
	private List<String> moniId;
	private final int ADD_METER = 2;
	@SuppressWarnings("unused")
	private final int ACTUAL_METER = 0;
	private final int FILL_METER = 1;
	private final int WATER = 4000;
	private final int ELEC = 1002;
	private final int HOT = 13000;
	private final int SYNTHESIZE = 46;
	private final String INPUT_TABLE_NAME = "自定义编码";
	private final int PUT_METER = 12;//相减虚表
	private final int CH_METER = 16;//变化量虚表
	@Override
	public void addEnergyProbe(Meter meter) throws AssetException {//excel表中的一行数据

		if (meter.getMeterType() == ADD_METER) {
			Integer total=0;
			if (meter.getCatalogId() == ELEC) {//1002
				total = 34;
			} else if (meter.getCatalogId() == WATER) {
				total = 37;
			} else if (meter.getCatalogId() == HOT) {
				total = 38;
			}
			//表示为虚表
			meter.setValueType(1);
			String value = getParamAddValue(meter, total);//根据类型查询  total 34 电
			setMeterPropertyList(meter, value);
		} else if (meter.getMeterType() == FILL_METER) {
			String value = getParamLessValue(meter);
			setMeterPropertyList(meter, value);
		} else if(meter.getMeterType() == PUT_METER) {//相减虚表
			//相减虚表 查询父级表  与二级实表相减
			String value = getParamPutValue(meter);
			setMeterPropertyList(meter, value);
		}else if(meter.getMeterType() == CH_METER) {
			//创建虚表存储表的变化量 单个监测器的差
			meter.setValueType(1);
			String value = getSamelevelValueId(meter);
			setMeterPropertyId(meter, value);
		}
		assetService.addAssetByKind(meter);
		meterMapper.addEnergyMeter(meter);
	}
	private String getSamelevelValueId(Meter meter) throws AssetException {
		//获取父id,求变化量的虚表关联Id相当于获取他的父级id
		Meter meter3 = new Meter();
		Set<Integer> parentIds = getEndParentIds(meter.getParentId());
		//获取父id
		meter3.setParents(parentIds);
		String parentId= setParemValue(meter,meter3);
//		meter.setMeterType(0);
		return parentId;
	}

	private String  getParamPutValue(Meter meter) { //相减虚表  查询父id为正 ，子id为负
		//获取父id 同级id
		Meter meter2 = new Meter();
		Meter meter3 = new Meter();
		Set<Integer> parentIds = getEndParentIds(meter.getParentId());
		meter2.setParents(parentIds);
		meter2.setCatalogId(meter.getCatalogId());
		Integer parentMeter = meter.getParentMeter();
		meter2.setParentMeter(parentMeter);//子表相加
		String value = setParemValue(meter,meter2);//获取同级id
		//获取父id
		meter3.setParents(parentIds);
		meter3.setGrade(0);
		String parentId= setParemValue(meter,meter3);
		value = parentId+value;
		//同级表添加负号
		String[] strArr = value.split(",");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<strArr.length;i++){
			sb.append(strArr[i]+",-1*");
		}
		System.out.println(sb.toString());
		String str = sb.substring(0,sb.length()-3);
		System.out.println("相减虚表的父id和同级id-----------"+str);
		meter.setMeterType(0);
		return str;
	}
	private String getParamAddValue(Meter meter, Integer total) {

		Meter meter2 = new Meter();
		Set<Integer> parentIds = getEndParentIds(meter.getParentId());
		meter2.setParents(parentIds);
		meter2.setCatalogId(meter.getCatalogId());
		Integer parentMeter = meter.getParentMeter();
		if (total == meter.getItemizeType()) {
			if(parentMeter== null || parentMeter==0){
				meter2.setGrade(0);
			}else{
				meter2.setParentMeter(parentMeter);//子表相加
			}
			return setParemValue(meter,meter2);
		} else {
			meter2.setItemizeType(meter.getItemizeType());
			meter2.setGrade(null);
			String descValue = setParemValue(meter,meter2);
			meter2.setItemizeType(SYNTHESIZE);
			String synthesizeValue = setParemSynthesizeValue(meter,meter2);
			return descValue + synthesizeValue;
		}
	}

	private String setParemSynthesizeValue(Meter dummyMeter,Meter meter2) {
		List<Meter> meterList = meterMapper.queryEnergyMeter(meter2);
		String value = "";
		for (Meter actualMeter : meterList) {
			String expression = actualMeter.getItemizeExpression();
			String letter = ItemizeTransform(dummyMeter.getItemizeType());
			// 表达式 A:0.5,B:0.5 空调，照明，动力，特殊 ABCD
			if (expression != null && expression.contains(letter)) {
				String[] split = expression.split(",");
				for (String itemizeStr : split) {
					String[] itemize = itemizeStr.split("\\:");
					if (itemize.length > 0 && letter.equals(itemize[0])) {
						//System.out.println("1111===="+Float.parseFloat(itemize[1]));
						value += itemize[1] + "*" + actualMeter.getId() + ",";
						MeterRelation meterRelation = new MeterRelation(actualMeter.getId(),dummyMeter.getId(),dummyMeter.getCatalogId(),Float.parseFloat(itemize[1])*100);
						meterMapper.addMeterRelation(meterRelation);
						//System.out.println("虚表Id==="+dummyMeter.getId()+"    能耗的分类==="+dummyMeter.getCatalogId()+"    实表Id==="+actualMeter.getId()+"    比例==="+meterRelation.getPercentage());
					}
				}
			}
		}
		return value;
	}

	private String ItemizeTransform(Integer itemizeType) {
		if (itemizeType == null)
			return null;
		if (itemizeType == Code.ENERGY_A_KONGTIAo)
			return "A";
		if (itemizeType == Code.ENERGY_B_ZHAOMING)
			return "B";
		if (itemizeType == Code.ENERGY_C_DONGLI)
			return "C";
		if (itemizeType == Code.ENERGY_D_TESHU)
			return "D";
		if (itemizeType == Code.ENERGY_E_QITA)
			return "E";
		return null;
	}

	private String setParemValue(Meter dummyMeter,Meter meter) {
		List<Meter> meterList = null;
		if(dummyMeter.getMeterType() == CH_METER) {
			String nameStr = dummyMeter.getName();
			meter.setName(nameStr.substring(0,nameStr.length()-2));;
			meterList = meterMapper.queryEnergyMeterId(meter);//查询对应的id
		}else {
			meterList = meterMapper.queryEnergyMeter(meter);//查询 t_prope和t_prope_energy  联查
		}

		String value = "";
		if(meterList == null){
			return value;
		}
		for (Meter actualMeter : meterList) {
			value += actualMeter.getId() + ",";
			updateMeter(dummyMeter,actualMeter);//t_probe_energy
			meterMapper.addMeterRelation(new MeterRelation(actualMeter.getId(),dummyMeter.getId(),dummyMeter.getCatalogId(),100));
			//System.out.println("虚表Id==="+dummyMeter.getId()+"    能耗的分类==="+dummyMeter.getCatalogId()+"    实表Id==="+actualMeter.getId()+"    比例===100");
		}
		return value;
	}
	public void updateMeter(Meter parentMeter ,Meter childMeter) {
		//System.out.println(childMeter.getId()+"$"+parentMeter.getId()+"$"+(childMeter.getGrade()+1)+"$");
		childMeter.setParentMeter(parentMeter.getId());
		childMeter.setGrade(childMeter.getGrade()+1);

		meterMapper.updateEnergyMeterById(childMeter);
		setMetersGrade(childMeter);
	}

	private void setMetersGrade(Meter meter) {
		Meter meterBean = new Meter();
		meterBean.setParentMeter(meter.getId());
		List<Meter> energyMeters = meterMapper.queryEnergyMeter(meterBean);
		if(!energyMeters.isEmpty()){
			for (Meter child : energyMeters) {
				child.setGrade(child.getGrade()+1);
				meterMapper.updateEnergyMeterById(child);
				setMetersGrade(child);
			}
		}

	}
	private void setMeterPropertyList(Meter meter, String value) {
		AssetProperty property = new AssetProperty();
		property.setName("ArrayStr");
		property.setValue(value);
		List<AssetProperty> list = new ArrayList<AssetProperty>();
		list.add(property);
		meter.setValueList(list);
	}
	private void setMeterPropertyId(Meter meter, String value) {
		AssetProperty property = new AssetProperty();
		property.setName("AmountOfChange");
		property.setValue(value);
		List<AssetProperty> list = new ArrayList<AssetProperty>();
		list.add(property);
		meter.setValueList(list);
	}

	private String getParamLessValue(Meter meter) {
		Integer parent = meter.getParentMeter();
		String value = "" + parent;
		Meter meter2 = new Meter();
		meter2.setParentMeter(parent);
		meter2.setMeterType(0);
		List<Meter> meterList = meterMapper.queryEnergyMeterByParentId(meter2);
		for (Meter meter3 : meterList) {
			value += ",-1*" + meter3.getId();
		}
		return value;
	}

	/*
	 * 分页获取表 (non-Javadoc)
	 *
	 * @see
	 * com.vibe.service.energy.EnergyMeterService#meterList(java.lang.Integer,
	 * java.lang.Integer, com.vibe.service.energy.Meter)
	 */
	@Override
	public EasyUIJson meterList(Integer rows, Integer page, Meter meter) {
		PageHelper.startPage(page, rows);
		/*
		 * List<Integer> catalogIds =
		 * selelctOption.getEnergyCatalogIds(meter.getItemizeType());
		 * meter.setCatalogs(catalogIds);
		 */
		List<Meter> meterList = meterMapper.queryEnergyMeter(meter);
		for (Meter meter2 : meterList) {
			setParentCaption(meter2);
			setItemizeType(meter2);

		}
		PageInfo<Meter> pageInfo = new PageInfo<Meter>(meterList);
		EasyUIJson uiJson = new EasyUIJson();
		uiJson.setTotal(pageInfo.getTotal());
		uiJson.setRows(meterList);
		return uiJson;
	}

	private void setItemizeType(Meter meter2) {
		Integer itemizeType = meter2.getItemizeType();
		if(itemizeType != null){
			CommonSelectOption energyItemize = selelctOption.getSelectOption(itemizeType, Code.ENERGY);
			if (energyItemize != null)
				meter2.setItemizeCaption(energyItemize.getName());
		}
	}

	private void setParentCaption(Meter meter2) {
		Integer parentId = meter2.getParentId();
		Asset<?> asset = assetStore.findAsset(parentId);
		if (asset != null)
			meter2.setParentCaption(asset.getCaption());
	}

	/*
	 * 生成实表的tree
	 */
	@Override
	public List<TreeNode> getMeterTree(Meter meter) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		List<Meter> meterList = meterMapper.queryEnergyMeterByParentId(meter);
		if (meterList != null && meterList.size() > 0) {
			for (Meter child : meterList) {
				TreeNode node = printMeterTree(child);
				list.add(node);
			}
		}
		return list;
	}

	// 获取树的递归方法
	public TreeNode printMeterTree(Meter meter) {
		TreeNode treeNode = new TreeNode();
		int id = meter.getId();
		treeNode.setId(id);
		treeNode.setText(meter.getCaption());
		meter.setParentMeter(id);
		Meter meterBean = new Meter();
		meterBean.setParentMeter(id);
		meterBean.setCatalogId(meter.getCatalogId());
		List<Meter> meterList = meterMapper.queryEnergyMeterByParentId(meterBean);
		if (meterList != null && meterList.size() > 0) {
			for (Meter child : meterList) {
				TreeNode childNode = printMeterTree(child);
				if (childNode != null) {
					treeNode.addChild(childNode);
				}
			}
		}
		return treeNode;
	}

	/*
	 * 生成虚表的树 参数为需要配置空间的级别
	 */
	@Override
	public List<TreeNode> queryDummyMeter(Integer energyKind) {
		List<TreeNode> spaceTree = getGradeSpace(2);
		findChildMeter(spaceTree, energyKind);
		return spaceTree;
	}

	private void findChildMeter(List<TreeNode> nodes, Integer energyKind) {
		if (nodes != null && nodes.size() > 0) {
			for (int i = 0; i < nodes.size(); i++) {
				Meter meter = new Meter();
				meter.setCatalogId(energyKind);
				meter.setMeterType(ADD_METER);
				TreeNode space = nodes.get(i);
				meter.setParentId(space.getId());
				List<Meter> list = meterMapper.queryEnergyMeterByParentId(meter);
				if (list != null && list.size() > 0) {
					for (Meter meter2 : list) {
						TreeNode node = new TreeNode(meter2.getId(), meter2.getCaption());
						node.setParent(meter2.getParentId());
						node.setItemizeType(meter2.getItemizeType());
						space.addChild(node);
					}
				}
				List<TreeNode> children = space.getNodes();
				findChildMeter(children, energyKind);
			}
		}
	}

	/*
	 * 获取几级空间树
	 */
	private List<TreeNode> getGradeSpace(int number) {
		List<TreeNode> spaceTree1 = assetService.getAllSpaceTree(0);
		int count = 0;
		getSpaceByGrade(spaceTree1, number, count);
		return spaceTree1;
	}

	private void getSpaceByGrade(List<TreeNode> nodes, int number, int count) {
		count++;
		if (nodes != null && nodes.size() > 0) {
			for (TreeNode node : nodes) {
				List<TreeNode> nodes2 = node.getNodes();
				if (count >= number) {
					node.setNodes(new ArrayList<TreeNode>());
				} else {
					getSpaceByGrade(nodes2, number, count);
				}
			}
		}
	}

	@Override
	public Meter queryEnergyMeterByProbeId(int probeId) {
		return meterMapper.queryEnergyMeterByProbeId(probeId);
	}

	@Override
	public Integer queryItemize(int probeId) {
		Meter meter = meterMapper.queryEnergyMeterByProbeId(probeId);
		if (meter == null) {
			return null;
		}
		return meter.getItemizeType();
	}

	@Override
	public void updateEnergyMeter(Meter meter) throws AssetException {
		assetService.assetEdit(meter);
		meterMapper.updateEnergyMeterById(meter);
	}

	@Override
	public void deleteMeter( String ids) throws AssetException {
		// 用，号切成数组
		String[] ides = ids.split(",");
		//遍历数组
		for (String id : ides) {
			Asset<?> asset = assetStore.findAsset(Integer.parseInt(id));
			//将内存中tree的节点删除
			if(asset != null){
				if(asset.isCompound()){
					CompoundAsset<?> parentC = (CompoundAsset<?>) asset;
					Collection<Asset<?>> childrenC = parentC.children();
					for (Asset<?> child : childrenC){
						int pid = asset.getParentId();
						child.setParentId(pid);
						AssetVo assetVo = new AssetVo();
						assetVo.setParentId(pid);
						assetVo.setId(child.getId());
						assetService.assetEdit(assetVo);
					}
				}
				assetStore.removeAsset(asset.getId());
				assetService.deleteAsset(asset);
			}

		}
	}

	@Override
	public List<Meter> queryChildMeter(Meter parameter) {
		List<Integer> spaceIds = getEndSpaceIds(parameter.getParentId());
		parameter.setParentId(null);
		parameter.setParents(spaceIds);
		List<Meter> meters = meterMapper.queryEnergyMeter(parameter);
		for (Meter meter : meters) {
			String expression = meter.getItemizeExpression();
			if (expression != null) {
				String[] split = expression.split(",");
				for (String itemizeStr : split) {
					String[] itemize = itemizeStr.split("\\*");
					if (itemize.length > 0 && Integer.parseInt(itemize[0]) == parameter.getItemizeType()) {
						meter.setRedio(itemize[1]);
					}
				}
			}
		}
		return meters;
	}
	public List<Integer> getEndSpaceIds(Integer id){
		List<Integer> list = new ArrayList<Integer>();
		Asset<?> root = assetStore.findAsset(id);
		getEndSpaceId(root,list);
		return list;
	}
	public void getEndSpaceId(Asset<?> asset,List<Integer> list){
		if (asset.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) asset;
			Collection<Asset<?>> childrens = parent.children();
			int i= 0;
			for (Asset<?> child : childrens) {
				if(AssetKind.SPACE.equals(child.getKind())){
					Space space =(Space)child;
					i++;
					getEndSpaceId(space,list);
				}
			}
			if(i == 0){
				list.add(asset.getId());
			}
		}
	}
	//递归获取监测器直接父设备的id集合
	public Set<Integer> getEndParentIds(Integer id){
		Set<Integer> set = new HashSet<Integer>();
		Asset<?> root = assetStore.findAsset(id);
		//System.out.println(id+"   "+root.getName());
		getEndParentId(root,set);
		return set;
	}
	public void getEndParentId(Asset<?> asset,Set<Integer> set){
		if (asset.isCompound()) {
			CompoundAsset<?> parent = (CompoundAsset<?>) asset;
			Collection<Asset<?>> childrens = parent.children();

			for (Asset<?> child : childrens) {
				if(child.isMonitor()){
					System.out.println(asset.getCaption()+"=="+asset.getName()+"child"+child.getName()+"-----"+asset.getId());
					set.add(asset.getId());
				}
				getEndParentId(child,set);
			}

		}
	}
	@Override
	public void addMeterGrade(Meter meter) {
		Integer parentMeter = meter.getParentMeter();
		if (parentMeter == 0) {
			meter.setGrade(0);
		} else {
			Meter parent = meterMapper.queryEnergyMeterByProbeId(parentMeter);
			Integer grade = parent.getGrade();
			meter.setGrade(grade + 1);
		}
	}

	@Override
	public void imEnergyMeter(List<ExcelSheetPO> excelSheet) throws AssetException {
		List<List<String>> exelList = getimMeterList(excelSheet);
		List<Meter> meterList = new ArrayList<Meter>();
		List<Meter> meters = new ArrayList<Meter>();
		List<SetAndTransform > meterTransformList = new ArrayList<SetAndTransform >();
		//IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
		for (int i = 1; i < exelList.size(); i++) {
			List<AssetProperty> propertyList = new ArrayList<AssetProperty>();
			Meter meter = new Meter();
			SetAndTransform setAndTransform = new SetAndTransform(meter);
			List<String> list = exelList.get(i);
			//Integer next = gen.next();
			setAndTransform.setMeter(list);
			setAndTransform.setSrcMeter(list);
			String parentName = setAndTransform.getSrcMeter().getParent().trim();
			if(moniId != null && !moniId.isEmpty() && moniId.get(i) != null){
				setAndTransform.setId(Integer.parseInt(moniId.get(i)));
			}else{
				int id = Math.abs((parentName +meter.getName()).hashCode());
				setAndTransform.setId(id);
			}
			meterTransformList.add(setAndTransform);
			meters.add(setAndTransform.getMeter());
			for (int j = 17; j < list.size(); j++) {
				AssetProperty assetProperty = new AssetProperty();
				String value = list.get(j);
				if(value != null){
					int length = value.length();
					if (length>=2) {  //这里大于等于2是防止有些列只有一个字符，到下面会报错
						if (value.substring(length-2, length).equals(".0")) //通过截取最后两个字符，如果等于.0 就去除最后两个字符
							assetProperty.setValue(value.substring(0, length-2));
						else
							assetProperty.setValue(value);
					}else {
						assetProperty.setValue(value);
					}
				}
				propertyList.add(assetProperty);
			}
			meter.setValueList(propertyList);
		}
		transformDataTypeToDB(meterList, meters, meterTransformList);//从电表中取出的全部数据 做处理

		addToDB(meterList);
	}

	private void addToDB(List<Meter> meterList) throws AssetException {
		 /*Map<Object, List<Meter>> collect = meterList.stream().collect(Collectors.groupingBy(x -> x.meterType));
		 List<Meter> actual = collect.get(0);*/
		for (Meter meter :meterList) {
			addEnergyProbe(meter);
		}
	}

	private void transformDataTypeToDB(List<Meter> meterList, List<Meter> meters,
									   List<SetAndTransform> meterTransformList) {
		for (SetAndTransform transform : meterTransformList) {
			List<CommonSelectOption> catalogList = selelctOption.querySelectOptionList(null, Code.PROBE);//t_code表查询 2001
			transform.transformCatalog(catalogList);
			transform.transformGrade();
			List<CommonSelectOption> ItemizeList = selelctOption.querySelectOptionList(null, Code.ENERGY);//2200
			transform.transformItemizeType(ItemizeList);
			transform.transformParent(assetStore);
			transform.transformParentMeter(meters);

			AssetTypeManager<AssetType> assetTypes = assetStore.getAssetTypes(AssetKind.valueOf("PROBE"));
			transform.transformType(assetTypes);
			transform.transformMeterType();
			transform.transformSource(assetStore);
			Meter meter = transform.getMeter();
			if(meter.getMeterType() == 0){//  实表
				List<AssetProperty> properts = transform.getMeter().getValueList();
				List<AssetProperty> propertyDefaultValue = assetService.getPropertyDefaultValue(transform.getMeter().getTypeName(), "PROBE");
				int propertsSize = properts.size();
				int defaultSize = propertyDefaultValue.size();
				if(propertsSize > 0 && defaultSize > 0 && propertsSize == defaultSize){
					for (int i = 0; i < propertyDefaultValue.size(); i++) {
						propertyDefaultValue.get(i).setValue(properts.get(i).getValue());
					}
					meter.setValueList(propertyDefaultValue);
				}

			}

			meterList.add(meter);

		}
	}

	private List<List<String>> getimMeterList(List<ExcelSheetPO> excelSheet) {
		if (excelSheet != null) {
			for (ExcelSheetPO excelSheetPO : excelSheet) {
				String sheetName = excelSheetPO.getSheetName();
				//if(ElEC_METER.equals(sheetName)){
				moniId = new ArrayList<String>();
				List<List<String>> dataList = excelSheetPO.getDataList();
				if(INPUT_TABLE_NAME.equals(sheetName)){

					for (List<String> list : dataList) {
						String value = list.get(0);
						if(value != null){
							int length = value.length();
							if (value.substring(length-2, length).equals(".0")){ //通过截取最后两个字符，如果等于.0 就去除最后两个字符
								value = value.substring(0, length-2);
							}
							if(value.equals("0")){
								value = null;
							}
							moniId.add(value);
							list.remove(0);
						}
					}
				}

				return dataList;
			}
		}
		return null;
	}

	@Override
	public EasyUIJson relationMeterList(Integer rows, Integer page, MeterRelation meterRelation) {
		List<Meter> meterList = getRelationMeters(meterRelation);
		EasyUIJson uiJson = new EasyUIJson();
		uiJson.setTotal((long)meterList.size());
		uiJson.setRows(meterList.subList((page - 1) * rows, page * rows > meterList.size() ? meterList.size() : page * rows));
		return uiJson;
	}

	private List<Meter> getRelationMeters(MeterRelation meterRelation) {
		List<Meter> list = new ArrayList<Meter>();
		List<MeterRelation> relations = meterMapper.queryMeterRelation(meterRelation);
		for (MeterRelation relation : relations) {
			Meter meter = meterMapper.queryEnergyMeterByProbeId(relation.getActualId());
			meter.setRedio(String.valueOf(relation.getPercentage()));
			list.add(meter);
		}
		return list;
	}
}

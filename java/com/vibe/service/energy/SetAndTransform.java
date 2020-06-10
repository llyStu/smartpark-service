package com.vibe.service.energy;

import java.util.Collection;
import java.util.List;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.AssetTypeManager;
import com.vibe.monitor.asset.type.AssetType;
import com.vibe.monitor.asset.type.MonitorType;
import com.vibe.monitor.asset.type.ServiceType;
import com.vibe.pojo.CommonSelectOption;

public class SetAndTransform {

	private SrcMeter srcMeter;
	private Meter meter;
	public final int INDEX_A = 0;
	public final int INDEX_B = 1;
	public final int INDEX_C = 2;
	public final int INDEX_D = 3;
	public final int INDEX_E = 4;
	public final int INDEX_F = 5;
	public final int INDEX_G = 6;
	public final int INDEX_H = 7;
	public final int INDEX_I = 8;
	public final int INDEX_J = 9;
	public final int INDEX_K = 10;
	public final int INDEX_L = 11;
	public final int INDEX_M = 12;
	public final int INDEX_N = 13;
	public final int INDEX_O = 14;
	public final int INDEX_P = 15;
	public final int INDEX_Q = 16;
	private final String METERGRADE_A = "一级";
	private final String METERGRADE_B = "二级";
	private final String METERGRADE_C = "三级";
	private final String METERGRADE_D = "四级";
	private final String METERGRADE_E = "五级";
	private final String METERGRADE_F = "六级";
	private final String METERGRADE_G = "七级";
	private final String METERGRADE_H = "八级";
	private final String METERGRADE_I = "九级";
	private final String METERGRADE_J = "十级";
	@SuppressWarnings("unused")
	private final String ACTUAL = "实表";
	private final String DUMMY_A = "末端虚表";
	private final String DUMMY_B = "累加虚表";
	private final String DUMMY_C = "相减虚表";//后加直连高端表的虚表
	private final String DUMMY_D = "变化量虚表";
	@SuppressWarnings("unused")
	private final String Total = "34";


	public SetAndTransform() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SetAndTransform(Meter meter) {
		super();
		this.srcMeter = new SrcMeter();
		this.meter = meter;
	}

	public SrcMeter getSrcMeter() {
		return srcMeter;
	}

	public void setSrcMeter(SrcMeter srcMeter) {
		this.srcMeter = srcMeter;
	}

	public Meter getMeter() {
		return meter;
	}

	public void setMeter(Meter meter) {
		this.meter = meter;
	}

	public void setSrcMeter(List<String> list) {
		srcMeter.setCatalog(list.get(INDEX_B));
		srcMeter.setGrade(list.get(INDEX_M));
		srcMeter.setItemizeType(list.get(INDEX_O));
		srcMeter.setMeterType(list.get(INDEX_N));
		srcMeter.setParent(list.get(INDEX_A).trim());
		srcMeter.setParent_meter(list.get(INDEX_L));
		srcMeter.setSource(list.get(INDEX_E));
		srcMeter.setType(list.get(INDEX_C));
	}

	public void setMeter(List<String> list) {
		meter.setName(list.get(INDEX_D).trim());
		meter.setTime_interval(list.get(INDEX_F));
		meter.setCaption(list.get(INDEX_K));
		meter.setItemizeExpression(list.get(INDEX_P));
		meter.setKind(AssetKind.PROBE.toString());
		meter.setTransform(list.get(INDEX_G));
		meter.setWarn_cond(list.get(INDEX_H));
		meter.setMinValue(list.get(INDEX_I));//最小值
		meter.setMaxValue(list.get(INDEX_J));//最大值
		meter.setSave_interval(list.get(INDEX_Q));
	}

	public void transformMeterType() {
		String meterType = srcMeter.getMeterType();
		if (DUMMY_A.equals(meterType)) {
			meter.setMeterType(INDEX_B);
		} else if(DUMMY_C.equals(meterType)) {
			meter.setMeterType(INDEX_M);//12  相减虚表
		}else if (DUMMY_B.equals(meterType)) {  //累加虚表
			meter.setMeterType(INDEX_C);//2
		} else if (DUMMY_D.equals(meterType)) {
			meter.setMeterType(INDEX_Q);//16 变化量虚表
		} else {
			meter.setMeterType(INDEX_A); //0  实表
		}
	}

	public void transformParentMeter(List<Meter> meters) {
		for (Meter parentMeter : meters) {
			String parent_meter = srcMeter.getParent_meter();
			if (parent_meter == null) {
				meter.setParentMeter(0);
			} else {
				if (parentMeter.getName().equals(parent_meter.trim())) {
					meter.setParentMeter(parentMeter.getId());
				}
			}

		}
	}

	public void transformSource(AssetStore assetStore) {
		AssetType assetType = assetStore.getProbeTypes().find(meter.getTypeName());
		if (assetType != null && assetType instanceof MonitorType) {
			MonitorType monitorType = (MonitorType) assetType;
			ServiceType source = monitorType.getSource();
			if (source != null) {
				Collection<Asset<ServiceType>> assets = assetStore.getAssets(source);
				if (assets != null && assets.size() > 0) {
					for (Asset<?> service : assets) {
						if (service.getCaption().equals(srcMeter.getSource())) {
							meter.setSource(service.getId());
						}
					}
				}
			}
		}
	}

	public void transformType(AssetTypeManager<AssetType> assetTypes) {
		for (AssetType assetType : assetTypes) {
			if (!assetType.isAbstract()) {
				String name = assetType.getCaption();
				boolean isEquals = name.equals(srcMeter.getType());
				if (isEquals) {
					meter.setTypeName(assetType.getName());
				}
			}
		}
	}

	public void transformParent(AssetStore assetStore) {
		Asset<?> asset = assetStore.findAsset(srcMeter.getParent(), ">");

		if (asset == null) {
			System.out.println("not find parent ==" + srcMeter.getParent() + "==");
		} else {
			meter.setParentId(asset.getId());
		}
	}

	public void transformItemizeType(List<CommonSelectOption> list) {
		for (CommonSelectOption commonSelectOption : list) {
			String name = commonSelectOption.getName();
			boolean isEquals = name.equals(srcMeter.getItemizeType());
			if (isEquals) {
				meter.setItemizeType(commonSelectOption.getId());
			}
		}
	}

	public void transformCatalog(List<CommonSelectOption> list) {
		for (CommonSelectOption commonSelectOption : list) {
			String name = commonSelectOption.getName();
			boolean isEquals = name.equals(srcMeter.getCatalog());
			if (isEquals) {
				meter.setCatalogId(commonSelectOption.getId());
			}
		}
	}

	public void transformGrade() {
		if (METERGRADE_A.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_A);
		}
		if (METERGRADE_B.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_B);
		}
		if (METERGRADE_C.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_C);
		}
		if (METERGRADE_D.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_D);
		}
		if (METERGRADE_E.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_E);
		}
		if (METERGRADE_F.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_F);
		}
		if (METERGRADE_G.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_G);
		}
		if (METERGRADE_H.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_H);
		}
		if (METERGRADE_I.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_I);
		}
		if (METERGRADE_J.equals(srcMeter.getGrade())) {
			meter.setGrade(INDEX_J);
		}
	}

	public void setId(Integer next) {
		meter.setId(next);

	}
}

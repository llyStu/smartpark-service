package com.vibe.service.emergency;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.data.UseCode;
import com.vibe.mapper.emergency.EmergencyManagementMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Device;
import com.vibe.monitor.asset.Monitor;
import com.vibe.monitor.util.UnitInterface;
import com.vibe.monitor.util.UnitUtil;
import com.vibe.pojo.emergency.Emergency;
import com.vibe.pojo.emergency.EmergencyTask;
import com.vibe.pojo.emergency.EmergencyTaskDetail;
import com.vibe.pojo.emergency.EmergencyTaskDetailVo;
import com.vibe.pojo.emergency.EmergencyTaskVo;
import com.vibe.pojo.emergency.EmergencyType;
import com.vibe.pojo.emergency.EmergencyVo;
import com.vibe.service.classification.Code;
import com.vibe.service.configuration.ConfigurationService;

import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;

import com.vibe.utils.Page;
import com.vibe.utils.TreeNode;

@Service
public class EmergencyManagementService {
	@Autowired
	private EmergencyManagementMapper emm;

	public FormJson addEmergencyTask(EmergencyTask e) {
		if (emm.addEmergencyTask(e) != 1) {
			return FormJsonBulider.fail("保存失败");
		}
		return FormJsonBulider.success().withCause(e.getEtid().toString());
	}

	public FormJson updateEmergencyTask(EmergencyTask e) {
		if (e.getEtid() == null) {
			return FormJsonBulider.fail("需要etid");
		}
		if (emm.updateEmergencyTask(e) != 1) {
			return FormJsonBulider.fail("修改失败");
		}
		return FormJsonBulider.success();
	}

	public FormJson deleteEmergencyTask(int[] etids) {
		if (etids == null || etids.length == 0)
			return FormJsonBulider.success();

		int deleted = emm.deleteEmergencyTask(etids);
		if (deleted != etids.length) {
			return FormJsonBulider.fail("只删除了 " + deleted + " 条记录");
		}
		emm.delEmergencyTaskDetailByParent(etids);
		return FormJsonBulider.success();
	}

	public Page<EmergencyTask> findEmergencyTask(EmergencyTaskVo e, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<EmergencyTask> list = emm.findEmergencyTask(e);
		return pageOf(list);
	}

	public FormJson addEmergencyTaskDetail(EmergencyTaskDetail e) {
		if (e.getEtid() == null) {
			return FormJsonBulider.fail("需要etid");
		}
		if (emm.addEmergencyTaskDetail(e) != 1) {
			return FormJsonBulider.fail(null);
		}
		return FormJsonBulider.success().withCause(e.getTdid().toString());
	}

	public FormJson updateEmergencyTaskDetail(EmergencyTaskDetail e) {
		if (e.getTdid() == null) {
			return FormJsonBulider.fail("需要tdid");
		}
		if (emm.updateEmergencyTaskDetail(e) != 1) {
			return FormJsonBulider.fail(null);
		}
		return FormJsonBulider.success();
	}

	public FormJson delEmergencyTaskDetail(int[] etdid) {
		if (etdid == null || etdid.length == 0) {
			return FormJsonBulider.success();
		}
		int deleted = emm.delEmergencyTaskDetail(etdid);
		if (deleted == etdid.length) {
			return FormJsonBulider.success();
		}
		return FormJsonBulider.fail("只删除了 " + deleted + " 条记录");
	}

	public Page<EmergencyTaskDetail> findEmergencyTaskDetail(EmergencyTaskDetailVo vo, Integer pageNum,
                                                             Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<EmergencyTaskDetail> list = emm.findEmergencyTaskDetail(vo);
		return pageOf(list);
	}

	public FormJson addEmergency(Emergency e) {
		if (emm.addEmergency(e) != 1) {
			return FormJsonBulider.fail(null);
		}
		return FormJsonBulider.success();
	}

	public FormJson updateEmergency(Emergency e) {
		if (e.getEid() == null) {
			return FormJsonBulider.fail("需要eid");
		}
		if (emm.updateEmergency(e) != 1) {
			return FormJsonBulider.fail(null);
		}
		return FormJsonBulider.success();
	}

	public FormJson delEmergency(int[] eid) {
		if (eid == null || eid.length == 0) {
			return FormJsonBulider.success();
		}
		int deleted = emm.delEmergency(eid);
		if (deleted == eid.length) {
			return FormJsonBulider.success();
		}
		return FormJsonBulider.fail("只删除了 " + deleted + " 条记录");
	}

	public Page<Emergency> findEmergency(EmergencyVo vo, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Emergency> list = emm.findEmergency(vo);
		return pageOf(list);
	}

	private static <T> Page<T> pageOf(List<T> list) {
		PageInfo<T> page = new PageInfo<>(list);

		Page<T> result = new Page<>();
		result.setRows(page.getList());
		result.setPage(page.getPageNum());
		result.setSize(page.getPageSize());
		result.setTotal((int) page.getTotal());
		return result;
	}

	public FormJson setMonitors(Integer[] mids) {
		if (cs.set(KEY_MONITORS, StringUtils.join(mids, ','))) {
			return FormJsonBulider.success();
		}
		return FormJsonBulider.fail(null);
	}
	private List<EmergencyType> mergencyTypes = Arrays.asList(
			new EmergencyType(1, "火灾"),
			new EmergencyType(8, "大面积停电事故"),
			new EmergencyType(8, "突发环境事件"),
			new EmergencyType(8, "网络信息安全事件"),
			new EmergencyType(8, "轨道交通运营突发事件"),
			new EmergencyType(2, "地震"),
			new EmergencyType(3, "洪水"),
			new EmergencyType(4, "台风"),
			new EmergencyType(5, "烟雾报警"),
			new EmergencyType(6, "非法入侵"),
			new EmergencyType(7, "盗窃抢劫"));
	public List<EmergencyType> getEmergencyTypes() {
		return this.mergencyTypes;
	}

	
	@Autowired
	private AssetStore as;
	@Autowired
	private ConfigurationService cs;
	private static final String KEY_MONITORS = "vibe-web.EmergencyManagement.monitors";

	public List<TreeNode> getMonitors() {
		String mids = cs.get(KEY_MONITORS);
		if (mids == null)
			return Collections.emptyList();

		TreeNode[] ret = Arrays.stream(mids.split(","))
				.map(Integer::valueOf)
				.map(as::findAsset)
				.map(this::isMonotorAndDevice)
				.toArray(v -> new TreeNode[v]);
		return Arrays.asList(ret);
	}

	public List<TreeNode> getMonitorsByMids(int[] mids) {
		TreeNode[] ret = Arrays.stream(mids)
				.mapToObj(as::findAsset)
				.map(this::isMonotorAndDevice)
				.toArray(v -> new TreeNode[v]);
		return Arrays.asList(ret);
	}
	@Autowired
	CodeDictManager codeDictManager;
	private TreeNode isMonotorAndDevice(final Asset<?> root) {
		if (root == null) return null;
		
		TreeNode treeNode = new TreeNode();
		setNodesBasicProbe(root, treeNode);

		if (root instanceof Device) {
			Device device = (Device) root;
			int catalog = device.getCatalog();
			treeNode.setCatalog(catalog);
		} else if (root instanceof Monitor) {
			Monitor mo = (Monitor) root;
			int catalog = mo.getMonitorKind();
			treeNode.setCatalog(catalog);
			String unit = mo.getUnit();
			treeNode.setUnit(unit);
			Object value = mo.getValue();
			treeNode.setValue(value.toString());
			treeNode.setValueStr(value==null?null:value.toString());
			if (value != null) {
				if (value instanceof Float && ((unit == null)? true:(!unit.contains("|")))) {
					String number = new UseCode(codeDictManager).getValue(value,(short) Code.PROBE,mo.getMonitorKind());
					treeNode.setValueStr(number + (unit == null ? "" : unit));
				} else {
					UnitUtil.unitParse(unit, value.toString(), new UnitInterface() {
						
						@Override
						public void parseResult(String result) {
							// TODO Auto-generated method stub
							treeNode.setValueStr(result);
						}
						
					});
				}
			}
		}
		return treeNode;
	}

	private static final String PROBE_STR = "监测器";
	private static final String CONTROL_STR = "控制器";
	private static final String DEVICE_STR = "设备";

	private String getKindStr(Asset<?> asset) {
		String kindStr = null;
		switch (asset.getKind()) {
		case SPACE:
			kindStr = "空间";
			break;
		case PROBE:
			kindStr = PROBE_STR;
			break;
		case CONTROL:
			kindStr = CONTROL_STR;
			break;
		case DEVICE:
			kindStr = DEVICE_STR;
			break;
		case SERVICE:
			kindStr = "服务";
			break;
		default:
			break;
		}
		return kindStr;
	}

	private String getStatusStr(Asset<?> asset) {
		String statusStr = null;
		switch (asset.getState()) {
		case WARNING:
			statusStr = "警告";
			break;
		case ERROR:
			statusStr = "错误";
			break;
		case NORMAL:
			statusStr = "正常";
			break;
		case UNKNOWN:
			statusStr = "未知";
			break;
		default:
			break;
		}
		return statusStr;
	}

	private void setNodesBasicProbe(final Asset<?> root, TreeNode treeNode) {
		treeNode.setId(root.getId());
		treeNode.setKind(root.getKind().ordinal());
		treeNode.setStatus(root.getState().ordinal());
		treeNode.setText(root.getCaption());
		treeNode.setKindStr(getKindStr(root));
		treeNode.setStatusStr(getStatusStr(root));
	}
	private List<EmergencyType> disposeTypes = Arrays.asList(
			new EmergencyType(1, "未处理"),
			new EmergencyType(2, "正在处理"),
			new EmergencyType(3, "已处理"));

	public List<EmergencyType> getDisposeTypes() {
		// TODO Auto-generated method stub
		return this.disposeTypes;
	}
}

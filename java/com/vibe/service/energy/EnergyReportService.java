package com.vibe.service.energy;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.table.DefaultTableModel;

import com.vibe.util.ScaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.energy.EnergyReportMapper;
import com.vibe.pojo.energy.CatalogEnergyReportData;
import com.vibe.pojo.energy.EnergyReport;
import com.vibe.pojo.energy.EnergyReportSelector;
import com.vibe.pojo.energy.EnergyReportVo;
import com.vibe.pojo.energy.SpaceEnergyReport;
import com.vibe.pojo.energy.SpaceEnergyReportData;
import com.vibe.scheduledtasks.report_statisticstask.StatisticsType;
import com.vibe.service.global.navigation.NavigationService;
import com.vibe.service.report.ReportService;
import com.vibe.utils.TreeNode;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

@Service
public class EnergyReportService {
	@Resource(name = "navigationServiceImpl")
	private NavigationService ns;
	@Autowired
	private EnergyReportMapper erm;

	public Object _handle(EnergyReportSelector selector) throws ParseException {
		switch (selector.getExec()) {
		case -1:
			return this.getCatalogEnergyReportData(selector.start(), selector.end(), selector.srcTable().destTable(),
					selector.catalog());
		case -2:
			return this.getSpaceEnergyReportData(selector.getParentSpace(), selector.start(), selector.end(),
					selector.srcTable().destTable(), selector.catalog());
		}
		return this.handle(selector);
	}

	public EnergyReport handle(EnergyReportSelector selector) throws ParseException {
		switch (selector.getExec()) {
		case 1:
			return this.getCatalogEnergyReport(selector.start(), selector.end(), selector.srcTable(),
					selector.catalog());
		case 2:
			return this.getSpaceEnergyReport(selector.getParentSpace(), selector.start(), selector.end(),
					selector.srcTable(), selector.catalog());
		}
		throw new IllegalArgumentException("类型错误: exec = [1, 2]");
	}

	@Autowired
	private ReportService rs;

	public JasperPrint compileJasperPrint(HttpServletRequest req, EnergyReport data) throws Exception {
		JasperReport jasperReport = rs.compileAndGetJasperReport(req, "EnergyReport", data.getxAxis().length,
				data.getyAxis().length);

		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("title", data.getTitle());
		parms.put("annotation", data.getAnnotation());
		parms.put("xAxis", data.getxAxis());
		parms.put("yAxis", data.getyAxis());

		String[] columnNames = IntStream.range(0, data.getxAxis().length).mapToObj(v -> "data_col" + v)
				.toArray(v -> new String[v]);
		JRTableModelDataSource dataSource = new JRTableModelDataSource(
				new DefaultTableModel(data.getData(), columnNames));

		return JasperFillManager.fillReport(jasperReport, parms, dataSource);
	}

	EnergyReport getCatalogEnergyReport(Date start, Date end, StatisticsType srcTable, int catalog) {
		List<CatalogEnergyReportData> data = this.getCatalogEnergyReportData(start, end, srcTable.destTable(), catalog);
		
		if (data == null || data.isEmpty()) {
			EnergyReport ret = new EnergyReport();
			ret.setTitle("分项能耗报表 ");
			return ret;
		}

		OffsetDateTime startTime = srcTable.deadline(start.toInstant().atZone(ZoneId.systemDefault()));
		String[] yAxis = new String[srcTable.between(startTime, end.toInstant().atZone(ZoneId.systemDefault())) + 1];
		List<String> xAxis = new ArrayList<String>();
		List<Float[]> result = new ArrayList<Float[]>();

		Float[] tmp = new Float[yAxis.length];
		int flag = -1;
		for (CatalogEnergyReportData it : data) {
			if (flag != (int) it.getCatalog()) {
				flag = it.getCatalog();
				xAxis.add(it.getName());
				tmp = new Float[yAxis.length];
				result.add(tmp);
			}
			
			int j = srcTable.between(startTime, it.getMoment().toInstant().atZone(ZoneId.systemDefault()));
			tmp[j] = ScaleUtil.roundFloat(it.getInc());
		}
		Float[][] array = new Float[yAxis.length][];
		for (int i = 0; i < yAxis.length; ++i) {
			if(srcTable.equals(StatisticsType.FLOAT_MONTHLY)){
				yAxis[i] = DateTimeFormatter.ISO_LOCAL_DATE.format(startTime).substring(0,7);
			}else {
				yAxis[i] = DateTimeFormatter.ISO_LOCAL_DATE.format(startTime);
			}
			startTime = srcTable.next(startTime);
			array[i] = new Float[xAxis.size()];
			for (int j = 0; j < xAxis.size(); ++j) {
				array[i][j] = result.get(j)[i];
			}
		}
		String sTime = DateTimeFormatter.ISO_LOCAL_DATE.format(start.toInstant().atZone(ZoneId.systemDefault()));
		String eTime = DateTimeFormatter.ISO_LOCAL_DATE.format(end.toInstant().atZone(ZoneId.systemDefault()));
		if(srcTable.equals(StatisticsType.FLOAT_MONTHLY)){
			sTime = sTime.substring(0,7);
			eTime =	eTime.substring(0,7);
		}
		EnergyReport ret = new EnergyReport(xAxis.toArray(new String[0]), yAxis, array);
		ret.setTitle("分项能耗报表 " + sTime
			+ "~" + eTime);
		ret.setAnnotation("报表时间：" + DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()) + "      单位："
				+ data.get(0).getUnit());
		return ret;
	}

	private List<CatalogEnergyReportData> getCatalogEnergyReportData(Date start, Date end, String srcTable,
                                                                     int catalog) {
		EnergyReportVo vo = new EnergyReportVo();
		vo.setSrcTable(srcTable);
		vo.setCatalog(catalog);
		vo.setStart(start);
		vo.setEnd(end);
		return erm.getCatalogEnergyReport(vo);
	}
	
	private EnergyReport toEnergyReport(Date start, Date end, StatisticsType srcTable, List<SpaceEnergyReportData> data) {
		if (data == null || data.isEmpty()) {
			EnergyReport ret = new EnergyReport();
			ret.setTitle("建筑能耗报表 ");
			return ret;
		}
		
		OffsetDateTime startTime = srcTable.deadline(start.toInstant().atZone(ZoneId.systemDefault()));
		String[] yAxis = new String[srcTable.between(startTime, end.toInstant().atZone(ZoneId.systemDefault())) + 1];
		List<String> xAxis = new ArrayList<String>();
		List<Float[]> result = new ArrayList<Float[]>();

		Float[] tmp = null;
		int flag = -1;
		for (SpaceEnergyReportData it : data) {
			if (flag != (int) it.getSpaceId()) {
				flag = it.getSpaceId();
				xAxis.add(it.getSpaceCaption());
				tmp = new Float[yAxis.length];
				result.add(tmp);
			}
			int j = srcTable.between(startTime, it.getMoment().toInstant().atZone(ZoneId.systemDefault()));
			tmp[j] = ScaleUtil.roundFloat(it.getInc());
		}
		Float[][] array = new Float[yAxis.length][];
		for (int i = 0; i < yAxis.length; ++i) {
			if(srcTable.equals(StatisticsType.FLOAT_MONTHLY)){
				yAxis[i] = DateTimeFormatter.ISO_LOCAL_DATE.format(startTime).substring(0,7);
			}else {
				yAxis[i] = DateTimeFormatter.ISO_LOCAL_DATE.format(startTime);
			}
			startTime = srcTable.next(startTime);
			array[i] = new Float[xAxis.size()];
			for (int j = 0; j < xAxis.size(); ++j) {
				array[i][j] = result.get(j)[i];
			}
		}
		String sTime = DateTimeFormatter.ISO_LOCAL_DATE.format(start.toInstant().atZone(ZoneId.systemDefault()));
		String eTime = DateTimeFormatter.ISO_LOCAL_DATE.format(end.toInstant().atZone(ZoneId.systemDefault()));
		if(srcTable.equals(StatisticsType.FLOAT_MONTHLY)){
			sTime = sTime.substring(0,7);
			eTime =	eTime.substring(0,7);
		}
		EnergyReport ret = new EnergyReport(xAxis.toArray(new String[0]), yAxis, array);
		ret.setTitle("建筑能耗报表 " + sTime
				+ "~" + eTime);
		ret.setAnnotation("报表时间：" + DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()) + "      单位："
				+ data.get(0).getProbeUnit());
		return ret;
	}
	private EnergyReport getSpaceEnergyReport(int parentSpaceId, Date start, Date end, StatisticsType srcTable, int catalog) {
		List<SpaceEnergyReportData> data = getSpaceEnergyReportData(parentSpaceId, start, end, srcTable.destTable(), catalog);
		return this.toEnergyReport(start, end, srcTable, data);
	}
	private List<SpaceEnergyReportData> getSpaceEnergyReportData(int parentSpaceId, Date start, Date end, String srcTable,
			int catalog) {
		EnergyReportVo vo = new EnergyReportVo();
		vo.setSrcTable(srcTable);
		vo.setCatalog(catalog);
		vo.setSpaceId(parentSpaceId);
		vo.setStart(start);
		vo.setEnd(end);

		return erm.getSpaceEnergyReport(vo);
	}

	@Deprecated
	private EnergyReport getSpaceEnergyReport0(int parentSpace, Date start, Date end, String srcTable, int catalog) {
		List<SpaceEnergyReport> data = this.getSpaceEnergyReportData0(parentSpace, start, end, srcTable, catalog);

		ZonedDateTime startTime = start.toInstant().atZone(ZoneId.systemDefault()).with(ChronoField.NANO_OF_DAY, 0);
		Instant beginInstant = startTime.toInstant();
		String[] yAxis = new String[(int) beginInstant.until(end.toInstant(), ChronoUnit.DAYS) + 1];
		String[] xAxis = new String[data.size()];
		Float[][] result = new Float[yAxis.length][xAxis.length];

		for (int i = 0; i < data.size(); ++i) {
			SpaceEnergyReport it = data.get(i);
			xAxis[i] = it.getTreeNode().getText();
			for (SpaceEnergyReportData node : it.getNodes()) {
				int j = (int) beginInstant.until(node.getMoment().toInstant(), ChronoUnit.DAYS);
				result[j][i] = node.getInc();
			}
		}
		for (int i = 0; i < yAxis.length; ++i) {
			yAxis[i] = DateTimeFormatter.ISO_LOCAL_DATE.format(startTime.plusDays(i));
		}

		EnergyReport ret = new EnergyReport(xAxis, yAxis, result);
		ret.setTitle("建筑能耗报表  " + start + "-" + end);
		ret.setAnnotation("报表时间：" + DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()) + "      单位："
				+ data.get(0).getTreeNode().getUnit());
		return ret;
	}

	@Deprecated
	private List<SpaceEnergyReport> getSpaceEnergyReportData0(int parentSpace, Date start, Date end, String srcTable,
                                                              int catalog) {
		SimpleEntry<TreeNode, List<Integer>>[] group = this.getAllChildren(parentSpace);
		EnergyReportVo vo = new EnergyReportVo();
		vo.setSrcTable(srcTable);
		vo.setCatalog(catalog);
		vo.setStart(start);
		vo.setEnd(end);

		SpaceEnergyReport[] ret = Arrays.stream(group).map(it -> {
			vo.setSpaceIds(it.getValue());
			List<SpaceEnergyReportData> list = erm.getSpaceEnergyReport(vo);
			SpaceEnergyReport report = new SpaceEnergyReport();
			report.setNodes(list);
			report.setTreeNode(it.getKey());
			it.getKey().setNodes(null);
			return report;
		}).toArray(v -> new SpaceEnergyReport[v]);
		return Arrays.asList(ret);
	}

	@SuppressWarnings("unchecked")
	private SimpleEntry<TreeNode, List<Integer>>[] getAllChildren(int parentSpace) {
		List<TreeNode> spaceTree = ns.getAllSpaceTree(parentSpace);
		return spaceTree.stream().map(it -> new SimpleEntry<>(it, this.dfs(it, new ArrayList<>())))
				.toArray(v -> new SimpleEntry[v]);
	}

	private List<Integer> dfs(TreeNode root, List<Integer> result) {
		if (root == null)
			return result;

		result.add(root.getId());
		if (root.getNodes() == null || root.getNodes().size() == 0)
			return result;

		for (TreeNode node : root.getNodes()) {
			dfs(node, result);
		}
		return result;
	}
}

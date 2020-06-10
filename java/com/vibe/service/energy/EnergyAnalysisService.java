package com.vibe.service.energy;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.energy.EnergyReportMapper;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.asset.Probe;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.HandInputProbe;
import com.vibe.pojo.energy.CatalogTreeNode;
import com.vibe.pojo.energy.EnergyReport;
import com.vibe.pojo.energy.EnergyReport2;
import com.vibe.pojo.energy.EnergyReportVo;
import com.vibe.pojo.energy.SpaceEnergyReportData;
import com.vibe.scheduledtasks.report_statisticstask.StatisticsType;

@Service
public class EnergyAnalysisService {
    @Autowired
    private EnergyReportMapper erm;
    @Autowired
    private AssetStore as;
    @Autowired
    private EnergyReportService ers;

    @Autowired
    private SelectOptionDao sod;


    public EnergyReport2 getPerCapitaEnergyReport1(int deptno, Date start, Date end, StatisticsType srcTable, int catalog) {
        List<SpaceEnergyReportData> data = this.getPerCapitaEnergyReport1Data(deptno, start, end, srcTable, catalog);

        return getEnergyReport2("人均能耗", catalog, data, it -> srcTable.getDateDesc(it.getMoment()), it -> it.getInc());
    }

    private List<SpaceEnergyReportData> getPerCapitaEnergyReport1Data(int deptno, Date start, Date end, StatisticsType srcTable,
                                                                      int catalog) {
        EnergyReportVo vo = new EnergyReportVo();
        vo.setSrcTable(srcTable.destTable());
        vo.setCatalog(catalog);
        vo.setSpaceId(deptno);
        vo.setStart(start);
        vo.setEnd(end);
        return erm.getPerCapitaEnergyReport1(vo);
    }


    public List<CommonSelectOption> querySelectOptionList() {
        return sod.querySelectOptionList(0, 2200);
    }

    public EnergyReport2 getUnitAreaEnergyReport1(int spaceId, Date start, Date end, StatisticsType srcTable,
                                                  int catalog) {
        List<SpaceEnergyReportData> data = this.getUnitAreaEnergyReportData1(spaceId, start, end, srcTable.destTable(),
                catalog);

        return getEnergyReport2("单位面积能耗", catalog, data, it -> srcTable.getDateDesc(it.getMoment()), it -> it.getInc());
    }

    public EnergyReport2 getUnitAreaEnergyReport2(int spaceId, Date start, Date end, String srcTable, int catalog) {
        List<SpaceEnergyReportData> data = getUnitAreaEnergyReportData2(spaceId, start, end, srcTable, catalog);

        return getEnergyReport2("子空间能耗对比", catalog, data, it -> it.getSpaceCaption(), it -> it.getInc());
    }

    private List<SpaceEnergyReportData> getUnitAreaEnergyReportData1(int spaceId, Date start, Date end, String srcTable,
                                                                     int catalog) {
        EnergyReportVo vo = new EnergyReportVo();
        vo.setSrcTable(srcTable);
        vo.setCatalog(catalog);
        vo.setSpaceId(spaceId);
        vo.setStart(start);
        vo.setEnd(end);

        return erm.getUnitAreaEnergyReport1(vo);
    }

    private List<SpaceEnergyReportData> getUnitAreaEnergyReportData2(int spaceId, Date start, Date end, String srcTable,
                                                                     int catalog) {
        EnergyReportVo vo = new EnergyReportVo();
        vo.setSrcTable(srcTable);
        vo.setCatalog(catalog);
        vo.setSpaceId(spaceId);
        vo.setStart(start);
        vo.setEnd(end);

        return erm.getUnitAreaEnergyReport2(vo);
    }


    public EnergyReport2 getEquiEnergyReport(int probeId, Date start, Date end, StatisticsType srcTable) {
        List<SpaceEnergyReportData> data = getEquiEnergyReportDate(probeId, start, end, srcTable.destTable());

        Probe p = (Probe) as.findAsset(probeId);

        return getEnergyReport2("单个设备能耗", p.getMonitorKind(), data, it -> srcTable.getDateDesc(it.getMoment()), it -> it.getInc());
    }

    private List<SpaceEnergyReportData> getEquiEnergyReportDate(int probeId, Date start, Date end, String srcTable) {
        EnergyReportVo vo = new EnergyReportVo();
        vo.setSrcTable(srcTable);
        vo.setProbeId(probeId);
        vo.setStart(start);
        vo.setEnd(end);
        return erm.getEquiEnergyReport(vo);
    }

    public List<HandInputProbe> getProbeByCatalogs(int catalogId) {
        return erm.getProbeByCatalogs(dfs(getAllCatalog(catalogId), new ArrayList<Integer>()).toArray(new Integer[0]));
    }

    private List<Integer> dfs(CatalogTreeNode catalog, List<Integer> ret) {
        if (catalog == null)
            return ret;

        ret.add(catalog.getId());
        List<CatalogTreeNode> nodes = catalog.getNodes();
        if (nodes == null || nodes.size() == 0)
            return ret;

        for (CatalogTreeNode it : nodes) {
            dfs(it, ret);
        }
        return ret;
    }

    private CatalogTreeNode getAllCatalog(int parentId) {
        List<CatalogTreeNode> catalog = erm.getAllCatalog();
        HashMap<Integer, CatalogTreeNode> map = new HashMap<>();
        map.put(0, CatalogTreeNode.withNodes());

        for (CatalogTreeNode it : catalog) {
            CatalogTreeNode parent = map.get(it.getParentId());
            if (parent == null) {
                parent = CatalogTreeNode.withNodes();
                map.put(it.getParentId(), parent);
            }
            parent.getNodes().add(it);
            CatalogTreeNode old = map.put(it.getId(), it);
            it.setNodes(old != null ? old.getNodes() : new ArrayList<>());
        }
        return map.get(parentId);
    }


    public EnergyReport getIdleEnergyReport2(Date start, Date end, StatisticsType srcTable, int catalog) {
        EnergyReport data = ers.getCatalogEnergyReport(start, end, srcTable, catalog);
        if (data == null || data.getxAxis() == null || data.getxAxis().length == 0) {
            EnergyReport ret = new EnergyReport();
            ret.setTitle("待机能耗检测表 ");
            return ret;
        }
        String[] xAxis = data.getxAxis();
        String[] yAxis = data.getyAxis();
        Float[][] values = data.getData();

        Float[][] values2 = new Float[xAxis.length][];
        for (int i = 0; i < xAxis.length; ++i) {
            values2[i] = new Float[yAxis.length];
            for (int j = 0; j < yAxis.length; ++j) {
                values2[i][j] = values[j][i];
            }
        }
        EnergyReport ret = new EnergyReport(yAxis, xAxis, values2);
        ret.setAnnotation(erm.getUnit(catalog));
        ret.setTitle("待机能耗检测");
        return ret;
    }

    public EnergyReport getIdleEnergyReport1(Date start, Date end) {
        if (start == null) {
            OffsetDateTime now = OffsetDateTime.now();
            end = new Date(now.toEpochSecond() * 1000);
            start = new Date(now.minusDays(7).toEpochSecond() * 1000);
        }

        List<SpaceEnergyReportData> data = getIdleEnergyReportData(start, end);

        EnergyReport ret = new EnergyReport();
        ret.setTitle("待机能耗检测");
        ret.setAnnotation(erm.getUnit(34));
        if (data == null || data.isEmpty()) {
            return ret;
        }
        StatisticsType srcTable = StatisticsType.FLOAT_DAILY;
        OffsetDateTime startTime = srcTable.deadline(start.toInstant().atZone(ZoneId.systemDefault()));

        String[] xAxis = new String[srcTable.between(startTime.toInstant(), end.toInstant()) + 1];
        Float[][] values = new Float[xAxis.length][];

        for (int i = 0; i < xAxis.length; ++i) {
            xAxis[i] = DateTimeFormatter.ISO_LOCAL_DATE.format(startTime.plusDays(i));
            values[i] = new Float[24];
        }
        for (SpaceEnergyReportData it : data) {
            ZonedDateTime time = it.getMoment().toInstant().atZone(ZoneId.systemDefault());
            values[srcTable.between(startTime, time)][time.getHour()] = it.getInc();
        }
        String[] yAxis = IntStream.range(1, 24).mapToObj(v -> v + ":00").toArray(v -> new String[v]);
        ret.setData(values);
        ret.setxAxis(xAxis);
        ret.setyAxis(yAxis);
        return ret;
    }

    private List<SpaceEnergyReportData> getIdleEnergyReportData(Date start, Date end) {
        EnergyReportVo vo = new EnergyReportVo();
        vo.setStart(start);
        vo.setEnd(end);
        return erm.getIdleEnergyReport(vo);
    }


    private static final String[] emptyxAxis = new String[0];
    private static final Float[] emptyValues = new Float[0];

    private <T> EnergyReport2 getEnergyReport2(String title, int catalog, List<T> data, Function<T, String> getxAxis,
                                               Function<T, Float> getValue) {
        EnergyReport2 ret = new EnergyReport2();
        ret.setTitle(title);
        ret.setUnit(erm.getUnit(catalog));

        if (data == null || data.isEmpty()) {
            ret.setxAxis(emptyxAxis);
            ret.setValues(emptyValues);
            return ret;
        }

        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<Float> values = new ArrayList<>();
        for (T it : data) {
            xAxis.add(getxAxis.apply(it));
            values.add(getValue.apply(it));
        }
        ret.setxAxis(xAxis.toArray(emptyxAxis));
        ret.setValues(values.toArray(emptyValues));
        return ret;
    }
}

package com.vibe.controller.energy;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.HandInputProbe;
import com.vibe.pojo.energy.EnergyReport;
import com.vibe.pojo.energy.EnergyReport2;
import com.vibe.pojo.energy.EnergyReportSelector;
import com.vibe.scheduledtasks.report_statisticstask.StatisticsType;
import com.vibe.service.energy.EnergyAnalysisService;

@Controller
public class EnergAnalysisController {
    @Autowired
    private EnergyAnalysisService eas;


    @RequestMapping("/energy/getPerCapitaEnergyReport1")
    @ResponseBody
    public EnergyReport2 getPerCapitaEnergyReport1(EnergyReportSelector selector) throws Exception {
        return eas.getPerCapitaEnergyReport1(
                selector.getId(),
                selector.start(),
                selector.end(),
                selector.srcTable(),
                selector.getCatalogId());
    }


    @RequestMapping("/energy/getTopCatalog")
    @ResponseBody
    public List<CommonSelectOption> querySelectOptionList() {
        return eas.querySelectOptionList();
    }

    @RequestMapping("/energy/getUnitAreaEnergyReport1")
    @ResponseBody
    public EnergyReport2 getUnitAreaEnergyReport1(EnergyReportSelector selector) throws Exception {
        return eas.getUnitAreaEnergyReport1(
                selector.getParentSpace(),
                selector.start(),
                selector.end(),
                selector.srcTable(),
                selector.getCatalogId());
    }

    @RequestMapping("/energy/getUnitAreaEnergyReport2")
    @ResponseBody
    public EnergyReport2 getUnitAreaEnergyReport2(EnergyReportSelector selector) throws Exception {
        return eas.getUnitAreaEnergyReport2(
                selector.getParentSpace(),
                selector.start(),
                selector.end(),
                selector.srcTable().destTable(),
                selector.getCatalogId());
    }


    @RequestMapping("/energy/getProbeByCatalogs")
    @ResponseBody
    public List<HandInputProbe> getProbeByCatalogs(int catalog) {
        return eas.getProbeByCatalogs(catalog);
    }

    @RequestMapping("/energy/getEquiEnergyReport")
    @ResponseBody
    public EnergyReport2 getEquiEnergyReport(EnergyReportSelector selector) throws Exception {
        return eas.getEquiEnergyReport(selector.getId(), selector.start(), selector.end(), selector.srcTable());
    }

    @RequestMapping("/energy/getIdleEnergyReport1")
    @ResponseBody
    public EnergyReport getIdleEnergyReport1() {
        return eas.getIdleEnergyReport1(null, null);
    }

    @RequestMapping("/energy/getIdleEnergyReport2")
    @ResponseBody
    public EnergyReport getIdleEnergyReport2() {
        OffsetDateTime now = OffsetDateTime.now();
        Date end = new Date(now.toEpochSecond() * 1000);
        Date start = new Date(now.minusDays(7).toEpochSecond() * 1000);
        return eas.getIdleEnergyReport2(start, end, StatisticsType.FLOAT_DAILY, 34);
    }
}

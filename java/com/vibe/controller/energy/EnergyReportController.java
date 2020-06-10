package com.vibe.controller.energy;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.energy.EnergyReport;
import com.vibe.pojo.energy.EnergyReportSelector;
import com.vibe.service.energy.EnergyReportService;
import com.vibe.service.report.DownloadUtil;
import com.vibe.service.report.ExportType;

import net.sf.jasperreports.engine.JasperPrint;

@Controller
public class EnergyReportController {
    @Autowired
    private EnergyReportService ers;

    @RequestMapping("/energy/export")
    public void export(EnergyReportSelector selector, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        EnergyReport data = ers.handle(selector);
        if (data == null || data.getData() == null || data.getData().length == 0) {
            req.getRequestDispatcher("/html/energy/report-empty.html").forward(req, resp);
            return;
        }
        JasperPrint jasperPrint = ers.compileJasperPrint(req, data);

        ExportType exportType = ExportType.of("xlsx");
        OutputStream out = DownloadUtil.getDownloadStream(resp, data.getTitle(), exportType);
        exportType.writeAndClose(out, jasperPrint);
    }

    @RequestMapping("/energy/preview")
    public void preview(EnergyReportSelector selector, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        EnergyReport data = ers.handle(selector);
        if (data == null || data.getData() == null || data.getData().length == 0) {
            req.getRequestDispatcher("/html/energy/report-empty.html").forward(req, resp);
            return;
        }
        JasperPrint jasperPrint = ers.compileJasperPrint(req, data);

        ExportType exportType = ExportType.of("html");
        exportType.writeAndClose(resp.getOutputStream(), jasperPrint);
    }

    @RequestMapping("/energy/getCatalogEnergyReportData")
    @ResponseBody
    public EnergyReport getCatalogEnergyReport(EnergyReportSelector selector) throws Exception {
        return ers.handle(selector.withExec(1));
    }

    @RequestMapping("/energy/getSpaceEnergyReportData")
    @ResponseBody
    public EnergyReport getSpaceEnergyReport(EnergyReportSelector selector) throws Exception {
        return ers.handle(selector.withExec(2));
    }

    @RequestMapping("/energy/getEnergyReportData")
    @ResponseBody
    public Object getEnergyReportData(EnergyReportSelector selector) throws Exception {
        return ers._handle(selector);
    }
}

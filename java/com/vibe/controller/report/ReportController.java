package com.vibe.controller.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vibe.pojo.report.TemplateData;
import com.vibe.service.logAop.MethodLog;
import com.vibe.service.report.DownloadUtil;
import com.vibe.service.report.ExportType;
import com.vibe.service.report.ReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
public class ReportController {
    @Autowired
    ReportService reportService;

    @RequestMapping("/report/export")
    public void export(
            String template,
            String type,
            HttpServletResponse resp) throws IOException, JRException {
        JasperPrint jasperPrint = reportService.fillReport(template);
        ExportType exportType = ExportType.of(type);

        OutputStream out = DownloadUtil.getDownloadStream(resp, template, exportType);

        exportType.writeAndClose(out, jasperPrint);
    }

    @RequestMapping("/report/print")
    @MethodLog(remark = "print", option = "打印报表")
    public String print(
            String template,
            HttpServletRequest req) throws IOException, JRException {

        JasperPrint jasperPrint = reportService.fillReport(template);
        ExportType exportType = ExportType.valueOf("HTML");

        req.setAttribute("jasperPrint", jasperPrint);
        req.setAttribute("exportType", exportType);

        return "/report/print";
    }

    @RequestMapping("/report/add")
    public void add(Vo vo) throws IOException, JRException {
        vo.template.setMap(vo.data);
        reportService.saveTemplate(vo.template, vo.xml);
    }

    public static class Vo {
        private Map<String, Serializable> data;
        private TemplateData template;
        private String xml;

        public Map<String, Serializable> getData() {
            return data;
        }

        public void setData(Map<String, Serializable> data) {
            this.data = data;
        }

        public String getXml() {
            return xml;
        }

        public void setXml(String xml) {
            this.xml = xml;
        }

        public TemplateData getTemplate() {
            return template;
        }

        public void setTemplate(TemplateData template) {
            this.template = template;
        }

    }
}

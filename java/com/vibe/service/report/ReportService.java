package com.vibe.service.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.vibe.mapper.report.TemplateMapper;
import com.vibe.pojo.report.TemplateData;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService {
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private TemplateMapper tm;

//	@Autowired
//	ServletContext sc;


    public void saveTemplate(TemplateData templateData, String templateJRXML) throws IOException, JRException {
        compileReportToFile(templateData.getName(), templateJRXML);
        tm.add(templateData);
    }

    void compileReportToFile(String templateName, String templateJRXML) throws IOException, JRException {
        String fullname = getBaseDir() + templateName + ".jasper";
        try (ByteArrayInputStream in = new ByteArrayInputStream(templateJRXML.getBytes());
             FileOutputStream out = new FileOutputStream(fullname);) {
            JasperCompileManager.compileReportToStream(in, out);
        }
    }

    public JasperPrint fillReport(String templateName) throws JRException, FileNotFoundException {
        JasperReport jasperReport = getJasperReport(templateName);
        JRDataSource dataSource = getDataSource(templateName);
        Map<String, Object> parameters = getParameters(templateName);

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public JasperPrint fillReport(String templateName, List<?> dataSource, HashMap<String, Object> parms) throws FileNotFoundException, JRException {
        JasperReport jasperReport = getJasperReport(templateName);
        JRDataSource beanColDataSource = new JRBeanCollectionDataSource(dataSource);

        return JasperFillManager.fillReport(jasperReport, parms, beanColDataSource);
    }

    public JasperReport compileAndGetJasperReport(HttpServletRequest req, String templateName, int colnum, int rownum) throws Exception {
        String pathname = "/report-template.jsp/" + templateName + ".jrxml.jsp?col=" + colnum + "&row=" + rownum;
        byte[] jrxml = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "utf-8"))) {
            req.getRequestDispatcher(pathname).include(req, (HttpServletResponse) Proxy.newProxyInstance(
                    HttpServletResponse.class.getClassLoader(),
                    new Class[]{HttpServletResponse.class},
                    (self, method, args) -> {
                        if (method.getName().equals("getWriter")) return writer;
                        throw new UnsupportedOperationException("特殊用途，不支持其他方法");
                    }));
            writer.flush();
            out.flush();
            jrxml = out.toByteArray();
        } catch (Exception e) {
            throw new Exception("获取 jrxml 文件错误", e);
        }
        int st = -1;
        while ((++st < jrxml.length) && (jrxml[st] <= ' ')) ;
        if (st == jrxml.length) throw new Exception("未获取到 jrxml 文件: " + pathname);

        try (InputStream in = new ByteArrayInputStream(jrxml, st, jrxml.length - st)) {
            return JasperCompileManager.compileReport(in);
        }
    }

    public JasperReport getJasperReport(String templateName) throws JRException, FileNotFoundException {
        String fullname = getBaseDir() + templateName + ".jasper";
        File file = new File(fullname);
        if (!file.exists() && !file.isFile()) {
            throw new FileNotFoundException(fullname);
        }
        return (JasperReport) JRLoader.loadObject(file);
    }

    private String getBaseDir() {
//		return sc.getRealPath("/WEB-INF/report-template/");
        return "/WEB-INF/report-template/";
    }

    JRDataSource getDataSource(String template) {
        try {
            TemplateData t = tm.find(template);
            Class<?> clazz = Class.forName(t.getClazz());
            Method method = clazz.getMethod(t.getMethod(), Map.class);
            Object mapper = ctx.getBean(clazz);
            List<?> dataList = (List<?>) method.invoke(mapper, t.getMap());

            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
            return beanColDataSource;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Map<String, Object> getParameters(String templateName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("reporttime", new Date());
        return map;
    }

}

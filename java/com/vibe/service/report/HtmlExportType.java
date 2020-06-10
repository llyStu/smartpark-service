package com.vibe.service.report;

import java.io.OutputStream;
import java.io.Writer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

public class HtmlExportType implements IExportType {
	@Override
	public String getSuffix() {
		return "html";
	}

	@Override
	public void writeAndClose(OutputStream outputStream, JasperPrint print) throws JRException {
		HtmlExporter exporter = new HtmlExporter();

		ExporterInput exporterInput = new SimpleExporterInput(print);
		exporter.setExporterInput(exporterInput);

		SimpleHtmlExporterOutput out = new SimpleHtmlExporterOutput(outputStream);
		exporter.setExporterOutput(out);
		exporter.exportReport();
	}

	@Override
	public void writeAndClose(Writer writer, JasperPrint print) throws JRException {
		HtmlExporter exporter = new HtmlExporter();

		ExporterInput exporterInput = new SimpleExporterInput(print);
		exporter.setExporterInput(exporterInput);

		SimpleHtmlExporterOutput out = new SimpleHtmlExporterOutput(writer);
		exporter.setExporterOutput(out);
		exporter.exportReport();
	}
}

package com.vibe.service.report;

import java.io.OutputStream;
import java.io.Writer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class XlsxExportType implements IExportType {
	@Override
	public String getSuffix() {
		return "xlsx";
	}

	@Override
	public void writeAndClose(OutputStream outputStream, JasperPrint print) throws JRException {
		JRXlsxExporter exporter = new JRXlsxExporter();

		ExporterInput exporterInput = new SimpleExporterInput(print);
		exporter.setExporterInput(exporterInput);

		SimpleOutputStreamExporterOutput out = new SimpleOutputStreamExporterOutput(outputStream);
		exporter.setExporterOutput(out);
		exporter.exportReport();
	}

	@Override
	public void writeAndClose(Writer writer, JasperPrint print) throws JRException {
		throw new UnsupportedOperationException();
	}
}

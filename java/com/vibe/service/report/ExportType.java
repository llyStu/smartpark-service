package com.vibe.service.report;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Writer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public enum ExportType implements IExportType {
    XLSX(new XlsxExportType()), HTML(new HtmlExportType());

    private IExportType handler;

    ExportType(IExportType handler) {
        this.handler = handler;
    }

    @Override
    public String getSuffix() {
        return handler.getSuffix();
    }

    @Override
    public void writeAndClose(OutputStream outputStream, JasperPrint print) throws JRException {
        handler.writeAndClose(outputStream, print);
    }

    @Override
    public void writeAndClose(Writer writer, JasperPrint print) throws JRException {
        handler.writeAndClose(writer, print);
    }

    public static ExportType of(String name) {
        return ExportType.valueOf(name.trim().toUpperCase());
    }
}

interface IExportType {
    String getSuffix();

    void writeAndClose(Writer writer, JasperPrint print) throws JRException;

    void writeAndClose(OutputStream outputStream, JasperPrint print) throws JRException;

    default void writeFile(String filename, JasperPrint print) throws JRException, FileNotFoundException {
        writeAndClose(new FileOutputStream(filename), print);
    }

}

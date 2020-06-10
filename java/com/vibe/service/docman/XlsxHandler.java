package com.vibe.service.docman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.vibe.service.docman.xlsx.ToHtml;

public class XlsxHandler extends ConvertHandler {
    @Override
    public void convertToHtml(File src, File dest) throws FileNotFoundException, IOException {
        try (InputStream in = new FileInputStream(src);
             Writer out = new OutputStreamWriter(new FileOutputStream(dest))) {
            ToHtml toHtml = ToHtml.create(in, out);
            toHtml.setCompleteHTML(true);
            toHtml.printPage();
        }
    }
}

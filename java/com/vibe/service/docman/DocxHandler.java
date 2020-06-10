package com.vibe.service.docman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import fr.opensagres.poi.xwpf.converter.xhtml.Base64EmbedImgManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;

public class DocxHandler extends ConvertHandler {
	@Override
	public void convertToHtml(File src, File dest) throws Exception {
		try (XWPFDocument document = new XWPFDocument(new FileInputStream(src));
				OutputStream out = new FileOutputStream(dest)) {
			XHTMLOptions options = XHTMLOptions.create().setImageManager(new Base64EmbedImgManager());
			XHTMLConverter.getInstance().convert(document, out, options);
		}
	}

}

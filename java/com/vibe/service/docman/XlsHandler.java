package com.vibe.service.docman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

public class XlsHandler extends ConvertHandler {
	@Autowired
	@Override
	public void convertToHtml(File src, File dest) throws Exception {
		ExcelToHtmlConverter converter = new ExcelToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		
		try (HSSFWorkbook excel = new HSSFWorkbook(new FileInputStream(src))) {
			converter.processWorkbook(excel);

			try (OutputStream out = new FileOutputStream(dest)) {
				serializer.transform(new DOMSource(converter.getDocument()), new StreamResult(out));
			}

//			List<HSSFPictureData> pics = excel.getAllPictures();
//			if (pics == null || pics.isEmpty()) return;
//			
//			for (HSSFPictureData pic : pics) {
//				byte[] data = pic.getData();
//				if (data == null || data.length == 0) continue;
//				FileUtils.writeByteArrayToFile(new File(""), data);
//				
//			}
		}
	}
}

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

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;

import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConstants;
import fr.opensagres.xdocreport.core.utils.Base64Utility;

public class DocHandler extends ConvertHandler {
    private static final String EMBED_IMG_SRC_PREFIX = XHTMLConstants.DATA_ATTR + ";base64,";

    @Override
    public void convertToHtml(File src, File dest) throws Exception {
        WordToHtmlConverter converter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        String root = dest.getAbsolutePath() + "_files";
        converter.setPicturesManager((picture, pictureType, suggestedName, widthInches, heightInches) -> {
            return EMBED_IMG_SRC_PREFIX + Base64Utility.encode(picture);
        });

        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        try (HWPFDocument doc = new HWPFDocument(new FileInputStream(src))) {
            converter.processDocument(doc);

            try (OutputStream out = new FileOutputStream(dest)) {
                serializer.transform(new DOMSource(converter.getDocument()), new StreamResult(out));
            }

            // List<Picture> pics = doc.getPicturesTable().getAllPictures();
            // if (pics == null) return;
            // for (Picture pic : pics) {
            // byte[] c = pic.getContent();
            // if (c == null && c.length == 0) continue;
            //
            // File filename = new File(root, pic.suggestFullFileName());
            // FileUtils.writeByteArrayToFile(filename, c);
            // }
        }
    }

}

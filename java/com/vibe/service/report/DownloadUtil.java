package com.vibe.service.report;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

public class DownloadUtil {

	public static OutputStream getDownloadStream(HttpServletResponse response, String filename, ExportType exportType) throws IOException {
		response.reset();
		filename =  URLEncoder.encode(filename,"utf8") +"."+ exportType.getSuffix();
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename  +"\"");
		return response.getOutputStream();
	}
}

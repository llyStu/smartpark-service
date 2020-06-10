package com.vibe.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JsonpController {
	@RequestMapping("/_jsonp/**")
	public void jsonp(String callback, Boolean _isString, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("application/json;charset=UTF-8");
		try (ServletOutputStream out = resp.getOutputStream()) {
			out.print(callback);
			out.print('(');
			if (_isString != null && _isString) out.print('"');
			resp.flushBuffer();
			req.getRequestDispatcher(getPath(req)).include(req, resp);
			if (_isString != null && _isString) out.print('"');
			out.print(')');
			resp.flushBuffer();
		}
	}
	

	private final Pattern pathParm = Pattern.compile("/[^/\\\\]+/_jsonp(/.*)?");

	private String getPath(HttpServletRequest req) throws UnsupportedEncodingException {
		String uri = req.getRequestURI();
		Matcher mat = pathParm.matcher(uri);
		String path = mat.find() ? mat.group(1) : null;

		if (path != null)
			path = URLDecoder.decode(path, "utf-8");
		return path;
	}
}

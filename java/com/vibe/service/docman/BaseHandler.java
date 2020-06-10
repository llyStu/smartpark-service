package com.vibe.service.docman;

import java.io.UnsupportedEncodingException;

public interface BaseHandler extends ResourceHandler {
	
	String jsp();
	
	default String getContent(String path) throws UnsupportedEncodingException {
		return "docman/resource" + path;
	}

	@Override
	default Dispatcher prepare(String path) {
		return (req, resp) -> {
			req.setAttribute("arg","localhost:9001/vibe-web/"+ getContent(path));
			System.out.println("arg====="+getContent(path));
//			req.getRequestDispatcher("/WEB-INF/jsp/docman/" + jsp() +".jsp").forward(req, resp);
			req.getRequestDispatcher("/docman/" + jsp() +".jsp").forward(req, resp);
		};
	}

}


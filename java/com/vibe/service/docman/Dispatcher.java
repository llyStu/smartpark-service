package com.vibe.service.docman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Dispatcher {

	void forward(HttpServletRequest req, HttpServletResponse resp) throws Exception;

}

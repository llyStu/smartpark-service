package com.vibe.service.docman;

public class DefaultResourceHandler implements ResourceHandler {
	enum Cause {
		Unsupported("不支持的文件类型"),
		NotFound("404 Not Found");
		
		private String msg;
		private Cause(String msg) {
			this.msg = msg;
		}
	}
	

	public Dispatcher prepare(Cause cause) {
		return this.prepare(cause.msg);
	}

	@Override
	public Dispatcher prepare(String cause) {
		return (req, resp) -> {
			req.setAttribute("arg", cause);
//			req.getRequestDispatcher("/WEB-INF/jsp/docman/show-err.jsp").forward(req, resp);
			req.getRequestDispatcher("/docman/show-err.jsp").forward(req, resp);
		};
	}

}

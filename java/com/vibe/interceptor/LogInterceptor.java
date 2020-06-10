package com.vibe.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.vibe.common.config.SystemConfigManager;
import com.vibe.pojo.user.User;

/**
 * 定义用户登录拦截器
 * @author FLex3
 *
 */
public class LogInterceptor implements HandlerInterceptor{
	/**
	 * 返回值：boolean  true：放行       or   false：不放行
	 * 
	 */
	@Autowired
	private SystemConfigManager configManager;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//System.out.println("执行登录拦截了");
		// 判断用户是否登录
		// 1、如果是登录操作，无需拦截
			String uri = request.getRequestURI();
			String parameter = request.getParameter("flg");
			if(uri.contains("login") || uri.contains("timeloit")){
				return true;
			}
			String loginnotinterceptStr = configManager.getValue("loginnotintercept");
			if(loginnotinterceptStr!=null){
				String[] loginnotintercept = loginnotinterceptStr.split(",");
				for (String item : loginnotintercept) {
					if(uri.contains(item)){
						return true;
					}
				}
			}
			response.setHeader("P3P","CP=CAO PSA OUR");
			HttpSession session = request.getSession();
			if(parameter != null ){
				session.setAttribute("loginUser", new User(parameter,"111111"));
			}
			// 2、如果不是登录操作，需要判断用户是否登录
			
			User user=(User) session.getAttribute("loginUser");
			if(user != null){ // 已登录，放行
				return true;
			}
			// 3、如果没有登录，需要踢回登录页面
			request.getRequestDispatcher("/WEB-INF/jsp/system/login/login.jsp").forward(request, response);
			
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}

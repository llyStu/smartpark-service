package com.vibe.controller.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vibe.client.ClientLoginUser;
import com.vibe.common.Application;
import com.vibe.common.LoginSuccessCallback;
import com.vibe.pojo.user.User;
import com.vibe.service.logAop.MethodLog;


@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	private int flag = 0;

	@Autowired
	private Application application;

	// 客户端登陆
	@RequestMapping("/login/{name}/{password}")
	@MethodLog(remark="cllogin",option="用户登录")
	public @ResponseBody ClientLoginUser clllogin(
			@PathVariable String name, @PathVariable String password,
			@RequestParam(defaultValue = "int")String flag,
			@RequestParam(defaultValue = "1")int loginSrc,HttpSession session,HttpServletResponse httpServletResponse) {
		String message = doLogin(name, password);

		ClientLoginUser loginUser = new ClientLoginUser();
		if (message != null) {
			// 用户名或密码错误
			loginUser.setResult(false);
			return loginUser;
		}else{
			User queryUser = (User)session.getAttribute("loginUser");
			queryUser.setLoginSrc(loginSrc);
			queryUser.setFlag(flag);
			session.setAttribute("loginUser", queryUser);
			loginUser.setUser(queryUser);
			loginUser.setResult(true);
			loginUser.setSetCookie(httpServletResponse.getHeader("Set-Cookie"));
			return loginUser;
		}

	}
	@RequestMapping("/user/permission")
	@ResponseBody
	public boolean[] permission(String permissions){
		Subject subject = SecurityUtils.getSubject();
		if(permissions != null){
			String[] split = permissions.split(",");
			boolean[] permitted = subject.isPermitted(split);
			return permitted;
		}
		return null;
	}
	@RequestMapping("/user/roles")
	@ResponseBody
	public boolean[] role(String roles){
		Subject subject = SecurityUtils.getSubject();
		ArrayList<String> list = new ArrayList<String>();
		if(roles != null){
			String[] split = roles.split(",");
			for (String role : split) {
				list.add(role);
			}
		}
		boolean[] role=subject.hasRoles(list);
		for (boolean b : role) {
			System.out.println(b+"=");
		}
		return role;
	}
	private String doLogin(String name, String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(name, password);
		Subject subject = SecurityUtils.getSubject();

		String message = null;
		try {
			subject.login(token);
			for (LoginSuccessCallback loginSuccessCallback : application.getLoginSuccessCallbackList()) {
				loginSuccessCallback.success();
			}
			message = null;
		} catch (UnknownAccountException | IncorrectCredentialsException e1) {
			message = "用户名或密码错误";
		} catch (ExcessiveAttemptsException e) {
			message = "超过了尝试登录的次数。";
		} catch (AuthenticationException e) { // 其他错误
			e.printStackTrace();
			if (e.getMessage() != null)
				message = "发生错误：" + e.getMessage();
			else
				message = "发生错误，无法登录。";
		}
		return message;
	}

	@Value("${home}")
	private String home;
	// 网页端用户登录
	@RequestMapping("/user/login")
	@MethodLog(remark="login",option="用户登录")
	public String login(HttpSession session, Model model, User user) {
		if (session.getAttribute("loginUser") != null) {
			return home;
		}
		if (user.getLogin_id() == null && user.getPassword() == null) {
			return "/system/login/login";
		}
		String password = user.getPassword();
		String message = doLogin(user.getLogin_id(), password);
		if (message != null) {
			model.addAttribute("message", message);
			model.addAttribute("password", password);
			model.addAttribute(user);
			return "/system/login/login";
		}
		User queryUser = (User)session.getAttribute("loginUser");
		session.setAttribute("loginUser", queryUser);
		return home;
	}

	// 退出登录
	@RequestMapping("/user/out")
	@MethodLog(remark="logout",option="用户退出")
	public String logout(HttpSession session) {

		session.removeAttribute("loginUser");
		System.out.println("退出登录了");
		return "/system/login/login";
	}

	@RequestMapping("/loginSrc")
	public @ResponseBody Integer headFlag(HttpSession session) {
		if (session.getAttribute("loginUser") == null) {
			return 0;
		}else{
			User user =(User)session.getAttribute("loginUser");
			return user.getLoginSrc();
		}
	}




	private final Pattern re = Pattern.compile("jquery(-[.\\d]+)(.min)?");

	@RequestMapping("/ext_libs/jQuery/{filename}.js")
	public void injectModulePathInAjax(@PathVariable String filename, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		String filepath = req.getServletContext().getRealPath("/ext_libs/jQuery/" + filename +".js");
		File jquery = null;
		if (filepath == null || (jquery = new File(filepath)) == null || !jquery.isFile()) {
			req.getRequestDispatcher(filepath).forward(req, resp);
			return;
		}
		resp.setContentType("Content-Type: application/javascript");
		resp.setDateHeader("Last-Modified", jquery.lastModified());
//		resp.setDateHeader("Expires", jquery.lastModified() + 20000);
		resp.setHeader("Cache-Control", "public");
		resp.setHeader("Pragma", "Pragma");

		try (PrintWriter out = new PrintWriter(resp.getOutputStream())) {
			try (InputStream in = new FileInputStream(jquery)) {
				IOUtils.copy(in, out);
			}
			if (re.matcher(filename).matches()) {
				out.write(";(function() {\r\n" +
						"    var fnAjax = $.ajax;\r\n" +
						"    var reParms = /^(\\w+=[^&]*&?)*$/\r\n" +
						"\r\n" +
						"    $.ajax = function(settings) {\r\n" +
						"        if (settings.data === undefined || settings.data === null) {\r\n" +
						"            settings.data = {};\r\n" +
						"        }\r\n" +
						"        var global = window;\r\n" +
						"        while (global.parent !== global.parent.parent)\r\n" +
						"            global = global.parent;\r\n" +
						"        if (typeof settings.data === 'object') {\r\n" +
						"			if (settings.data instanceof FormData) {\r\n" +
						"				settings.data.append('ATTACH_MODULE_PATH', global.location.href);\r\n" +
						"			} else {\r\n" +
						"				settings.data.ATTACH_MODULE_PATH = global.location.href;\r\n" +
						"			}\r\n" +
						"        } else if (typeof settings.data === 'string' && reParms.test(settings.data)) {\r\n" +
						"            settings.data += '&ATTACH_MODULE_PATH=' + encodeURIComponent(global.location.href);\r\n" +
						"        }\r\n" +
						"        return fnAjax.call(this, settings);\r\n" +
						"    }\r\n" +
						"    ;\r\n" +
						"}\r\n" +
						")();");
			}
		}
	}
}

package com.vibe.controller.global;

import com.vibe.pojo.MenuBean;
import com.vibe.service.global.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MenuController {
	@Autowired
	private MenuService menuService;

	@RequestMapping("/getMenus")
	@ResponseBody
//	@MethodLog(remark="insert",option="获取菜单")
	public MenuBean getMenus(HttpServletRequest request){
		return menuService.getMenus(request);
	}

}

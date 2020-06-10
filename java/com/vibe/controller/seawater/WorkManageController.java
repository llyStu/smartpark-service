package com.vibe.controller.seawater;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.seawater.WorkPattern;
import com.vibe.pojo.user.Role;
import com.vibe.pojo.user.User;
import com.vibe.service.seawater.WorkSimulatorService;
import com.vibe.service.user.UserService;

@Controller
public class WorkManageController {
	
	@Autowired
	private WorkSimulatorService workSimulatorService;
	@Autowired
	private UserService userService;

	@RequestMapping("/WorkManage/findAllWorkPatternName")
	@ResponseBody
	public List<WorkPattern> findAllWorkPatternName(HttpServletRequest request){//查询所有工况
		User user = (User)request.getSession().getAttribute("loginUser");
		List<Role> allRole = userService.getAllRole(user.getId());
		for (Role role : allRole) {
			if(role.getId()==4){
				user=new User();
			}
		}
		return workSimulatorService.findAllWorkPatternName(user);
	}
	
	@RequestMapping("/WorkManage/findWorkPatternCheckIds")
	@ResponseBody
	public List<WorkPattern> findWorkPatternCheckId(int[] ids){
		return workSimulatorService.findWorkPatternCheckId(ids);
	}
	
}

package com.vibe.service.wsservice;


import com.vibe.pojo.user.User;
import com.vibe.pojo.user.User;
import com.vibe.util.constant.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName UserWSService
 * @Description
 * @return null
 *
 * @Version 1.0
 * @Date 2019/9/15 2:36
 * @Author zhsili81@gmail.com
 */
@FeignClient(name = "user-service")
public interface UserWSService {

	/**
	 * 保存用户信息，并获得查询结果
	 *
	 * @return <HashMap<String,List<User>>>
	 */
	@RequestMapping(value = "/user/list/info", method = RequestMethod.GET)
	ResponseModel<HashMap<String,List<User>>> listUsersByUserString(@RequestParam(name = "userString") String userString);

	/**
	 * 获得此用户的所有同组人员信息
	 *
	 * @param ldapNo
	 * @return <HashMap<String,List<User>>>
	 */
	@RequestMapping(value = "/user/list/info/bysameorg", method = RequestMethod.GET)
	ResponseModel<HashMap<String,List<User>>> listUserBySameOrg(@RequestParam(name = "ldapNo") String ldapNo);


	/**
	 * 根据用户名获取用户信息，并更新用户erp信息,同步erp查询
	 *
	 * @return  <HashMap<String, User>>
	 */
	@RequestMapping(value = "/user/name/{ldapNo}/syncerp", method = RequestMethod.GET)
	ResponseModel<User> getUserByLdapNo(@PathVariable(name = "ldapNo") String ldapNo);

}

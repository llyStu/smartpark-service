package com.vibe.shiro;


import com.vibe.common.Application;
import com.vibe.common.LoginSuccessCallback;
import com.vibe.pojo.user.User;
import com.vibe.service.wsservice.UserWSService;
import com.vibe.util.constant.ResponseModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;


public class MyShiroRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

	@Autowired
	private UserWSService userWSService;
	@Autowired
	private Application application;
	/**
	 * 登录认证
	 */

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		// UsernamePasswordToken对象用来存放提交的登录信息
		logger.info("登录认证");
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

		ResponseModel<User> result = userWSService.getUserByLdapNo(token.getUsername());

		User user = result.getData();
		if(null != user){
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute("loginUser",user);
		}
		//为AssetBing对象添加默认的初始化数据
		for (LoginSuccessCallback loginSuccessCallback : application.getLoginSuccessCallbackList()) {
			loginSuccessCallback.success();
		}
		// 生成认证用户
		return new SimpleAuthenticationInfo(user, new String(token.getPassword()), getName());
	}

	/**
	 * 权限认证，为当前登录的Subject授予角色和权限
	 *
	 * @see ：本例中该方法的调用时机为需授权资源被访问时
	 * @see ：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
	 * @see ：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-
	 *      shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
	 */

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		logger.info("##################执行Shiro权限认证##################");
		try {
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			if (user != null) {
				// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

				Set<String> roleNames = new HashSet<String>();


				/*if ("Y".equals(user.getIsAdmin())) {
					roleNames.add("admin");
				}
*/
				// 将角色名称提供给info
				info.setRoles(roleNames);

				// 用户的book[member]和book[manager]权限
				//logger.info("已为用户[" + user.getLdapNo() + "]赋予了[" + info.getRoles() + "]权限");
				return info;
			}

		} catch (Exception e) {
			logger.error("执行Shiro权限认证异常！", e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		// 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
		return null;
	}
}


package com.vibe.shiro;

import com.alibaba.fastjson.JSONObject;
import com.vibe.util.constant.HttpRequestUtils;
import com.vibe.util.constant.PropertiesValue;
import com.vibe.util.constant.QueryStringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

//import com.sun.deploy.net.URLEncoder;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);

    /**
     * (non-Javadoc)
     *
     * @org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess (org.apache.shiro.authc.AuthenticationToken,
     *org.apache.shiro.subject.Subject, javax.servlet.ServletRequest,
     *javax.servlet.ServletResponse)
     * @see
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        //logger.info("onLoginSuccess");
        WebUtils.getAndClearSavedRequest(request);

        WebUtils.redirectToSavedRequest(request, response, "");

        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // TODO Auto-generated method stub
        if (SecurityUtils.getSubject().getPrincipal() == null) {
            return false;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * (non-Javadoc)
     *
     * @org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onAccessDenied (javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     * @see
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        //logger.info("onAccessDenied");
        System.out.println("url" + ((HttpServletRequest) request).getRequestURI());
		/*if (StringUtils.equals("/oaApi/user/mobileLogin",((HttpServletRequest) request).getRequestURI())){
			return true;
		}*/
        if (((HttpServletRequest) request).getRequestURI().contains("/image")) {
            return true;
        }
        if (((HttpServletRequest) request).getRequestURI().contains("timeloit") ||
                ((HttpServletRequest) request).getRequestURI().contains("/spacemodel")) {
            return true;
        }
        if (((HttpServletRequest) request).getRequestURI().contains("socket")) {
            return true;
        }
        if (StringUtils.equals(((HttpServletRequest) request).getRequestURI(), "/energy/imEnergyMeter")) {
            return true;
        }
        System.out.println(((HttpServletRequest) request).getParameter("sso_token"));
        String requestedWith = ((HttpServletRequest) request).getHeader("X-Requested-With");
        String requestedInPage = ((HttpServletRequest) request).getHeader("X-Requested-InPage");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith) && StringUtils.isNotBlank(requestedInPage)) {
            logger.info("XMLHttpRequest重定向到sso登录页面");
            String redirectURL = StringUtils.substringBefore(requestedInPage, "?");
            String queryString = StringUtils.substringAfter(requestedInPage, "?");
            queryString = QueryStringUtil.removeParam(queryString, "sso_token");
            redirectURL = StringUtils.isBlank(queryString) ? redirectURL : redirectURL + "?" + URLEncoder.encode(queryString, "UTF-8");

            String loginUrl = PropertiesValue.getSSO_LOGIN_URL() + "?forward=" + URLEncoder.encode(redirectURL, "UTF-8");
            ((HttpServletResponse) response).addHeader("X-SSO-Redirect", loginUrl);
            ((HttpServletResponse) response).setStatus(401);
            return false;
        } else {
            Enumeration<String> em = ((HttpServletRequest) request).getParameterNames();
            String redirectURL = ((HttpServletRequest) request).getRequestURL().toString();
            while (em.hasMoreElements()) {
                String queryString = ((HttpServletRequest) request).getQueryString();
                queryString = QueryStringUtil.removeParam(queryString, "sso_token");
                redirectURL = StringUtils.isBlank(queryString) ? redirectURL : redirectURL + "?" + URLEncoder.encode(queryString, "UTF-8");
                break;
            }

            logger.info("重定向到sso登录页面");
            setLoginUrl(PropertiesValue.getSSO_LOGIN_URL() + "?forward=" + redirectURL);
            return super.onAccessDenied(request, response);
        }

    }

    /**
     * (non-Javadoc)
     *
     * @org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginFailure (org.apache.shiro.authc.AuthenticationToken,
     *org.apache.shiro.authc.AuthenticationException,
     *javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     * @see
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        // TODO Auto-generated method stub
        return super.onLoginFailure(token, e, request, response);
    }

    /**
     * (non-Javadoc)
     *
     * @org.apache.shiro.web.filter.AccessControlFilter onPreHandle(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
     * @see
     */
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // TODO Auto-generated method stub
        //logger.info("onPreHandle");
        String ssoToken = request.getParameter("sso_token");
        if (StringUtils.isBlank(ssoToken)) {
            ssoToken = ((HttpServletRequest) request).getHeader("X-SSO-Token");
        }
        Subject user = SecurityUtils.getSubject();
        logger.info("ssoToken:" + ssoToken + "---user.isAuthenticated():" + user.isAuthenticated());
        if (ssoToken != null && !user.isAuthenticated()) {
            logger.info("ssoToken != null && !user.isAuthenticated(),进行sso verify");
            String ssoVerifyUrl = PropertiesValue.getSSO_VERIFY_URL() + "?sso_token=" + ssoToken;
            JSONObject obj = HttpRequestUtils.httpGet(ssoVerifyUrl);
            if (null != obj && "true".equals(obj.getString("success"))) {
                logger.info("sso verify校验成功，shiro进行登录认证");
                String userName = obj.get("username") + "";
                UsernamePasswordToken token = new UsernamePasswordToken(userName, "");
                SecurityUtils.getSubject().login(token);
            }
        }
        return super.onPreHandle(request, response, mappedValue);
    }

}
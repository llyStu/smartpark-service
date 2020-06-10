/**
 * 
 */
package com.vibe.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName PropertiesValue
 * @Description
 *
 * @Version 1.0
 * @Date 2019/9/15 2:37
 * @Author zhsili81@gmail.com
 */
@Component
public class PropertiesValue {

	public static String SSO_LOGIN_URL = "";

	public static String SSO_LOGOUT_URL = "";

	public static String SSO_VERIFY_URL = "";

	public static String SSO_FORWARD_URL = "";


	public static String getSSO_LOGIN_URL() {
		return SSO_LOGIN_URL;
	}

	@Value("${sso.login.url}")
	public void setSSO_LOGIN_URL(String sSO_LOGIN_URL) {
		SSO_LOGIN_URL = sSO_LOGIN_URL;
	}

	public static String getSSO_LOGOUT_URL() {
		return SSO_LOGOUT_URL;
	}

	@Value("${sso.logout.url}")
	public void setSSO_LOGOUT_URL(String sSO_LOGOUT_URL) {
		SSO_LOGOUT_URL = sSO_LOGOUT_URL;
	}

	public static String getSSO_VERIFY_URL() {
		return SSO_VERIFY_URL;
	}

	@Value("${sso.verify.url}")
	public void setSSO_VERIFY_URL(String sSO_VERIFY_URL) {
		SSO_VERIFY_URL = sSO_VERIFY_URL;
	}

	public static String getSSO_FORWARD_URL() {
		return SSO_FORWARD_URL;
	}

	@Value("${sso.forward.url}")
	public void setSSO_FORWARD_URL(String sSO_FORWARD_URL) {
		SSO_FORWARD_URL = sSO_FORWARD_URL;
	}

}

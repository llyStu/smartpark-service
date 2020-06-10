package com.vibe.client;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vibe.util.HttpClientPoolUtil;
import com.vibe.util.RSAUtils;
import com.vibe.util.constant.Constants;

/**
 * <p>Title:SdkClient</p>
 * <p>Description:客户端调用工具类</p>
 * <p>Company:浙江大华技术股份有限公司</p>
 *
 * @author 32174
 * @date 2018年12月18日
 */
public class DahuaClient {

    private static Map<String, String> headMap = new HashMap<String, String>();

    static {
        headMap.put("Content-Type", "application/json;charset=UTF-8");
    }

    /**
     * @return String
     * @throws
     * @Title:get
     * @Description:get请求
     * @Param @param url
     * @Param @return
     * @Param @throws Exception
     */
    public static String get(String url) throws Exception {

        return HttpClientPoolUtil.get(url, headMap);
    }

    /**
     * @return String
     * @throws
     * @Title:post
     * @Description:post请求
     * @Param @param url
     * @Param @param data JSON序列化后的参数 JSON.toJSONString(xxx)
     * @Param @return
     * @Param @throws Exception
     */
    public static String post(String url, String data) throws Exception {


        return HttpClientPoolUtil.post(url, data, headMap);
    }

    /**
     * @return String
     * @throws
     * @Title:put
     * @Description:put请求
     * @Param @param url
     * @Param @param data JSON序列化后的参数 JSON.toJSONString(xxx)
     * @Param @return
     * @Param @throws Exception
     */
    public static String put(String url, String data) throws Exception {
        return HttpClientPoolUtil.put(url, data, headMap);
    }

    /**
     * @return String
     * @throws
     * @Title:delete
     * @Description:delete请求
     * @Param @param url
     * @Param @param data
     * @Param @return
     * @Param @throws Exception
     */
    public static String delete(String url, String data) throws Exception {
        return HttpClientPoolUtil.delete(url, headMap);
    }

    /**
     * @return String
     * @throws
     * @Title:login
     * @Description:模拟登录调用,获取token
     * @Param @param url
     * @Param @param userName 用户名
     * @Param @param password 密码(明文)
     * @Param @return
     * @Param @throws Exception
     */
    public static String login(String ip, String port, String userName, String password) throws Exception {
        String result = "";
        String publicKeyUrl = MessageFormat.format(Constants.GET_PUBLIC_KEY, ip, port);
        String loginUrl = MessageFormat.format(Constants.LOGIN, ip, port);
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("loginName", userName);
            String publicKeyResult = HttpClientPoolUtil.post(publicKeyUrl, JSON.toJSONString(paramMap), headMap);
            if ((publicKeyResult != null && !"".equals(publicKeyResult))) {
                JSONObject publicKeyResultObject = JSON.parseObject(publicKeyResult);
                String publicKey = publicKeyResultObject.getString("publicKey");
                if (publicKey == null || "".equals(publicKey)) {
                    throw new Exception("获取token失败");
                }
                String entryPassword = RSAUtils.encryptBASE64(RSAUtils.encryptByPublicKey(password.getBytes(), publicKey));
                paramMap.put("loginPass", entryPassword);
                result = HttpClientPoolUtil.post(loginUrl, JSON.toJSONString(paramMap), headMap);
            }
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

}

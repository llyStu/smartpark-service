package com.vibe.util.constant;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;


/** 
 * @ClassName HttpRequestUtils
 * @Description 
 * @param null 
 * @return null
 *
 * @Version 1.0
 * @Date 2019/9/15 2:37
 * @Author zhsili81@gmail.com
 */
public class HttpRequestUtils {
	/**
	 *  日志记录
	 */
	public static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

	/**
	 * httpPost
	 * 
	 * @param url
	 *            路径
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam) {
		return httpPost(url, jsonParam, false);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
		// post请求返回结果
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		JSONObject jsonResult = null;
		HttpPost method = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000).build();
		method.setConfig(requestConfig);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = closeableHttpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/** 把json字符串转换成json对象 **/
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				logger.error("资源关闭异常:" + e.getMessage(), e);
			}
		}
		return jsonResult;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            路径
	 * @return
	 */
	public static JSONObject httpGet(String url) {
		// get请求返回结果
		JSONObject jsonResult = null;
		CloseableHttpClient closeableHttpClient = null;
		try {
			// 创建HttpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// HttpClient
			closeableHttpClient = httpClientBuilder.build();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000).setConnectionRequestTimeout(1000).setSocketTimeout(2000).build();
			request.setConfig(requestConfig);
			HttpResponse response = closeableHttpClient.execute(request);

			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				String data = EntityUtils.toString(response.getEntity());
				data = data.replaceAll("%", "%25");
				data = data.replaceAll("\\+", "%2B");
				String strResult = URLDecoder.decode(data, "UTF-8");
				strResult = URLDecoder.decode(strResult, "UTF-8");
				/** 把json字符串转换成json对象 **/
				jsonResult = JSONObject.parseObject(strResult);
			} else {
				logger.error("get请求提交失败:" + url);
			}
			request.releaseConnection();
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					logger.error("资源关闭异常:" + e.getMessage(),e);
				}
			}
		}
		return jsonResult;
	}
}

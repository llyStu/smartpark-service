package com.vibe.util.constant;

/**
 * @ClassName QueryStringUtil
 * @Description
 * @Version 1.0
 * @Date 2019/9/15 2:38
 * @Author zhsili81@gmail.com
 */
public class QueryStringUtil {

    public static String removeParam(String queryString, String paramName) {
        if (queryString != null) {
            String[] strArray = queryString.split("&");
            StringBuffer newQueryString = new StringBuffer();
            for (String str : strArray) {
                if (!str.startsWith(paramName + "=")) {
                    newQueryString.append("&");
                    newQueryString.append(str);
                }
            }
            queryString = newQueryString.length() > 1 ? newQueryString.toString().substring(1) : "";
        }
        return queryString;
    }
}

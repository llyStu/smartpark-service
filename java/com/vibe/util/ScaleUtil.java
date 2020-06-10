package com.vibe.util;

import java.math.BigDecimal;

import com.vibe.common.config.SystemConfigManager;
import com.vibe.scheduledtasks.AssetStoreSqlSession;

/**
 * 配置全局保留小数点位数
 * @return
 * @Author: lxx-nice@163.com
 * @Create: 11:10 2020/5/7
 */
public class ScaleUtil {

    private static SystemConfigManager configManager;
    private static String scale;

    static {
        ScaleUtil.configManager = AssetStoreSqlSession.getConfigManager();
        //decimal.scale 数据库中配置
        scale = configManager.getValue("decimal.scale");
        if("".equals(scale)) {
            scale = "2";
        }
    }
    //newScale为小数点位数
    public static Double roundValue(Double d) {

        Double retValue = null;
        if (d != null) {
            BigDecimal bd = new BigDecimal(d);
            retValue = bd.setScale(Integer.parseInt(scale),BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return retValue;
    }

    public static String getRate(Object d) {
        return String.format("%."+scale+"f", d);
    }
    public static String getRateStr(Object d) {
        Double retValue = null;
        if (d != null) {
            BigDecimal bd = new BigDecimal(String.valueOf(d));
            retValue = bd.setScale(Integer.parseInt(scale),BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return retValue.toString();
    }

    public static Double roundObject(Object o) {
        Double retValue = null;
        if (o != null) {
            BigDecimal bd = new BigDecimal(String.valueOf(o));
            retValue = bd.setScale(Integer.parseInt(scale),BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return retValue;
    }

    /**
     * object类型转换为double类型
     * @param o
     * @return
     */
    /*public static Double objectToDouble(Object o) {
        Double retValue = null;
        if (o != null) {
            BigDecimal bd = new BigDecimal(String.valueOf(o));
            retValue = bd.setScale(Integer.parseInt(scale),BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return retValue;
    }*/
    public static Float roundFloat(Object o) {
        Float retValue = null;
        if (o != null) {
            BigDecimal bd = new BigDecimal(String.valueOf(o));
            retValue = (float)bd.setScale(Integer.parseInt(scale),BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return retValue;
    }
}

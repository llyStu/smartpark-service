package com.vibe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ClassName: ConfUtil
 * Package:com.cizing.office.config
 * Description: 用于在静态方法中获取配置文件中的参数
 *
 * @Date:2020/1/3 14:36
 * @Author: hyd132@136.com
 */
@Component
public class ConfUtil {

    private  static  String imgStoreCatalogue;

    @Value("${imgStoreCatalogue}")
    private String pushImgStoreCatalogue;

    @PostConstruct
    public void getApiToken() {
        imgStoreCatalogue = this.pushImgStoreCatalogue;
    }

    public static String getPushSelectPrefix() {
        return imgStoreCatalogue;
    }
}

package com.vibe.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class PropertiesUtil {
    public static void main(String[] args) {
        String area = PropertiesUtil.getPropertiesValue("area");
        System.out.println(area);
    }

    public static String getPropertiesValue(String name) {
        Resource re = new ClassPathResource("personAndArea.properties");
        Properties p = null;
        try {
            p = PropertiesLoaderUtils.loadProperties(re);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = StringUtils.trim(p.getProperty(name));
        return value;

    }

    public static List<String> getPropertiesValueList(List<String> nameList) {

        Resource re = new ClassPathResource("personAndArea.properties");
        Properties p = null;
        try {
            p = PropertiesLoaderUtils.loadProperties(re);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> resultName = new ArrayList<String>();
        for (String name : nameList) {
            String value = StringUtils.trim(p.getProperty(name));
            resultName.add(value);
        }
        return resultName;
    }
}

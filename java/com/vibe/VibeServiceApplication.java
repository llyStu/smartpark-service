package com.vibe;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * u@Description:
 *
 * @author zhousili
 * @ClassName Main
 * @Date 2019年7月17日
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.vibe.mapper.**", "com.vibe.monitor.db.**", "com.vibe.common.db.**"})
@EnableScheduling
public class VibeServiceApplication {

    public static final Logger logger = LoggerFactory.getLogger(VibeServiceApplication.class);

    public static void main(String[] args) {
        logger.info("Spring Boot Beginning ...");
        SpringApplication.run(VibeServiceApplication.class, args);
    }


    // 配置mybatis的分页插件pageHelper
	/*@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("params", "countSql");
		properties.setProperty("support-methods-arguments", "true");
		properties.setProperty("pageSizeZero", "true");//pageSize=0,查询全部
		properties.setProperty("reasonable", "true");
		properties.setProperty("helper-dialect", "mysql"); // 配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}*/
	/*@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("pageSizeZero", "true");//pageSize=0,查询全部
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "mysql"); // 配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}*/

}
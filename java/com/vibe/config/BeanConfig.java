package com.vibe.config;

import com.vibe.service.global.navigation.NavigationServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: BeanConfig
 * Package:com.vibe.config
 * Description:
 *
 * @Date:2019/11/19 11:46
 * @Author: hyd132@136.com
 */
@Configuration
public class BeanConfig {

    @Bean(name = "navigationService")
    public NavigationServiceImpl navigationService() {
        return new NavigationServiceImpl();
    }
}

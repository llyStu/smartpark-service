package com.vibe.server;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.vibe.common.Application;
import com.vibe.common.code.CodeDictManager;
import com.vibe.common.config.SystemConfigManager;
import com.vibe.monitor.server.MonitorServer;
import com.vibe.service.asset.AssetBinding;
import com.vibe.service.system.PermissionService;

@Component
public class ServerLifeCycle implements InitializingBean, DisposableBean, ServletContextAware {
	static Logger logger = LogManager.getLogger(ServerLifeCycle.class);
	
	@Autowired
	private MonitorServer server;
	
	@Autowired
    PermissionService permissionService;
	
	@Autowired
	private CodeDictManager codeDicts;
	
	@Autowired
	private SystemConfigManager configManager;
	
	@Autowired
	private AssetBinding assetBinding;
	
	@Autowired
	private Application application;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Initilizing the vibe server ...");
		//ClassPathXmlApplicationContext beanFactory = new ClassPathXmlApplicationContext("/config/spring/beans.xml");
		//server = beanFactory.getBean("monitorServer", MonitorServer.class);
		//beanFactory.close();
		try {
			codeDicts.load();
			server.loadAssets();
			//permissionService.loadMenuPermission();
			application.addLoginSuccessCall(assetBinding);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		//codeDicts.dump();
		//server.getAssetStore().dump();
		String test = configManager.getValue("test");
		if(!"true".equals(test)){
			server.startUp();
		}
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("Finalizing the web listener ...");
		server.shutDown();
	}

}

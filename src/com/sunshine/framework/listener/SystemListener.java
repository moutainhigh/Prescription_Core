/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年6月26日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.framework.listener.SystemListener;
import com.sunshine.framework.config.SystemConfig;

/**
 * 系统初始化
 * @Package: com.sunshine.framework.listener
 * @ClassName: SystemListener
 * @Statement: <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SystemListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(SystemListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("System Configuration load start......");
		SystemConfig.loadSystemConfig();
		loadBusinessesXml();
		logger.info("System Configuration load end......");
	}

	/**
	 * 加载业务配置文件
	 */
	public void loadBusinessesXml() {
		// List<File> files = FileUtils.searchFiles(this.getClass().getClassLoader().getResource("/rules").getPath(),
		// new String[] { "xml" }, false);
		// for (File file : files) {
		// Businesses businesses = RuleConvertUtil.xml2Businesses(file);
		// SystemConfig.businessesMap.put(businesses.getCode(), businesses);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}
}

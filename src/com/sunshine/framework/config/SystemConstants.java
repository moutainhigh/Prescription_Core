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
package com.sunshine.framework.config;

/**
 * 系统常量
 * 
 * @Package: com.sunshine.framework.config
 * @ClassName: SystemConstants
 * @Statement:
 *             <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SystemConstants {
	/**
	 * 文件分隔符
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * 版本
	 */
	public static final String VERSION_TYPE_RC = "rc";
	public static final String VERSION_TYPE_DEV = "dev";
	public static final String VERSION_TYPE_DEF = "dev";

	public static final String FILE_CALSS_PATH = "classpath:/";

	public static final String FILE_SPLIT_CHAR = ",";

	public static final String FRAMEWORK_CONFIG_START_PREFIX = "framework_";
	public static final String FRAMEWORK_CONFIG_FILE_TYPE = ".properties";

	public static final String FRAMEWORK_CONFIG_DEF = "framework_dev.properties";
	/**
	 * 服务配置文件
	 */
	public static final String SERVICE_CONFIG = "service.properties";
	/**
	 * 接口配置文件
	 */
	public static final String INTERFACE_CONFIG = "provider_services/interface.properties";
}

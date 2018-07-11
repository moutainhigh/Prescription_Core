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

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.common.spring.config.CombaPropertyPlaceholderConfigurer;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;

/**
 * 系统参数
 * 
 * @Package: com.sunshine.framework.config
 * @ClassName: SystemConfig
 * @Statement: <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:于策
 * @modify Date:
 * @Why&What is modify:分为rc版和dev版的不同加载
 * @Version: 1.0
 */
public class SystemConfig {
	private static Logger logger = LoggerFactory.getLogger(SystemConfig.class);
	/**
	 * 系统参数集合
	 */
	private static Map<String, String> systemConfigMap = new HashMap<String, String>();

	public static String Host_IP;

	static {
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			Host_IP = address.getHostAddress().toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Host_IP = "该服务器没有设置IP地址";
		}
	}

	/**
	 * 业务规则集合
	 */

	private SystemConfig() {

	}

	/**
	 * 加载系统配置文件 modify by yuce 2015-8-1 改为使用spring的文件解析
	 */
	public static void loadSystemConfig() {
		CombaPropertyPlaceholderConfigurer propsConfig = SpringContextHolder.getBean(CombaPropertyPlaceholderConfigurer.class);

		try {
			Properties props = propsConfig.mergeProperties();
			systemConfigMap.putAll(propsConfig.convertPropsToMap(props));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("system params:{}", JSON.toJSONString(systemConfigMap));
		}
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public static void put(String key, String value) {
		systemConfigMap.put(key, value);
	}

	/**
	 * 根据key值返回value
	 * 
	 * @param key
	 * @return
	 */
	private static String getValue(String key) {
		return systemConfigMap.get(key);
	}

	/**
	 * 根据key值返回String类型的value.
	 */
	public static String getStringValue(String key) {
		return getValue(key);
	}

	/**
	 * 根据key值返回Integer类型的value.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static String getStringValue(String key, String defaultValue) {
		String value = getValue(key);
		return StringUtils.isNotBlank(value) ? value : defaultValue;
	}

	/**
	 * 根据key值返回Integer类型的value.如果都為Null或内容错误则抛出异常.
	 */
	public static Integer getIntegerValue(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 根据key值返回Integer类型的value.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 根据key值返回Double类型的value.如果都為Null或内容错误则抛出异常.
	 */
	public static Double getDoubleValue(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 根据key值返回Double类型的value.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static Double getDoubleValue(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 根据key值返回Boolean类型的value.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 */
	public static Boolean getBooleanValue(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 根据key值返回Boolean类型的value.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 */
	public static Boolean getBooleanValue(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
}

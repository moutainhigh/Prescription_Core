/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年6月26日</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.spring.config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.CollectionUtils;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.common.spring.config
 * @ClassName CombaPropertyPlaceholderConfigurer.java
 * @Description 扩展PropertyPlaceholderConfigurer 使应用中能使用spring的文件解析类库
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class CombaPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	public CombaPropertyPlaceholderConfigurer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Return a merged Properties instance containing both the loaded properties and properties set on this FactoryBean.
	 */
	@Override
	public Properties mergeProperties() throws IOException {
		Properties result = new Properties();

		if (this.localOverride) {
			// Load properties from file upfront, to let local properties override.
			loadProperties(result);
		}

		if (this.localProperties != null) {
			for (Properties localProp : this.localProperties) {
				CollectionUtils.mergePropertiesIntoMap(localProp, result);
			}
		}

		if (!this.localOverride) {
			// Load properties from file afterwards, to let those properties override.
			loadProperties(result);
		}

		return result;
	}

	public Map<String, String> convertPropsToMap(java.util.Properties props) {
		Map<String, String> propsMap = new HashMap<String, String>();
		Enumeration<?> propertyNames = props.propertyNames();
		do {
			if (!propertyNames.hasMoreElements()) {
				break;
			}
			java.lang.String propertyName = (java.lang.String) propertyNames.nextElement();
			java.lang.String propertyValue = props.getProperty(propertyName);
			propsMap.put(propertyName, propertyValue);

		} while (true);
		return propsMap;
	}
}

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
package com.sunshine.framework.cache.redis;

import org.apache.commons.lang.StringUtils;

/**
 * redis常量
 * 
 * @Package: com.sunshine.framework.cache.redis
 * @ClassName: RedisConstant
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
public class RedisConstant {
	/**
	 * redis 缓存key不存在返回的标志
	 */
	public static final String CACHE_KEY_NOT_EXIST = "nil";

	/**
	 * 判断缓存是否有值
	 * 
	 * @param jsonData
	 * @return true 有值 <br>
	 *         false 没有值
	 */
	public static boolean isHadValue(String jsonData) {
		boolean isHadValue = false;
		if (StringUtils.isNotBlank(jsonData) && !CACHE_KEY_NOT_EXIST.equalsIgnoreCase(jsonData)) {
			isHadValue = true;
		}
		return isHadValue;
	}
}

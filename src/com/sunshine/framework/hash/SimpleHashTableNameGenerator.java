/**
 * <html>
 * <body>
 *  <P>  Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2015-7-20</p>
 *  <p> Created by 无名子</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Package: com.sunshine.mobileapp.common
 * @ClassName: HashTableNameGenerator
 * @Statement:
 * 			<p>
 *             hash取模算法分表
 *          </p>
 * @JDK version used: 1.6
 * @Author: Yuce
 * @Create Date: 2015-7-20
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SimpleHashTableNameGenerator {
	private static final Logger logger = LoggerFactory.getLogger(SimpleHashTableNameGenerator.class);

	/**
	 * 得到hsah子表名
	 * </p>
	 * 
	 * @param tableName
	 *            原表名<br>
	 * @param splitKeyVal
	 *            hash取模的关键值(根据splitKeyVal值来取模)<br>
	 * @param subTableCount
	 *            要拆分子表总数<br>
	 * @param isNeedUniform
	 *            是否需要均匀散列<br>
	 * @return
	 */
	public static String getSplitTableName(String tableName, Object splitKeyVal, Integer subTableCount, Boolean isNeedUniform) {
		long hashVal = 0;
		if (splitKeyVal instanceof Number) {
			hashVal = Integer.parseInt(splitKeyVal.toString());
		} else {
			hashVal = splitKeyVal.toString().hashCode();
		}

		// 斐波那契（Fibonacci）散列
		if (isNeedUniform) {
			hashVal = ( hashVal * 2654435769L ) >> 28;
		}

		// 避免hashVal超出 MAX_VALUE = 0x7fffffff时变为负数,取绝对值
		hashVal = Math.abs(hashVal) % subTableCount;
		String hashTableName = tableName + "_" + ( hashVal + 1 );
		if (logger.isDebugEnabled()) {
			logger.debug("get {} hash table name: {}.", tableName, hashTableName);
		}
		return hashTableName;
	}
}

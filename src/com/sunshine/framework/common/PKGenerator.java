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
package com.sunshine.framework.common;

import java.util.UUID;

/**
 * @Package: com.sunshine.framework.common
 * @ClassName: PKGenerator
 * @Statement: <p>
 *             主键ID 生成器
 *             </p>
 * @JDK version used: 1.6
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class PKGenerator {
	/**
	 * 生成数据库主键ID值
	 * 
	 * @return
	 */
	public static String generateId() {
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "");
	}

	/*
	public static void main(String[] args) {
		for (int i = 0; i < 13; i++) {
			System.out.println(generateId());
		}
	}
	*/
}

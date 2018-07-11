/**
 * <html>
 * <body>
 *  <P>  Copyright 2017 阳光康众</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2016-4-8</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * @Package com.sunshine.common.utils
 * @ClassName CommonUtil
 * @Statement
 *            <p>
 *            </p>
 * @JDK version used: 1.7
 * @Author: 于策
 * @Create Date: 2016-4-8
 * @modify-Author:
 * @modify-Date:
 * @modify-Why/What:
 * @Version 1.0
 */
public class CommonUtil {
	private static String SYSTEM_REQUEST_PATH = "";

	/**
	 * 获取系统request Scheme
	 * 
	 * @return
	 */
	public static String getRequestContextPath(HttpServletRequest request) {
		return SYSTEM_REQUEST_PATH.concat(request.getContextPath());
	}

	/**
	 * 从request里获取参数的值
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestParam(HttpServletRequest request, String param) {
		String requestParam = request.getParameter(param);
		if (StringUtils.isBlank(requestParam) && request.getAttribute(param) != null) {
			requestParam = (String) request.getAttribute(param);
		}

		return requestParam;
	}

}

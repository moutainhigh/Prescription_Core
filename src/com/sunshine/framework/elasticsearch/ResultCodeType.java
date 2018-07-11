/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年5月9日</p>
 *  <p> Created by 51397</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.elasticsearch;

/**
 * @Project: yct
 * @Package: elasticsearch
 * @ClassName: ResultCodeType
 * @Description: <p>
 *               </p>
 * @JDK version used:
 * @Author: 天竹子
 * @Create Date: 2017年5月9日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ResultCodeType {

	/**
	 * 请求接口异常
	 */
	public static final String[] REQUEST_PARAM_ERROR = { "-1", "请求参数不合法" };
	/**
	 * 成功
	 */
	public static final String[] SUCCESS = { "0", "成功" };

	/**
	 * actionName
	 */
	public static final String ACTION_NAME = "actionName";

	/**
	 * 按照列名查询的时候不知道为什么要加上这个后缀，回头再仔细琢磨一下。
	 */
	public static final String SPTLIT_SUFFIX = ".keyword";

}

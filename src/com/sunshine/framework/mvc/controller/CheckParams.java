/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月15日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.mvc.controller
 * @ClassName CheckParams.java
 * @Description 后台校验参数信息
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class CheckParams implements Serializable {

	private static final long serialVersionUID = 5029944078684863514L;
	/**
	 * 校验参数
	 */
	private Map<String, Serializable> params = new HashMap<String, Serializable>();

	public CheckParams() {
		super();
	}

	/**
	 * @param params
	 */
	public CheckParams(Map<String, Serializable> params) {
		super();
		this.params = params;
	}

	/**
	 * @return the params
	 */
	public Map<String, Serializable> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, Serializable> params) {
		this.params = params;
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月14日</p>
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
 * @ClassName PageParams.java
 * @Description 分页表单信息
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class PageParams implements Serializable {

	private static final long serialVersionUID = -5819554419875365914L;
	/**
	 * 参数
	 */
	private Map<String, Object> params = new HashMap<>();

	private Map<String, Integer> sortParams = new HashMap<>();

	public PageParams() {
		super();
	}

	/**
	 * @param params
	 */
	public PageParams(Map<String, Object> params) {
		super();
		this.params = params;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Integer> getSortParams() {
		return sortParams;
	}

	public void setSortParams(Map<String, Integer> sortParams) {
		this.sortParams = sortParams;
	}
}

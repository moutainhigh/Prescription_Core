/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.vo;

public enum RespCode {
	/**
	 * 成功
	 */
	SUCCESS(1),
	/**
	 * 请求成功,无数据
	 */
	NO_DATA(0),
	/**
	 * 失败
	 */
	FAILURE(-1),
	/**
	 * 异常
	 */
	EXCEPTION(-2);

	private Integer value;

	private RespCode(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}

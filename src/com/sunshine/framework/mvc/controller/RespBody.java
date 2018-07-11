/**
 * <html>
 * <body>
 *  <P>  Copyright © 版权所有2016 广东天泽阳光康众医疗投资管理有限公司.</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2015年4月29日</p>
 *  <p> Created by 申午武</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.controller;

import java.io.Serializable;

/**
 * 统一的响应格式
 * 
 * @Package: com.sunshine.framework.mvc.controller
 * @ClassName: RespBody
 * @Statement:
 *             <p>
 *             </p>
 * @JDK version used:
 * @Author: 申姜
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class RespBody implements Serializable {

	private static final long serialVersionUID = 5905715228490291386L;
	/**
	 * 状态
	 */
	private StatusEnum status;
	/**
	 * 结果
	 */
	private Object result;
	/**
	 * 消息描述
	 */
	private String message;

	public RespBody() {
		super();
	}

	/**
	 * @param status
	 */

	public RespBody(StatusEnum status) {
		super();
		this.status = status;
	}

	public RespBody(StatusEnum status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public RespBody(StatusEnum status, Object result) {
		super();
		this.status = status;
		this.result = result;
	}

	public RespBody(StatusEnum status, Object result, String message) {
		super();
		this.status = status;
		this.result = result;
		this.message = message;
	}

	/**
	 * 结果类型信息
	 */
	public enum StatusEnum {
		OK, FAIL, ERROR
	}

	/**
	 * 添加成功结果信息
	 */
	public void addOK(String message) {
		this.message = message;
		this.status = StatusEnum.OK;
	}

	/**
	 * 添加成功结果信息
	 */
	public void addOK(Object result, String message) {
		this.message = message;
		this.status = StatusEnum.OK;
		this.result = result;
	}

	/**
	 * 添加失败信息
	 */
	public void addFail(Object result, String message) {
		this.message = message;
		this.status = StatusEnum.FAIL;
	}

	/**
	 * 添加错误消息
	 */
	public void addError(String message) {
		this.message = message;
		this.status = StatusEnum.ERROR;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

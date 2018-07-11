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

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2171196924584785598L;
	/**
	 * 相应码 参见enum RespCode
	 */
	@NotNull
	private Integer respCode;
	/**
	 * 结果
	 */
	private Object result;
	/**
	 * 消息描述
	 */
	private String message;

	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Response(Integer respCode, String message) {
		super();
		this.respCode = respCode;
		this.message = message;
	}

	public Response(Integer respCode, Object result) {
		super();
		this.respCode = respCode;
		this.result = result;
	}

	public Response(Integer respCode, Object result, String message) {
		super();
		this.respCode = respCode;
		this.result = result;
		this.message = message;
	}

	public Integer getRespCode() {
		return respCode;
	}

	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
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

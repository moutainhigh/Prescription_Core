/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.elasticsearch.validate;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * @Project: ChuFangLiuZhuan_PlatForm
 * @Package: elasticsearch
 * @ClassName: ValidateInfo.java
 * @Description: <p>
 *               验证client是否可用信息
 *               </p>
 * @JDK version used: 1.8
 * @Author: 于策/yu.ce@foxmail.com
 * @Create Date: 2017年4月5日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ValidateInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6156054324429978995L;

	private Date validateTime;
	private String validateIp;

	public ValidateInfo() {
		super();
		this.validateTime = new Date();
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			this.validateIp = addr.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.validateIp = "获取Ip地址失败.";
		}
	}

	public Date getValidateTime() {
		return validateTime;
	}

	public void setValidateTime(Date validateTime) {
		this.validateTime = validateTime;
	}

	public String getValidateIp() {
		return validateIp;
	}

	public void setValidateIp(String validateIp) {
		this.validateIp = validateIp;
	}
}

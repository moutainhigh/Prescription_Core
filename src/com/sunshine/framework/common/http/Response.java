/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月7日</p>
 *  <p> Created by Administrator</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.http;

import java.io.Serializable;

/**
 * 
 * @Project: prescription_clinic 
 * @Package: com.sunshine.framework.common.http
 * @ClassName: Response
 * @Description: <p>响应数据</p>
 * @JDK version used: 
 * @Author: 天竹子
 * @Create Date: 2017年9月7日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 6757958172396531662L;
	/**
	 * 响应结果编码
	 */
	private String resultCode;
	/**
	 * 响应结果描述
	 */
	private String resultMsg;
	/**
	 * 结果集
	 */
	private Object data;

	/**
	 * 
	 */
	public Response() {
		super();
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the resultMsg
	 */
	public String getResultMsg() {
		return resultMsg;
	}

	/**
	 * @param resultMsg the resultMsg to set
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

}

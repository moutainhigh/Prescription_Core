/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年6月26日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.email;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件回调信息
 * @Package: com.sunshine.framework.common.email
 * @ClassName: CallBackInfo
 * @Statement: <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class CallBackInfo implements Serializable {
	private static final long serialVersionUID = 5401971939678483405L;
	/**
	 * 发送状态
	 */
	private boolean status;
	/**
	 * 邮件发送时间
	 */
	private Date sentDate;
	/**
	 * 尝试次数,暂时无用
	 */
	@Deprecated
	private Integer attempts;

	public CallBackInfo() {

	}

	public CallBackInfo(boolean status, Date sentDate, Integer attempts) {
		this.status = status;
		this.sentDate = sentDate;
		this.attempts = attempts;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	@Deprecated
	public Integer getAttempts() {
		return attempts;
	}

	@Deprecated
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

}

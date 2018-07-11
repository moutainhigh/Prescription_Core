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

/**
 * 邮件附件
 * @Package: com.sunshine.framework.common.email
 * @ClassName: AttachmentInfo
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
public class AttachmentInfo implements Serializable {
	private static final long serialVersionUID = -638346101635261172L;
	/**
	 * 附件名称
	 */
	private String attachmentName;
	/**
	 * 附件地址
	 */
	private String attachmentPath;

	public AttachmentInfo() {

	}

	public AttachmentInfo(String attachmentName, String attachmentPath) {
		this.attachmentName = attachmentName;
		this.attachmentPath = attachmentPath;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

}

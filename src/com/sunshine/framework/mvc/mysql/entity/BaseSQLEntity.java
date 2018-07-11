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
package com.sunshine.framework.mvc.mysql.entity;

import com.sunshine.framework.mvc.entity.BaseEntity;

/**
 * entity基类
 * 
 * @Package: com.sunshine.framework.mvc.entity
 * @ClassName: BaseEntity
 * @Statement:
 *             <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class BaseSQLEntity extends BaseEntity {

	private static final long serialVersionUID = 2368417877456900821L;

	public BaseSQLEntity() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

}

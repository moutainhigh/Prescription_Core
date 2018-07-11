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
package com.sunshine.framework.mvc.mongodb.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.sunshine.framework.mvc.entity.BaseEntity;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package mongodb.entity
 * @ClassName BaseMongoEntity.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月31日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class BaseMongoEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1772706032833294100L;

	@JSONField(name = "_id")
	public final String getId() {
		return id;
	}

	@JSONField(name = "_id")
	public final void setId(String id) {
		this.id = id;
	}

}

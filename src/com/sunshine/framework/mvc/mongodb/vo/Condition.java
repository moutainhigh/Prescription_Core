/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月8日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.mongodb.vo;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;

/**
 * @Project Prescription_PlatForm
 * @Package com.sunshine.framework.mvc.mongodb.vo
 * @ClassName Condition.java
 * @Description 条件基类
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月8日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface Condition {
	public Bson buildConditionBson();

	public Document buildConditionDocument();

	public Condition createCondition();

	public Condition add(Condition condition);

	public Condition or(Condition condition);

	public QueryOperationalEnum getQueryOperator();
}

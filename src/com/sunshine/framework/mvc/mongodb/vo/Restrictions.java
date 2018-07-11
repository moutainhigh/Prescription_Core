/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月26日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.mongodb.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;

/**
 * @Project Prescription_PlatForm
 * @Package com.sunshine.framework.mvc.mongodb.vo
 * @ClassName Restrictions.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年9月26日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class Restrictions {
	/**
	 * @Description 主键id等于条件
	 * @param value
	 * @return
	 * @date 2017年9月26日
	 */
	public static Condition idEq(Object value) {
		return new QueryCondition("id", value, QueryOperationalEnum.EQ);
	}

	/**
	 * @Description 等于条件
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月26日
	 */
	public static Condition eq(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.EQ);
	}

	/**
	 * @Description 不等于条件
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Condition ne(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.NE);
	}

	/**
	 * @Description is null条件
	 * @param propertyName
	 * @return
	 * @date 2017年9月26日
	 */
	public static Condition isNull(String propertyName) {
		return new QueryCondition(propertyName, null, QueryOperationalEnum.EQ);
	}

	/**
	 * @Description is not null条件
	 * @param propertyName
	 * @return
	 * @date 2017年9月26日
	 */
	public static Condition isNotNull(String propertyName) {
		return new QueryCondition(propertyName, null, QueryOperationalEnum.NE);
	}

	/**
	 * @Description 小于 条件
	 * @param propertyName
	 * @return
	 * @date 2017年9月26日
	 */
	public static Condition lt(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.LT);
	}

	/**
	 * @Description 大于
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition gt(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.GT);
	}

	/**
	 * @Description 大于等于
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition gte(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.GTE);
	}

	/**
	 * @Description 小于等于
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition lte(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.LTE);
	}

	/**
	 * @Description 在...范围
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition in(String propertyName, Collection<? extends Object> value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.IN);
	}

	/**
	 * @Description 不在...范围
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition nin(String propertyName, Collection<? extends Object> value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.NIN);
	}

	/**
	 * @Description 模糊查询
	 * @param propertyName
	 * @param value
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition regex(String propertyName, Object value) {
		return new QueryCondition(propertyName, value, QueryOperationalEnum.REGEX);
	}

	/**
	 * @Description and连接
	 * @param lhs
	 * @param rhs
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition and(Condition lhs, Condition rhs) {
		List<Condition> conditions = new ArrayList<>();
		conditions.add(lhs);
		conditions.add(rhs);
		return new LogicalCondition(conditions, QueryOperationalEnum.AND);
	}

	/**
	 * @Description and连接
	 * @param lhs
	 * @param rhs
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition and(List<Condition> conditions) {
		return new LogicalCondition(conditions, QueryOperationalEnum.AND);
	}

	/**
	 * @Description or连接
	 * @param lhs
	 * @param rhs
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition or(Condition lhs, Condition rhs) {
		List<Condition> conditions = new ArrayList<>();
		conditions.add(lhs);
		conditions.add(rhs);
		return new LogicalCondition(conditions, QueryOperationalEnum.OR);
	}

	/**
	 * @Description or连接
	 * @param lhs
	 * @param rhs
	 * @return
	 * @date 2017年9月27日
	 */
	public static Condition or(List<Condition> conditions) {
		return new LogicalCondition(conditions, QueryOperationalEnum.OR);
	}
}

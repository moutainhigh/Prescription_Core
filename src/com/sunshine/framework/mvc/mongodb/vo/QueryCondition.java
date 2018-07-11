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
package com.sunshine.framework.mvc.mongodb.vo;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Filters;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.mvc.mongodb.vo
 * @ClassName QueryCondition.java
 * @Description MongoDB 比较条件对象
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月5日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class QueryCondition implements Condition {
	private static Logger logger = LoggerFactory.getLogger(QueryCondition.class);
	/**
	 */
	private String queryFieldName;

	/**
	 */
	private Object queryFieldVal;

	/**
	 * Query Operators
	 * 
	 * @see com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum
	 */
	private QueryOperationalEnum queryOperator;

	public QueryCondition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QueryCondition(String fieldName, Object fieldVal, QueryOperationalEnum queryOperator) {
		super();
		this.queryFieldName = fieldName;
		this.queryFieldVal = fieldVal;
		this.queryOperator = queryOperator;
	}

	public QueryCondition(String fieldName, Object fieldVal) {
		super();
		this.queryFieldName = fieldName;
		this.queryFieldVal = fieldVal;
		this.queryOperator = QueryOperationalEnum.EQ;
	}

	public String getQueryFieldName() {
		return queryFieldName;
	}

	public void setQueryFieldName(String queryFieldName) {
		this.queryFieldName = queryFieldName;
	}

	public Object getQueryFieldVal() {
		return queryFieldVal;
	}

	public void setQueryFieldVal(Object queryFieldVal) {
		this.queryFieldVal = queryFieldVal;
	}

	@Override
	public QueryOperationalEnum getQueryOperator() {
		return queryOperator;
	}

	public void setQueryOperator(QueryOperationalEnum queryOperator) {
		this.queryOperator = queryOperator;
	}

	@Override
	public Document buildConditionDocument() {
		Document document = null;
		if (queryOperator == null) {
			return new Document();
		}
		String keyword = queryOperator.getKeyword();
		try {
			switch (keyword) {
			case "eq":
				document = new Document(queryFieldName, new Document("$eq", queryFieldVal));
				break;
			case "gt":
				document = new Document(queryFieldName, new Document("$gt", queryFieldVal));
				break;
			case "gte":
				document = new Document(queryFieldName, new Document("$gte", queryFieldVal));
				break;
			case "lt":
				document = new Document(queryFieldName, new Document("$lt", queryFieldVal));
				break;
			case "lte":
				document = new Document(queryFieldName, new Document("$lte", queryFieldVal));
				break;
			case "ne":
				document = new Document(queryFieldName, new Document("$ne", queryFieldVal));
				break;
			case "in":
				if (queryFieldVal instanceof Collection) {
					document = new Document(queryFieldName, new Document("$in", queryFieldVal));
				} else {
					logger.error("参数类型错误,使用in查询参数类型为Collection");
					throw new Exception("参数类型错误,使用in查询参数类型为Collection");
				}
				break;
			case "nin":
				if (queryFieldVal instanceof Collection) {
					document = new Document(queryFieldName, new Document("$nin", queryFieldVal));
				} else {
					logger.error("参数类型错误,使用nin查询参数类型为Collection");
					throw new Exception("参数类型错误,使用nin查询参数类型为Collection");
				}
				break;
			case "exists":
				if (queryFieldVal instanceof Boolean) {
					document = new Document(queryFieldName, new Document("$exists", queryFieldVal));
				} else {
					logger.error("参数类型错误,使用exists查询参数类型为Boolean");
					throw new Exception("参数类型错误,使用exists查询参数类型为Boolean");
				}
				break;
			case "type":
				document = new Document(queryFieldName, new Document("$type", queryFieldVal));
				break;
			case "regex":
				document = new Document(queryFieldName, new Document("$regex", ".*".concat(queryFieldVal.toString()).concat(".*")));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("QueryCondition queryOperator is error!case:{}", e.getMessage());
		}
		return document;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Bson buildConditionBson() {
		Bson bson = null;
		if (queryOperator == null) {
			return new Document();
		}
		String keyword = queryOperator.getKeyword();
		try {
			switch (keyword) {
			case "eq":
				bson = Filters.eq(queryFieldName, queryFieldVal);
				break;
			case "gt":
				bson = Filters.gt(queryFieldName, queryFieldVal);
				break;
			case "gte":
				bson = Filters.gte(queryFieldName, queryFieldVal);
				break;
			case "lt":
				bson = Filters.lt(queryFieldName, queryFieldVal);
				break;
			case "lte":
				bson = Filters.lte(queryFieldName, queryFieldVal);
				break;
			case "ne":
				bson = Filters.ne(queryFieldName, queryFieldVal);
				break;
			case "in":
				if (queryFieldVal instanceof Collection) {
					Collection<Object> fieldVals = (Collection<Object>) queryFieldVal;
					bson = Filters.in(queryFieldName, fieldVals);
				} else {
					logger.error("参数类型错误,使用in查询参数类型为Collection");
					throw new Exception("参数类型错误,使用in查询参数类型为Collection");
				}
				break;
			case "nin":
				if (queryFieldVal instanceof Collection) {
					Collection<Object> fieldVals = (Collection<Object>) queryFieldVal;
					bson = Filters.nin(queryFieldName, fieldVals);
				} else {
					logger.error("参数类型错误,使用nin查询参数类型为Collection");
					throw new Exception("参数类型错误,使用nin查询参数类型为Collection");
				}
				break;
			case "exists":
				if (queryFieldVal instanceof Boolean) {
					bson = Filters.exists(queryFieldName, (Boolean) queryFieldVal);
				} else {
					bson = Filters.exists(queryFieldName);
				}
				break;
			case "type":
				bson = Filters.type(queryFieldName, queryFieldVal.toString());
				break;
			case "regex":
				bson = Filters.regex(queryFieldName, ".*".concat(queryFieldVal.toString()).concat(".*"));
				break;
			default:
				break;
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!case:{}", e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!case:{}", e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!case:{}", e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!case:{}", e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!case:{}", e.getMessage());
		}
		return bson;
	}

	@Override
	public Condition add(Condition cond) {
		// TODO Auto-generated method stub
		Condition confition = null;
		if (getQueryOperator() != null) {
			confition = Restrictions.and(this, cond);
		} else {
			confition = cond;
		}
		return confition;
	}

	@Override
	public Condition or(Condition cond) {
		// TODO Auto-generated method stub
		Condition confition = null;
		if (getQueryOperator() != null) {
			confition = Restrictions.or(this, cond);
		} else {
			confition = cond;
		}
		return confition;
	}

	@Override
	public Condition createCondition() {
		// TODO Auto-generated method stub
		return new QueryCondition();
	}
}
/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月3日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.mongodb.vo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.mongodb.client.model.Filters;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;

/**
 * @Project Prescription_PlatForm
 * @Package com.sunshine.framework.mvc.mongodb.vo
 * @ClassName LogicalCondition.java
 * @Description 条件连接对象 例如:$and $or $not $nor
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月3日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class LogicalCondition implements Condition {
	private static Logger logger = LoggerFactory.getLogger(LogicalCondition.class);

	private List<? extends Condition> conditions;

	private QueryOperationalEnum queryOperator;

	public LogicalCondition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LogicalCondition(List<? extends Condition> conditions, QueryOperationalEnum queryOperator) {
		super();
		this.conditions = conditions;
		this.queryOperator = queryOperator;
	}

	public List<? extends Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<? extends Condition> conditions) {
		this.conditions = conditions;
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
		// TODO Auto-generated method stub
		Document document = null;
		if (!CollectionUtils.isEmpty(conditions)) {
			// 处理conditions中只有一个QueryCondition的情况 这种是不需要And or连接的
			if (conditions.size() == 1) {
				document = conditions.get(0).buildConditionDocument();
			} else {
				List<Document> filters = new ArrayList<>();
				for (Condition condition : conditions) {
					filters.add(condition.buildConditionDocument());
				}

				document = new Document("$".concat(queryOperator.getKeyword()), filters);
			}
		} else {
			new Document();
		}
		return document;
	}

	@Override
	public Bson buildConditionBson() {
		Bson filter = null;
		try {
			if (!CollectionUtils.isEmpty(conditions)) {
				// 处理conditions中只有一个QueryCondition的情况 这种是不需要And or连接的
				if (conditions.size() == 1 && conditions.get(0) instanceof QueryCondition) {
					filter = conditions.get(0).buildConditionBson();
				} else {
					Method method = null;
					List<Bson> filters = new ArrayList<>();
					for (Condition condition : conditions) {
						filters.add(condition.buildConditionBson());
					}
					method = Filters.class.getMethod(queryOperator.getKeyword(), Iterable.class);
					filter = (Bson) method.invoke(null, filters);
				}
			} else {
				filter = new Document();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("QueryCondition queryOperator is error!");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filter;
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
		return new LogicalCondition();
	}

}

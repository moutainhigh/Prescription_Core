/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月2日</p>
 *  <p> Created by 申姜</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.mongodb.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;
import com.sunshine.framework.mvc.mongodb.service.BaseMongoService;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;

/**
 * service基类实现类
 * 
 * @Package: com.sunshine.framework.mvc.service.impl
 * @ClassName: BaseServiceImpl
 * @Statement:
 *             <p>
 *             </p>
 * @JDK version used:
 * @Author: 申姜
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public abstract class BaseMongoServiceImpl<T extends BaseMongoEntity, PK extends Serializable> implements BaseMongoService<T, PK> {
	/**
	 * 获取数据库操作类
	 * 
	 * @return
	 */
	protected abstract BaseMongoDao<T, PK> getDao();

	@Override
	public T find(T entity) {
		return getDao().find(entity);
	}

	/**
	 * @Description 查询
	 * @param entity
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年10月17日
	 */
	@Override
	public T find(T entity, List<String> queryProperties) {
		return getDao().find(entity, queryProperties);
	}

	@Override
	public T findById(PK id) {
		return getDao().findById(id);
	}

	@Override
	public T findById(PK id, List<String> queryProperties) {
		return getDao().findById(id, queryProperties);
	}

	@Override
	public List<T> findByIds(List<PK> ids) {
		return getDao().findByIds(ids);
	}

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	@Override
	public List<T> findByIds(List<PK> ids, List<String> queryProperties) {
		return getDao().findByIds(ids, queryProperties);
	}

	@Override
	public PageInfo<T> findListByPage(List<Condition> conditions, Map<String, Integer> sortMap, Page<T> page) {
		return getDao().findListByPage(conditions, sortMap, page);
	}

	@Override
	public List<T> findAll() {
		return getDao().findAll();
	}

	/**
	 * @Description 查询所有记录
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年10月18日
	 */
	@Override
	public List<T> findAll(List<String> queryProperties) {
		return getDao().findAll(queryProperties);
	}

	@Override
	public Long count(LogicalCondition condition) {
		return getDao().count(condition);
	}

	@Override
	public Long count() {
		LogicalCondition condition = new LogicalCondition();
		return getDao().count(condition);
	}

	@Override
	public PK insert(T entity) {
		return getDao().insert(entity);
	}

	@Override
	public void delete(T entity) {
		getDao().delete(entity);
	}

	/**
	 * @Description 生成多个属性的索引
	 * @param fieldNames
	 * @return 索引Name
	 * @date 2017年6月5日
	 */
	@Override
	public List<String> createIndex(List<String> fieldNames) {
		return getDao().createIndex(fieldNames);
	}

	@Override
	public void deleteById(PK id) {
		getDao().deleteById(id);
	}

	@Override
	public Long deleteByIds(List<PK> ids) {
		return getDao().deleteByIds(ids);
	}

	@Override
	public Long deleteAll() {
		return getDao().deleteAll();
	}

	@Override
	public void update(T entity) {
		getDao().updateNotWithNull(entity);
	}

	@Override
	public void updateNotWithNull(T entity) {
		// TODO Auto-generated method stub
		getDao().updateNotWithNull(entity);
	}

	@Override
	public void updateWithNull(T entity) {
		// TODO Auto-generated method stub
		getDao().updateWithNull(entity);
	}

	@Override
	public Boolean check(Map<String, Serializable> params) {
		return getDao().check(params);
	}

	@Override
	public Long batchDelete(List<T> entities) {
		return getDao().batchDelete(entities);
	}

	@Override
	public void batchInsert(List<T> entitys) {
		getDao().batchInsert(entitys);
	}

	@Override
	public Long batchUpdate(List<T> entitys) {
		return getDao().batchUpdate(entitys);
	}

	/**
	 * @Description 根据对象属性查找对象,多个条件默认and连接
	 * @param paraMap
	 * @return
	 * @date 2017年6月5日
	 */
	@Override
	public List<T> findByParams(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Document document = new Document();
		// BasicDBObject queryCond = new BasicDBObject();
		for (String key : paraMap.keySet()) {
			document.append(key, paraMap.get(key));
		}
		return getDao().findByCondition(document);
	}

	/**
	 * @Description 根据条件查询 多个条件and连接
	 * @param conditions
	 * @return
	 * @date 2017年6月6日
	 */
	@Override
	public List<T> findByAndCondition(List<Condition> conditions) {
		if (CollectionUtils.isEmpty(conditions)) {
			return getDao().findAll();
		} else if (conditions.size() == 1) {
			return getDao().findByCondition(conditions.get(0).buildConditionBson());
		} else {
			LogicalCondition condition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			return findByLogicalCondition(condition);
		}
	}

	/**
	 * @Description 根据条件查询 多个条件and连接
	 * @param conditions
	 * @return
	 * @date 2017年6月6日
	 */
	@Override
	public List<T> findByAndCondition(List<Condition> conditions, List<String> queryProperties) {
		if (CollectionUtils.isEmpty(conditions)) {
			return getDao().findAll();
		} else if (conditions.size() == 1) {
			return getDao().findByCondition(conditions.get(0).buildConditionBson(), queryProperties);
		} else {
			LogicalCondition condition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			return findByLogicalCondition(condition, queryProperties);
		}
	}

	/**
	 * @Description 根据条件更新信息 多个条件为and连接,条件都为等值判断,
	 * @param conditionMap
	 * @param updateMap
	 * @date 2017年6月6日
	 */
	@Override
	public Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap) {
		return getDao().updateByParams(conditionMap, updateMap);
	}

	/**
	 * @Description 根据Id更新dataMap数据
	 * @param id
	 * @param updateMap
	 * @return
	 * @date 2017年8月7日
	 */
	@Override
	public void updateById(String id, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		getDao().updateById(id, updateMap);
	}

	@Override
	public void updateByIds(List<String> ids, Map<String, Object> updateMap) {
		getDao().updateByIds(ids, updateMap);
	}

	/**
	 * @Description 根据多个条件更新信息 and连接
	 * @param conditions
	 * @param updateMap
	 * @date 2017年8月8日
	 * @return 更新记录的条数
	 */
	@Override
	public Long updateByAndCondition(List<Condition> conditions, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		return getDao().updateByAndCondition(conditions, updateMap);
	}

	@Override
	public Long deleteByParams(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Document document = new Document();
		for (String key : paraMap.keySet()) {
			document.append(key, paraMap.get(key));
		}
		return getDao().deleteByCondition(document);
	}

	@Override
	public Long deleteByAndCondition(List<Condition> conditions) {
		// TODO Auto-generated method stub
		if (CollectionUtils.isEmpty(conditions)) {
			return getDao().deleteAll();
		} else if (conditions.size() == 1) {
			return getDao().deleteByCondition(conditions.get(0).buildConditionBson());
		} else {
			LogicalCondition condition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			return deleteByLogicalCondition(condition);
		}
	}

	@Override
	public Long deleteByLogicalCondition(LogicalCondition condition) {
		Bson bson = condition.buildConditionBson();
		return getDao().deleteByCondition(bson);
	}

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @return
	 * @date 2017年8月8日
	 */
	@Override
	public List<T> findByLogicalCondition(LogicalCondition condition) {
		return getDao().findByLogicalCondition(condition);
	}

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @return
	 * @date 2017年8月8日
	 */
	@Override
	public List<T> findByLogicalCondition(LogicalCondition condition, List<String> queryProperties) {
		return getDao().findByLogicalCondition(condition, queryProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sunshine.framework.mvc.mongodb.service.BaseMongoService#updateByLogicalCondition(com.sunshine.framework.mvc.mongodb.vo.LogicalCondition,
	 * java.util.Map)
	 */
	@Override
	public Long updateByLogicalCondition(LogicalCondition condition, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		return getDao().updateByLogicalCondition(condition, updateMap);
	}

	@Override
	public void updateById(T entity) {
		getDao().updateWithNull(entity);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Page<T> page) {
		Bson sort = null;
		if (CollectionUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}

		return getDao().findListByPage(condition.buildConditionBson(), sort, page);
	}

	@Override
	public PageInfo<T> findListByPage(LogicalCondition condition, Map<String, Integer> sortMap, Page<T> page) {
		Bson sort = null;
		if (CollectionUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}

		return getDao().findListByPage(condition.buildConditionBson(), sort, page);
	}

	@Override
	public PageInfo<T> findListByPage(Map<String, Object> parms, Map<String, Integer> sortMap, Page<T> page) {
		// TODO Auto-generated method stub
		Bson filters = null;
		if (CollectionUtils.isEmpty(parms)) {
			filters = new Document();
		} else {
			filters = Document.parse(JSON.toJSONString(parms));
		}

		Bson sort = null;
		if (CollectionUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}

		return getDao().findListByPage(filters, sort, page);
	}

	@Override
	public List<T> findByCondition(Condition condition) {
		// TODO Auto-generated method stub
		return getDao().findByCondition(condition.buildConditionBson());
	}

	@Override
	public <E> List<E> query(Class<E> clazz, Condition condition) {
		// TODO Auto-generated method stub
		return getDao().query(clazz, condition);
	}

	@Override
	public List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit) {
		// TODO Auto-generated method stub
		return getDao().aggregatesQuery(condition, groupJson, sortMap, projectMap, skip, limit);
	}

	@Override
	public <E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit, Class<E> resultClass) {
		// TODO Auto-generated method stub
		return getDao().aggregatesQuery(condition, groupJson, sortMap, projectMap, skip, limit, resultClass);
	}

	@Override
	public List<String> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit) {
		// TODO Auto-generated method stub
		return getDao().aggregatesQuery(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit);
	}

	@Override
	public Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson) {
		return getDao().aggregatesCount(condition, unwindMap, groupJson);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page) {
		// TODO Auto-generated method stub
		return getDao().findListByPage(condition, unwindMap, sortMap, projectMap, groupJson, page);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Map<String, Integer> projectMap, String groupJson,
			Page<T> page) {
		// TODO Auto-generated method stub
		return getDao().findListByPage(condition, sortMap, projectMap, groupJson, page);
	}

	@Override
	public <E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> clazz) {
		return getDao().aggregatesQuery(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit, clazz);
	}
}

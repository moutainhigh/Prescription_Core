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
package com.sunshine.framework.mvc.mongodb.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.MongoCollection;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package mongodb.dao
 * @ClassName BaseMongoDao.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月27日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface BaseMongoDao<T extends BaseMongoEntity, PK extends Serializable> {

	/**
	 * @Description 获取连接的Collection
	 * @return
	 * @date 2017年8月8日
	 */
	public MongoCollection<Document> getConCollection();

	/**
	 * 查询
	 * 
	 * @param entity
	 * @return
	 */
	public T find(T entity);

	/**
	 * @Description 查询
	 * @param entity
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年10月17日
	 */
	public T find(T entity, List<String> queryProperties);

	/**
	 * 通过Id查询
	 * 
	 * @param id
	 * @return
	 */
	public T findById(PK id);

	/**
	 * @Description 查询
	 * @param id
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年10月17日
	 */
	public T findById(PK id, List<String> queryProperties);

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> findByIds(List<PK> ids);

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	public List<T> findByIds(List<PK> ids, List<String> queryProperties);

	/**
	 * 查询列表
	 * 
	 * @param condition
	 *            查询条件
	 * @param page
	 *            sort 排序字段和排序规则
	 * @return
	 */
	public PageInfo<T> findListByPage(Bson condition, Bson sort, Page<T> page);

	/**
	 * 查询列表
	 * 
	 * @param conditions
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param page
	 * @return
	 */
	public PageInfo<T> findListByPage(List<Condition> conditions, Map<String, Integer> sortMap, Page<T> page);

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	public List<T> findAll();

	/**
	 * @Description 查询所有记录
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年10月18日
	 */
	public List<T> findAll(List<String> queryProperties);

	/**
	 * 查询总记录数
	 * 
	 * @return
	 */
	public Long count(Condition condition);

	/**
	 * 添加
	 * 
	 * @param entity
	 */
	public PK insert(T entity);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public Long delete(T entity);

	/**
	 * 根据Id删除
	 * 
	 * @param id
	 */
	public Long deleteById(PK id);

	/**
	 * 根据ID集合删除
	 * 
	 * @param ids
	 */
	public Long deleteByIds(List<PK> ids);

	/**
	 * 删除所有记录
	 */
	public Long deleteAll();

	/**
	 * @Description 根据条件删除数据
	 * @param queryCond
	 * @return
	 * @date 2017年8月7日
	 */
	public Long deleteByCondition(Bson queryCond);

	/**
	 * 检查数据是否已经存在
	 * 
	 * @param params
	 * @return
	 */
	public Boolean check(Map<String, Serializable> params);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	public Long batchDelete(List<T> entitys);

	/**
	 * 批量插入
	 * 
	 * @param entitys
	 */
	public void batchInsert(List<T> entitys);

	/**
	 * 批量更新
	 * 
	 * @param entitys
	 */
	public Long batchUpdate(List<T> entitys);

	/**
	 * @Description 生成多个属性的索引
	 * @param fieldNames
	 * @return 索引Name
	 * @date 2017年6月5日
	 */
	public List<String> createIndex(List<String> fieldNames);

	/**
	 * @Description 根据查询条件查找对象
	 * @param queryCond
	 * @return
	 * @date 2017年6月5日
	 */
	public List<T> findByCondition(Bson queryCond);

	/**
	 * @Description 根据查询条件查找对象
	 * @param queryCond
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年6月5日
	 */
	public List<T> findByCondition(Bson queryCond, List<String> queryProperties);

	/**
	 * @Description 更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 * @param entity
	 * @return
	 * @date 2017年8月7日
	 */
	public Long updateWithNull(T entity);

	/**
	 * @Description 更新所有属性 不包括entity中为null的属性
	 * @param entity
	 * @return
	 * @date 2017年8月7日
	 */
	public Long updateNotWithNull(T entity);

	/**
	 * @Description 根据Id更新dataMap数据
	 * @param id
	 * @param updateMap
	 * @return
	 * @date 2017年8月7日
	 */
	public Long updateById(String id, Map<String, Object> updateMap);

	/**
	 * @Description 根据Id集合更新dataMap数据
	 * @param id
	 * @param updateMap
	 * @return
	 */
	public Long updateByIds(List<String> ids, Map<String, Object> updateMap);

	/**
	 * @Description 更新所有属性
	 * @param id
	 * @return
	 * @date 2017年8月7日
	 */
	public Long updateByIdForDataMap(String id, Map<String, Object> dataMap);

	/**
	 * @Description 更新所有属性
	 * @param ids
	 * @param dataMap
	 * @return
	 */
	public Long updateByIdsForDataMap(List<String> ids, Map<String, Object> dataMap);

	/**
	 * @Description 根据多个eq条件更新信息 and连接
	 * @param conditionMap
	 * @param updateMap
	 * @date 2017年6月6日
	 */
	public Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap);

	/**
	 * @Description 根据多个条件更新信息 and连接
	 * @param conditions
	 * @param updateMap
	 * @date 2017年8月8日
	 * @return 更新记录的条数
	 */
	public Long updateByAndCondition(List<Condition> conditions, Map<String, Object> updateMap);

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @return
	 * @date 2017年8月8日
	 */
	public List<T> findByLogicalCondition(LogicalCondition condition);

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年8月8日
	 */
	public List<T> findByLogicalCondition(LogicalCondition condition, List<String> queryProperties);

	public Long updateByLogicalCondition(LogicalCondition condition, Map<String, Object> updateMap);

	public Long deleteByLogicalCondition(LogicalCondition condition);

	/**
	 * @Description 查询指定实体类的信息
	 * @param clazz
	 * @param condition
	 * @return
	 * @date 2017年10月18日
	 */
	public <E> List<E> query(Class<E> clazz, Condition condition);

	/**
	 * @Description 根据条件查询指定实体类
	 * @param clazz
	 * @param condition
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年9月27日
	 */
	public <E> List<E> query(Class<E> clazz, Condition condition, List<String> queryProperties);

	/**
	 * @Description
	 * @param condition
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @return
	 * @date 2017年10月20日
	 */
	public List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit);

	/**
	 * @Description
	 * @param condition
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @param resultClass
	 *            查询结果自动转换为的类型
	 * @return
	 * @date 2017年10月20日
	 */
	public <E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit, Class<E> resultClass);

	/**
	 * 数据中有子集，对子集中的数据进行分组统计。
	 * @param matchCondition
	 * @param unwindCondition 将数组集合中的每一个值拆分成独立的文档
	 * @param groupJson  分组的Json表达式
	 * @param sortMap  排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap 字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param skip  传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @param limit 传null 表示不进行limit操作
	 * @return
	 */
	public List<String> aggregatesQuery(Condition matchCondition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit);

	/**
	 * 查询列表
	 * 
	 * @param conditions
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param unwindCondition 将数组集合中的每一个值拆分成独立的文档
	 * @param groupJson  分组的Json表达式
	 * @param sortMap  排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap 字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param page
	 * @return
	 */
	public PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page);

	/**
	 * 查询列表
	 * 
	 * @param conditions
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param groupJson  分组的Json表达式
	 * @param sortMap  排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap 字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param page
	 * @return
	 */
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Map<String, Integer> projectMap, String groupJson,
			Page<T> page);

	/**
	 * 查询列表
	 * 
	 * @param conditions
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param unwindCondition 将数组集合中的每一个值拆分成独立的文档
	 * @param groupJson  分组的Json表达式
	 * @param sortMap  排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap 字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param page
	 * @param clazz 需要序列化的对象
	 * @return
	 */
	public <E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> clazz);

	/**
	 * 分组总记录数
	 * @param condition
	 * @param unwindMap
	 * @param groupJson
	 * @return
	 */
	public Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson);
}

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
package com.sunshine.framework.mvc.mongodb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;
import com.sunshine.framework.mvc.service.BaseService;

/**
 * service基类接口
 * 
 * @Package: com.sunshine.framework.mvc.service
 * @ClassName: BaseService
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
public interface BaseMongoService<T extends BaseMongoEntity, PK extends Serializable> extends BaseService<T, PK> {

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
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	public List<T> findByIds(List<PK> ids, List<String> queryProperties);

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
	 * 查询列表
	 * 
	 * @param conditions
	 *            查询条件
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param page
	 * @return
	 */
	public PageInfo<T> findListByPage(LogicalCondition conditions, Map<String, Integer> sortMap, Page<T> page);

	/**
	 * 查询列表
	 * 
	 * @param conditions
	 *            查询条件
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param page
	 * @return
	 */
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Page<T> page);

	/**
	 * @Description 查询所有记录
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年10月18日
	 */
	public List<T> findAll(List<String> queryProperties);

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @return
	 * @date 2017年9月27日
	 */
	public List<T> findByCondition(Condition condition);

	/**
	 * @Description 根据查询条件查询 指定的实体类
	 * @param condition
	 * @return
	 * @date 2017年9月27日
	 */
	public <E> List<E> query(Class<E> clazz, Condition condition);

	/**
	 * 查询总记录数
	 * 
	 * @return
	 */
	public Long count(LogicalCondition condition);

	/**
	 * @Description 根据参数条件删除数据
	 * @date 2017年8月7日
	 */
	public Long deleteByParams(Map<String, Object> paraMap);

	/**
	 * @Description 根据条件删除 多个条件默认and连接
	 * @param conditions
	 * @return
	 * @date 2017年6月6日
	 * @return 删除记录的条数
	 */
	public Long deleteByAndCondition(List<Condition> conditions);

	/**
	 * 更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 *
	 * @param entity
	 */
	public void updateWithNull(T entity);

	/**
	 * @Description 更新所有属性 不包括entity中为null的属性
	 * @param entity
	 * @return
	 * @date 2017年8月7日
	 */
	public void updateNotWithNull(T entity);

	/**
	 * @Description 根据Id更新updateMap中的数据
	 * @param id
	 * @param updateMap
	 * @return
	 * @date 2017年8月7日
	 */
	public void updateById(String id, Map<String, Object> updateMap);

	/**
	 * @Description 根据Id集合更新updateMap中的数据
	 * @param ids
	 * @param updateMap
	 */
	public void updateByIds(List<String> ids, Map<String, Object> updateMap);

	/**
	 * @Description 生成多个属性的索引
	 * @param fieldNames
	 * @return 索引Name
	 * @date 2017年6月5日
	 */
	public List<String> createIndex(List<String> fieldNames);

	/**
	 * @Description 根据对象属性查找对象,多个条件默认and连接 条件都为等值判断
	 * @param paraMap
	 * @return
	 * @date 2017年6月5日
	 */
	public List<T> findByParams(Map<String, Object> paraMap);

	/**
	 * @Description 根据条件查询 多个条件默认and连接
	 * @param conditions
	 * @return
	 * @date 2017年6月6日
	 * @return 更新记录的条数
	 */
	public List<T> findByAndCondition(List<Condition> conditions);

	/**
	 * @Description 根据条件查询 多个条件默认and连接
	 * @param conditions
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年6月6日
	 * @return 更新记录的条数
	 */
	public List<T> findByAndCondition(List<Condition> conditions, List<String> queryProperties);

	/**
	 * @Description 根据多个eq条件更新信息 and连接
	 * @param conditionMap
	 * @param updateMap
	 * @date 2017年6月6日
	 * @return 更新记录的条数
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

	/**
	 * @Description 根据查询条件删除
	 * @param condition
	 * @return
	 * @date 2017年8月8日
	 */
	public Long deleteByLogicalCondition(LogicalCondition condition);

	/**
	 * @Description 根据查询条件更新
	 * @param condition
	 * @param updateMap
	 * @return
	 * @date 2017年8月8日
	 */
	public Long updateByLogicalCondition(LogicalCondition condition, Map<String, Object> updateMap);

	/**
	 * @Description 分组聚合查询
	 * @param condition
	 *            查询条件 为null则没有过滤条件
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
	 * @Description 分组聚合查询
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
	 * 分组总记录数
	 * @param condition
	 * @param unwindMap
	 * @param groupJson
	 * @return
	 */
	public Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson);

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
}

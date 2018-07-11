/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月26日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.entity.BaseEntity;

/**
 * @Project Prescription_PlatForm
 * @Package com.sunshine.framework.mvc.service
 * @ClassName BaseService.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月26日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface BaseService<T extends BaseEntity, PK extends Serializable> {
	/**
	 * 查询
	 * 
	 * @param entity
	 * @return
	 */
	public T find(T entity);

	/**
	 * 通过Id查询
	 * 
	 * @param id
	 * @return
	 */
	public T findById(PK id);

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> findByIds(List<PK> ids);

	/**
	 * 查询列表
	 * 
	 * @param entity
	 * @param pageInfo
	 * @return
	 */
	public PageInfo<T> findListByPage(Map<String, Object> parms, Map<String, Integer> sortMap, Page<T> page);

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 查询总记录数
	 * 
	 * @return
	 */
	public Long count();

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
	public void delete(T entity);

	/**
	 * 根据Id删除
	 * 
	 * @param id
	 */
	public void deleteById(PK id);

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
	 * 根据Id更新有值的属性、新增字段 不更新值为null或者空字符串的属性
	 * 
	 * @param entity
	 */
	public void update(T entity);

	/**
	 * 更新所有属性 包括值为null或者空字符串的属性
	 * 
	 * @param entity
	 */
	public void updateById(T entity);

	/**
	 * 根据实体类集合批量删除
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
	 * 检查数据是否已经存在
	 * 
	 * @param params
	 * @return
	 */
	public Boolean check(Map<String, Serializable> params);
}

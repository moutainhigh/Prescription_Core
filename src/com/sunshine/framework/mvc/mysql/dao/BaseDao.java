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
package com.sunshine.framework.mvc.mysql.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;

/**
 * dao基类接口
 * 
 * @Package: com.sunshine.framework.mvc.mysql.dao
 * @ClassName: BaseDao
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
public interface BaseDao<T extends BaseSQLEntity, PK extends Serializable> {

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
	 * 根据参数查询
	 * 
	 * @param params
	 * @return
	 */
	public List<T> findByParams(Map<String, Serializable> params);

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
	 * @return
	 */
	public List<T> findList(T entity);

	/**
	 * 查询列表
	 * 
	 * @param params
	 * @param pageBounds
	 * @return
	 */
	public PageInfo<T> findListByPage(Map<String, Object> params, Page<T> page);

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
	 * 查询总记录数
	 * 
	 * @param entity
	 * @return
	 */
	public Long count(T entity);

	/**
	 * 插入数据 包括值为null或者空字符串的属性
	 * 
	 * @param entity
	 */
	public PK insert(T entity);

	/**
	 * @Description 插入数据 不包括值为null或者空字符串的属性
	 * @param entity
	 * @return
	 * @date 2017年7月9日
	 */
	public PK insertWithoutNull(T entity);

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
	 * 更新有值的属性 不更新值为null或者空字符串的属性
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
	 * 检查数据是否已经存在
	 * 
	 * @param params
	 * @return
	 */
	public Boolean check(Map<String, Serializable> params);

	/**
	 * 根据ID集合批量删除
	 * 
	 * @param ids
	 */
	public Long batchDelete(Collection<T> entitys);

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

}

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
package com.sunshine.framework.mvc.mysql.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;
import com.sunshine.framework.mvc.mysql.service.BaseSQLService;

/**
 * service基类实现类
 * 
 * @Package: com.sunshine.framework.mvc.service.impl
 * @ClassName: BaseServiceImpl
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
public abstract class BaseServiceImpl<T extends BaseSQLEntity, PK extends Serializable> implements BaseSQLService<T, PK> {
	/**
	 * 获取数据库操作类
	 * 
	 * @return
	 */
	protected abstract BaseDao<T, PK> getDao();

	@Override
	public T find(T entity) {
		return getDao().find(entity);
	}

	@Override
	public T findById(PK id) {
		return getDao().findById(id);
	}

	@Override
	public List<T> findByParams(Map<String, Serializable> params) {
		return getDao().findByParams(params);
	}

	@Override
	public List<T> findByIds(List<PK> ids) {
		return getDao().findByIds(ids);
	}

	@Override
	public PageInfo<T> findListByPage(Map<String, Object> parms, Map<String, Integer> sortMap, Page<T> page) {
		return getDao().findListByPage(parms, page);
	}

	@Override
	public List<T> findAll() {
		return getDao().findAll();
	}

	@Override
	public Long count() {
		return getDao().count();
	}

	@Override
	public Long count(T entity) {
		return getDao().count(entity);
	}

	@Override
	public PK insert(T entity) {
		return getDao().insert(entity);
	}

	@Override
	public void delete(T entity) {
		getDao().delete(entity);
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
	public Boolean check(Map<String, Serializable> params) {
		return getDao().check(params);
	}

	@Override
	public Long batchDelete(List<T> entitys) {
		return getDao().batchDelete(entitys);
	}

	@Override
	public void batchInsert(List<T> entitys) {
		getDao().batchInsert(entitys);
	}

	@Override
	public Long batchUpdate(List<T> entitys) {
		return getDao().batchUpdate(entitys);
	}

	@Override
	public PK insertWithoutNull(T entity) {
		// TODO Auto-generated method stub
		return getDao().insertWithoutNull(entity);
	}

	@Override
	public void update(T entity) {
		getDao().update(entity);
	}

	@Override
	public void updateById(T entity) {
		// TODO Auto-generated method stub
		getDao().updateById(entity);
	}

}

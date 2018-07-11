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
package com.sunshine.framework.mvc.mysql.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;
import com.sunshine.framework.utils.BeanUtils;

/**
 * 基础Dao接口实现类，实现该类的子类必须设置泛型类型
 * 
 * @Package: com.sunshine.framework.mvc.mysql.dao.impl
 * @ClassName: BaseDaoImpl
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
public class BaseDaoImpl<T extends BaseSQLEntity, PK extends Serializable> implements BaseDao<T, PK> {
	private static Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	@Resource
	protected SqlSessionTemplate sqlSession;

	/**
	 * 只用于查询 实时性查询请勿使用
	 */
	@Resource
	protected SqlSessionTemplate querySqlSession;

	protected final String SQLNAME_SEPARATOR = ".";

	private String sqlMapperNamespace = getDefaultSqlMapperNamespace();

	private final static String SQLNAME_FIND_BY_ID = "findById";
	private final static String SQLNAME_FIND_BY_PARAMS = "findByParams";
	private final static String SQLNAME_FIND_BY_IDS = "findByIds";
	private final static String SQLNAME_FIND_LIST = "findList";
	protected final static String SQLNAME_FIND_LIST_BY_PAGE = "findListByPage";
	private final static String SQLNAME_FIND_ALL = "findAll";
	private final static String SQLNAME_INSERT = "insert";
	private final static String SQLNAME_INSERT_WITHOUT_NULL = "insertWithoutNull";
	private final static String SQLNAME_DELETE_BY_ID = "deleteById";
	private final static String SQLNAME_DELETE_BY_IDS = "deleteByIds";
	private final static String SQLNAME_DELETE_ALL = "deleteAll";
	private final static String SQLNAME_UPDATE_BY_ID = "updateById";
	private final static String SQLNAME_UPDATE = "update";
	private final static String SQLNAME_CHECK = "check";
	private final static String SQLNAME_BATCH_INSERT = "batchInsert";
	private final static String SQLNAME_BATCH_UPDATE = "batchUpdate";
	private final static String SQLNAME_COUNT = "count";

	/**
	 * @return the sqlMapperNamespace
	 */

	public String getSqlMapperNamespace() {
		return sqlMapperNamespace;
	}

	/**
	 * @param sqlMapperNamespace
	 *            the sqlMapperNamespace to set
	 */

	public void setSqlMapperNamespace(String sqlMapperNamespace) {
		this.sqlMapperNamespace = sqlMapperNamespace;
	}

	/**
	 * @return the sqlSession
	 */

	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}

	/**
	 * @param sqlSession
	 *            the sqlSession to set
	 */

	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 获取泛型类型的实体对象类全名
	 * 
	 * @return
	 */
	private String getDefaultSqlMapperNamespace() {
		Class<?> genericClass = BeanUtils.getGenericClass(this.getClass());
		return genericClass == null ? null : genericClass.getName();
	}

	/**
	 * 将sqlMapperNamespace与给定的sqlName组合在一起.
	 * 
	 * @param sqlName
	 * @return
	 */
	protected String getSqlName(String sqlName) {
		return sqlMapperNamespace.concat(SQLNAME_SEPARATOR).concat(sqlName);
	}

	@Override
	public T findById(PK id) {
		try {
			Assert.notNull(id);
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_BY_ID), id);
		} catch (Exception e) {
			logger.error(String.format("根据ID查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_ID)), e);
			throw new SystemException(String.format("根据ID查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_ID)), e);
		}
	}

	@Override
	public List<T> findByParams(Map<String, Serializable> params) {
		try {
			Assert.notNull(params);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_BY_PARAMS), params);
		} catch (Exception e) {
			logger.error(String.format("根据参数查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_PARAMS)), e);
			throw new SystemException(String.format("根据参数查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_PARAMS)), e);
		}
	}

	@Override
	public List<T> findByIds(List<PK> ids) {
		try {
			Assert.notNull(ids);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_BY_IDS), ids);
		} catch (Exception e) {
			logger.error(String.format("根据ID集合查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_IDS)), e);
			throw new SystemException(String.format("根据ID集合查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_IDS)), e);
		}
	}

	@Override
	public List<T> findList(T entity) {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_LIST), entity);
		} catch (Exception e) {
			logger.error(String.format("查询对象列表出错！语句：%s", getSqlName(SQLNAME_FIND_LIST)), e);
			throw new SystemException(String.format("查询对象列表出错！语句：%s", getSqlName(SQLNAME_FIND_LIST)), e);
		}
	}

	@Override
	public List<T> findAll() {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL));
		} catch (Exception e) {
			logger.error(String.format("查询所有对象列表出错！语句：%s", getSqlName(SQLNAME_FIND_ALL)), e);
			throw new SystemException(String.format("查询所有对象列表出错！语句：%s", getSqlName(SQLNAME_FIND_ALL)), e);
		}
	}

	@Override
	public PageInfo<T> findListByPage(Map<String, Object> params, Page<T> page) {
		try {
			PageHelper.startPage(page.getPageNum(), page.getPageSize());
			List<T> list = sqlSession.selectList(getSqlName(SQLNAME_FIND_LIST_BY_PAGE), params);
			return new PageInfo<>(list);
		} catch (Exception e) {
			logger.error(String.format("根据分页对象查询列表出错！语句:%s", getSqlName(SQLNAME_FIND_LIST_BY_PAGE)), e);
			throw new SystemException(String.format("根据分页对象查询列表出错！语句:%s", getSqlName(SQLNAME_FIND_LIST_BY_PAGE)), e);
		}
	}

	@Override
	public Long count() {
		try {
			return sqlSession.selectOne(getSqlName(SQLNAME_COUNT));
		} catch (Exception e) {
			logger.error(String.format("查询对象总记录数出错！语句：%s", getSqlName(SQLNAME_COUNT)), e);
			throw new SystemException(String.format("查询对象总记录数出错！语句：%s", getSqlName(SQLNAME_COUNT)), e);
		}
	}

	@Override
	public Long count(T entity) {
		try {
			return sqlSession.selectOne(getSqlName(SQLNAME_COUNT), entity);
		} catch (Exception e) {
			logger.error(String.format("查询对象总记录数出错！语句：%s", getSqlName(SQLNAME_COUNT)), e);
			throw new SystemException(String.format("查询对象总记录数出错！语句：%s", getSqlName(SQLNAME_COUNT)), e);
		}
	}

	@Override
	public void delete(T entity) {
		try {
			Assert.notNull(entity.getId());
			sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_ID), entity.getId());
		} catch (Exception e) {
			logger.error(String.format("删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ID)), e);
			throw new SystemException(String.format("删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ID)), e);
		}
	}

	@Override
	public void deleteById(PK id) {
		try {
			Assert.notNull(id);
			sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_ID), id);
		} catch (Exception e) {
			logger.error(String.format("根据ID删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ID)), e);
			throw new SystemException(String.format("根据ID删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_ID)), e);
		}
	}

	@Override
	public Long deleteByIds(List<PK> ids) {
		Long delCount = 0L;
		try {
			Assert.notNull(ids);
			List<PK> idList = new ArrayList<>();
			idList.addAll(ids);
			delCount = Long.valueOf(sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_IDS), ids));
		} catch (Exception e) {
			logger.error(String.format("根据ID集合删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_IDS)), e);
			throw new SystemException(String.format("根据ID集合删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_IDS)), e);
		}
		return delCount;
	}

	@Override
	public Long deleteAll() {
		Long delCount = 0L;
		try {
			delCount = Long.valueOf(sqlSession.delete(getSqlName(SQLNAME_DELETE_ALL)));
		} catch (Exception e) {
			logger.error(String.format("删除所有对象出错！语句：%s", getSqlName(SQLNAME_DELETE_ALL)), e);
			throw new SystemException(String.format("删除所有对象出错！语句：%s", getSqlName(SQLNAME_DELETE_ALL)), e);
		}

		return delCount;
	}

	@Override
	public PK insert(T entity) {
		try {
			Assert.notNull(entity);
			if (StringUtils.isBlank(entity.getId())) {
				entity.setId(PKGenerator.generateId());
			}
			sqlSession.insert(getSqlName(SQLNAME_INSERT), entity);
			return (PK) entity.getId();
		} catch (Exception e) {
			logger.error(String.format("保存对象出错！语句：%s", getSqlName(SQLNAME_INSERT)), e);
			throw new SystemException(String.format("保存对象出错！语句：%s", getSqlName(SQLNAME_INSERT)), e);
		}
	}

	@Override
	public void update(T entity) {
		try {
			Assert.notNull(entity.getId());
			sqlSession.update(getSqlName(SQLNAME_UPDATE), entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("更新对象出错！语句：%s", getSqlName(SQLNAME_UPDATE)), e);
			throw new SystemException(String.format("更新对象出错！语句：%s", getSqlName(SQLNAME_UPDATE)), e);
		}
	}

	@Override
	public void updateById(T entity) {
		try {
			Assert.notNull(entity);
			sqlSession.update(getSqlName(SQLNAME_UPDATE_BY_ID), entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("更新对象出错！语句：%s", getSqlName(SQLNAME_UPDATE_BY_ID)), e);
			throw new SystemException(String.format("更新对象出错！语句：%s", getSqlName(SQLNAME_UPDATE_BY_ID)), e);
		}
	}

	@Override
	public Boolean check(Map<String, Serializable> params) {
		try {
			T t = sqlSession.selectOne(getSqlName(SQLNAME_CHECK), params);
			if (t != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(String.format("检查数据是否已经存在出错！语句：%s", getSqlName(SQLNAME_CHECK)), e);
			throw new SystemException(String.format("检查数据是否已经存在出错！语句：%s", getSqlName(SQLNAME_CHECK)), e);
		}
	}

	@Override
	public Long batchDelete(Collection<T> entitys) {
		Long delCount = 0L;
		try {
			Assert.notNull(entitys);
			List<PK> idList = new ArrayList<>();
			for (T entity : entitys) {
				idList.add((PK) entity.getId());
			}
			delCount = Long.valueOf(sqlSession.delete(getSqlName(SQLNAME_DELETE_BY_IDS), idList));
		} catch (Exception e) {
			logger.error(String.format("批量删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_IDS)), e);
			throw new SystemException(String.format("批量删除对象出错！语句：%s", getSqlName(SQLNAME_DELETE_BY_IDS)), e);
		}
		return delCount;
	}

	@Override
	public void batchInsert(List<T> entitys) {
		try {
			Assert.notNull(entitys);
			for (T entity : entitys) {
				/**
				 * 增加判断，如果添加数据已经存在ID则不需要设置 2015年7月7日 18:00:14 周鉴斌
				 */
				if (StringUtils.isBlank(entity.getId())) {
					entity.setId(PKGenerator.generateId());
				}
			}
			sqlSession.insert(getSqlName(SQLNAME_BATCH_INSERT), entitys);
		} catch (Exception e) {
			logger.error(String.format("批量插入对象出错！语句：%s", getSqlName(SQLNAME_BATCH_INSERT)), e);
			throw new SystemException(String.format("批量插入对象出错！语句：%s", getSqlName(SQLNAME_BATCH_INSERT)), e);
		}
	}

	@Override
	public Long batchUpdate(List<T> entitys) {
		Long updateCount = 0L;
		try {
			Assert.notNull(entitys);
			updateCount = Long.valueOf(sqlSession.update(getSqlName(SQLNAME_BATCH_UPDATE), entitys));
		} catch (Exception e) {
			logger.error(String.format("批量更新对象出错！语句：%s", getSqlName(SQLNAME_BATCH_UPDATE)), e);
			throw new SystemException(String.format("批量更新对象出错！语句：%s", getSqlName(SQLNAME_BATCH_UPDATE)), e);
		}
		return updateCount;
	}

	@Override
	public T find(T entity) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(entity.getId());
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_BY_ID), entity.getId());
		} catch (Exception e) {
			logger.error(String.format("查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_ID)), e);
			throw new SystemException(String.format("查询对象出错！语句：%s", getSqlName(SQLNAME_FIND_BY_ID)), e);
		}
	}

	@Override
	public PK insertWithoutNull(T entity) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(entity);
			if (StringUtils.isBlank(entity.getId())) {
				entity.setId(PKGenerator.generateId());
			}
			sqlSession.insert(getSqlName(SQLNAME_INSERT_WITHOUT_NULL), entity);
			return (PK) entity.getId();
		} catch (Exception e) {
			logger.error(String.format("保存对象出错！语句：%s", getSqlName(SQLNAME_INSERT_WITHOUT_NULL)), e);
			throw new SystemException(String.format("保存对象出错！语句：%s", getSqlName(SQLNAME_INSERT_WITHOUT_NULL)), e);
		}
	}

}

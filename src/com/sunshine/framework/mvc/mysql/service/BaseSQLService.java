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
package com.sunshine.framework.mvc.mysql.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sunshine.framework.mvc.mysql.entity.BaseSQLEntity;
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
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface BaseSQLService<T extends BaseSQLEntity, PK extends Serializable> extends BaseService<T, PK> {

	/**
	 * 根据参数查询
	 * 
	 * @param params
	 * @return
	 */
	public List<T> findByParams(Map<String, Serializable> params);

	/**
	 * @Description 插入数据 不插入null的字段
	 * @param entity
	 * @return
	 * @date 2017年7月9日
	 */
	public PK insertWithoutNull(T entity);

	/**
	 * 查询总记录数
	 * 
	 * @param entity
	 * @return
	 */
	public Long count(T entity);

}

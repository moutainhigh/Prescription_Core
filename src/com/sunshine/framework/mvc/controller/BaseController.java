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
package com.sunshine.framework.mvc.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.sunshine.framework.mvc.entity.BaseEntity;
import com.sunshine.framework.mvc.service.BaseService;

/**
 * controller基类
 * 
 * @Package: com.sunshine.framework.mvc.controller
 * @ClassName: BaseController
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
public abstract class BaseController<T extends BaseEntity, PK extends Serializable> {

	/**
	 * 获取服务
	 * 
	 * @return BaseService
	 */
	protected abstract BaseService<T, PK> getService();

	/**
	 * getRootAbsolutePath方法
	 * <p>
	 * 获取项目在操作系统的绝对路径
	 * </p>
	 * 返回形如D:\工具\Tomcat-6.0\webapps\projectname\字符串 其中projectname为项目名称
	 * 
	 * @param request
	 * @return
	 * @author : Asiainfo R&D deparment(GuangZhou)/Yuce
	 */
	public String getRootAbsolutePath(HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return rootPath;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
	}

	public String getBasePath(HttpServletRequest request) {
		return request.getContextPath();
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @param clazz
	 * @return
	 */
	protected Class<?> getGenericClass() {
		Type t = getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return ((Class<?>) p[0]);
		}
		return null;
	}
}

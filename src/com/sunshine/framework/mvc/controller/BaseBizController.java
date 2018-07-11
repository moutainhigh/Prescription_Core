/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年6月27日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.framework.mvc.MVCConstant;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.entity.BaseEntity;
import com.sunshine.framework.utils.StringHelper;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.mvc.controller
 * @ClassName BaseBizController.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月27日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public abstract class BaseBizController<T extends BaseEntity, PK extends Serializable> extends BaseController<T, PK> {
	private static Logger logger = LoggerFactory.getLogger(BaseBizController.class);

	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public RespBody save(T entity, HttpServletRequest request) {
		if (StringUtils.isNotBlank((String) entity.getId())) {
			getService().update(entity);
		} else {
			getService().insert(entity);
		}
		return new RespBody(StatusEnum.OK, entity, "保存成功!");
	}

	/**
	 * BaseEnity信息设置
	 * 
	 * @Description
	 * @param entity
	 * @param request
	 * @date 2017年5月23日
	 */
	protected void setCurUserInfo(BaseEntity entity, HttpServletRequest request) {
		// SysUser sysUser = super.getPlatformUser(request);
		// setInformation(sysUser, entity);
	}

	/**
	 * 删除
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RespBody delete(PK id, HttpServletRequest request) {
		if (id == null) {
			logger.error("要删除的ID号为null或空字符串！对象：{}", this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "删除失败,ID不能为空!");
		}
		getService().deleteById(id);
		return new RespBody(StatusEnum.OK, "删除成功!");
	}

	/**
	 * 根据ID集合删除
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteByIds")
	public RespBody deleteByIds(String ids, HttpServletRequest request) {
		if (StringUtils.isBlank(ids)) {
			logger.error("参数:{}不能为空！对象：{}", ids, this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "删除失败,ID集合不能为空!");
		}
		List<PK> idsList = StringHelper.strToArrayList(ids);
		getService().deleteByIds(idsList);
		return new RespBody(StatusEnum.OK, "批量删除成功!");
	}

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findById")
	public RespBody findById(PK id, HttpServletRequest request) {
		if (id == null) {
			logger.error("参数:{}不能为null或空字符串！对象:{}", id, this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "没有传入要查询的ID！");
		}
		T t = getService().findById(id);
		return new RespBody(StatusEnum.OK, t);
	}

	/**
	 * 根据ID集合查询
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findByIds")
	public RespBody findByIds(List<PK> ids, HttpServletRequest request) {
		if (ids != null && ids.size() > 0) {
			logger.error("未设置批量删除对象的ID号！对象：{}", this.getClass().getName());
			return new RespBody(StatusEnum.ERROR, "没有传入要查询的ID集合！");
		}
		List<T> list = getService().findByIds(ids);
		return new RespBody(StatusEnum.OK, list);
	}

	/**
	 * 跳转列表页面
	 * 
	 * @param query
	 * @param pageBounds
	 * @return
	 */
	@RequestMapping(value = "/listView")
	public String listView(HttpServletRequest request) {
		return getBasePath(request) + MVCConstant.VIEW_LIST;
	}

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAll")
	public RespBody findAll(HttpServletRequest request) {
		List<T> list = getService().findAll();
		return new RespBody(StatusEnum.OK, list);
	}

	/**
	 * 跳转编辑页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit(PK id, HttpServletRequest request) {
		T t = null;
		if (id != null) {
			t = getService().findById(id);
		}
		return new ModelAndView(getBasePath(request) + MVCConstant.VIEW_EDIT, "entity", t);
	}

	/**
	 * 检查数据是否已经存在
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/check")
	public RespBody check(CheckParams checkParams) {
		boolean status = getService().check(checkParams.getParams());
		if (status) {
			return new RespBody(StatusEnum.ERROR);
		} else {
			return new RespBody(StatusEnum.OK);
		}
	}

	/**
	 * @Description 分页列表查询
	 * @param pageNum
	 * @param pageSize
	 * @param pageParams
	 * @return
	 * @date 2017年6月29日
	 */
	@ResponseBody
	@RequestMapping(value = "/findListByPage")
	public PageInfo<T> findListByPage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize, PageParams pageParams) {
		PageInfo<T> pageInfo = getService().findListByPage(pageParams.getParams(), pageParams.getSortParams(), new Page<T>(pageNum, pageSize));
		return pageInfo;
	}

}

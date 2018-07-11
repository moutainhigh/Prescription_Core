/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年5月9日</p>
 *  <p> Created by 51397</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.elasticsearch;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.sort.SortOrder;

/**
 * @Project: yct
 * @Package: elasticsearch
 * @ClassName: SearchCondition
 * @Description: <p>
 *               </p>
 * @JDK version used:
 * @Author: 天竹子
 * @Create Date: 2017年5月9日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SearchCondition<T extends Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5386152998290686955L;

	/**
	 * 当前页
	 */
	private Integer curPage = 0;

	/**
	 * 页大小
	 */
	private Integer pageSize = 100;

	/**
	 * 总数
	 */
	private Integer totalNum;
	/**
	 * 索引id
	 */
	private String indexId;

	/**
	 * 索引名称
	 */
	private String indexName;

	/**
	 * 索引类型
	 */
	private String indexType;

	/**
	 * 索引id数组
	 */
	private String[] indexIds;

	/**
	 * 查询条件以json格式传入
	 */
	private String jsonObject;

	/**
	 * 键值对的键
	 */
	private String field;

	/**
	 * 统计用户操作轨迹用的
	 */
	private String actionName;

	/**
	 * 键值对的值
	 */
	private String value;

	/**
	 * operationThreaded表示操作执行是否在不同的线程上,默认设置为true
	 */
	Boolean operationThreaded;

	/**
	 * 分组键值列表，类似于group by 之后的字段
	 */
	List<String> groupFields;

	/**
	 * 分组SUM键值列表
	 */
	List<String> sumFields;

	/**
	 * 分组AVG键值列表
	 */
	List<String> avgFields;

	/**
	 * 排序字段键值对，可以添加多个，类似于("opTime","DESC")
	 */
	Map<String, SortOrder> sortField;
	/**
	 * 分组后排序字段键值对，可以添加多个，类似于("opTime","DESC"),注意此处最后PUT的键最先排序,排序键必须在sumFields或avgFields(只针对最后一层分组)
	 */
	Map<String, SortOrder> groupSortField;

	/**
	 * @return the curPage
	 */
	public Integer getCurPage() {
		return curPage;
	}

	/**
	 * @param curPage
	 *            the curPage to set
	 */
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the indexId
	 */
	public String getIndexId() {
		return indexId;
	}

	/**
	 * @param indexId
	 *            the indexId to set
	 */
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName
	 *            the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	/**
	 * @return the indexType
	 */
	public String getIndexType() {
		return indexType;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	/**
	 * @param indexIds
	 *            the indexIds to set
	 */
	public void setIndexIds(String[] indexIds) {
		this.indexIds = indexIds;
	}

	/**
	 * @return the jsonObject
	 */
	public String getJsonObject() {
		return jsonObject;
	}

	/**
	 * @param jsonObject
	 *            the jsonObject to set
	 */
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the operationThreaded
	 */
	public Boolean getOperationThreaded() {
		return operationThreaded;
	}

	/**
	 * @param operationThreaded
	 *            the operationThreaded to set
	 */
	public void setOperationThreaded(Boolean operationThreaded) {
		this.operationThreaded = operationThreaded;
	}

	/**
	 * @return the groupFields
	 */
	public List<String> getGroupFields() {
		return groupFields;
	}

	/**
	 * @param groupFields
	 *            the groupFields to set
	 */
	public void setGroupFields(List<String> groupFields) {
		this.groupFields = groupFields;
	}

	/**
	 * @return the sumFields
	 */
	public List<String> getSumFields() {
		return sumFields;
	}

	/**
	 * @param sumFields
	 *            the sumFields to set
	 */
	public void setSumFields(List<String> sumFields) {
		this.sumFields = sumFields;
	}

	/**
	 * @return the avgFields
	 */
	public List<String> getAvgFields() {
		return avgFields;
	}

	/**
	 * @param avgFields
	 *            the avgFields to set
	 */
	public void setAvgFields(List<String> avgFields) {
		this.avgFields = avgFields;
	}

	/**
	 * @return the sortField
	 */
	public Map<String, SortOrder> getSortField() {
		return sortField;
	}

	/**
	 * @param sortField
	 *            the sortField to set
	 */
	public void setSortField(Map<String, SortOrder> sortField) {
		this.sortField = sortField;
	}

	/**
	 * @return the groupSortField
	 */
	public Map<String, SortOrder> getGroupSortField() {
		return groupSortField;
	}

	/**
	 * @param groupSortField
	 *            the groupSortField to set
	 */
	public void setGroupSortField(Map<String, SortOrder> groupSortField) {
		this.groupSortField = groupSortField;
	}

	/**
	 * @return the totalNum
	 */
	public Integer getTotalNum() {
		return totalNum;
	}

	/**
	 * @param totalNum
	 *            the totalNum to set
	 */
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName
	 *            the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the indexIds
	 */
	public String[] getIndexIds() {
		return indexIds;
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @param clazz
	 * @return
	 */
	public Class<?> getGenericClass() {
		Type t = getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return ((Class<?>) p[0]);
		}
		return null;
	}

}

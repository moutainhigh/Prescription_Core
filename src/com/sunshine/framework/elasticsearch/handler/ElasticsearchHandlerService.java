package com.sunshine.framework.elasticsearch.handler;

import java.util.List;

import com.sunshine.framework.elasticsearch.EsSearchResponse;
import com.sunshine.framework.elasticsearch.SearchCondition;
import com.sunshine.framework.elasticsearch.SourceData;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch.handler.impl
 * @ClassName ElasticsearchHandlerService.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface ElasticsearchHandlerService {

	/**
	 * @Description 插入数据，创建数据索引
	 * @param data
	 *            要插入的数据
	 * @date 2017年4月6日
	 */
	public Boolean insertData(SourceData data);

	/**
	 * 
	 * @Description 批量插入数据
	 * @param datas
	 * @param batchNum
	 *            每次提交的数量 默认为500条
	 * @date 2017年4月6日
	 */
	public void batchInsertData(List<SourceData> datas, Integer batchNum);

	/**
	 * @Description 通过索引获取数据
	 * @param searchCondition
	 * @return
	 * @date 2017年4月6日
	 */
	public <T> EsSearchResponse queryDataByIndexId(SearchCondition<T> searchCondition);

	/**
	 * @description 通过索引、类型、字段值获取数据
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse queryDataByField(SearchCondition<T> searchCondition);

	/**
	 * @description 通过索引、类型、键值对获取数据-精确查询
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse queryDataByKeyValuePair(SearchCondition<T> searchCondition);

	/**
	 * @description 根据索引id删除记录,删除的时候慎用
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse deleteDataByIndexId(SearchCondition<T> searchCondition);

	/**
	 * @description 根据键值对以及索引名称进行数据的批量同步删除
	 * @param searchCondition
	 * @return 删除数据的数量 matchQuery的机制是：先检查字段类型是否是analyzed，如果是，则先分词，再去去匹配token；如果不是，则直接去匹配token； termQuery的机制是：直接去匹配token
	 */
	public <T> EsSearchResponse deleteDataByKeyValuePair(SearchCondition<T> searchCondition);

	/**
	 * @description 根据索引名称、类型、索引id以及jsonobject更新索引。 更新索引的方法还有prepareUpdate()方法(通过script或者是doc),本质上是一样,这里仅以字符串的方式进行更新。
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse dataUpdateByJsonObject(SearchCondition<T> searchCondition);

	/**
	 * 同一个索引以及类型根据索引id数组批量获取数据信息。
	 * 
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse multiDataGetByIndexids(SearchCondition<T> searchCondition);

	/**
	 * 分页查询
	 * 
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse queryFeildByPagination(SearchCondition<T> searchCondition);

	/**
	 * 分页查询:按照键值对查询
	 * 
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse queryKeyValueByPagination(SearchCondition<T> searchCondition);

	/**
	 * 查询执行actionName的数量,如果用hostName条件,则SearchCondition.field表示hostName.
	 * 
	 * @param searchCondition
	 * @return
	 */
	public <T> EsSearchResponse queryActionNameCount(SearchCondition<T> searchCondition);
}

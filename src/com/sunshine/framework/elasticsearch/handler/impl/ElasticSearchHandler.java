/**
 * 
 */
package com.sunshine.framework.elasticsearch.handler.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkIndexByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.elasticsearch.EsSearchResponse;
import com.sunshine.framework.elasticsearch.ResultCodeType;
import com.sunshine.framework.elasticsearch.SearchCondition;
import com.sunshine.framework.elasticsearch.SourceData;
import com.sunshine.framework.elasticsearch.handler.ElasticsearchHandlerService;
import com.sunshine.framework.elasticsearch.pool.ESClientPool;
import com.sunshine.framework.exception.SystemException;
import com.google.common.base.Joiner;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch
 * @ClassName ElasticSearchHandler.java
 * @Description elasticsearch 数据操作处理类
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@SuppressWarnings("hiding")
public class ElasticSearchHandler implements ElasticsearchHandlerService {
	private static Logger logger = LoggerFactory.getLogger(ElasticSearchHandler.class);
	/**
	 * 默认批量提交的数量
	 */
	private static final int DEFAULT_BATCH_NUM = 500;

	protected ESClientPool esClientPool;

	public ESClientPool getEsClientPool() {
		return esClientPool;
	}

	public void setEsClientPool(final ESClientPool esClientPool) {
		this.esClientPool = esClientPool;
	}

	private TransportClient getESClient() throws com.sunshine.framework.exception.SystemException {
		boolean isContinue = true;
		TransportClient client = null;
		int count = 3;
		try {
			do {
				try {
					client = esClientPool.getResource();
					isContinue = false;
				} catch (Exception e) {
					isContinue = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					count++;
				}
				if (count > 3) {
					break;
				}
			} while (isContinue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("redis server connect exception!", e);
		}
		return client;
	}

	public void returnResource(TransportClient client) {
		esClientPool.returnResource(client);
	}

	public void returnResource(TransportClient client, boolean broken) {
		if (broken) {
			esClientPool.returnBrokenResource(client);
		} else {
			esClientPool.returnResource(client);
		}
	}

	/**
	 * @Description 插入数据，创建数据索引
	 * @param data
	 *            要插入的数据
	 * @date 2017年4月6日
	 */
	@Override
	public Boolean insertData(SourceData data) {
		TransportClient client = getESClient();
		if (StringUtils.isBlank(data.getIndexName())) {
			logger.error("insertData is error.indexName is null.data:{}", JSON.toJSONString(data));
			return false;
		}
		if (StringUtils.isBlank(data.getIndexType())) {
			logger.error("insertData is error.indexType is null.data:{}", JSON.toJSONString(data));
			return false;
		}
		IndexResponse response = null;
		String source = data.getSource();
		if (StringUtils.isBlank(data.getIndexId())) {
			response = client.prepareIndex(data.getIndexName(), data.getIndexType(), PKGenerator.generateId()).setSource(source).get();
		} else {
			response = client.prepareIndex(data.getIndexName(), data.getIndexType(), data.getIndexId()).setSource(source).get();
		}

		if (response == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @Description 批量插入数据
	 * @param datas
	 * @param batchNum
	 *            每次提交的数量 默认为500条
	 * @date 2017年4月6日
	 */
	@Override
	public void batchInsertData(List<SourceData> datas, Integer batchNum) {
		TransportClient client = getESClient();

		if (batchNum == null) {
			batchNum = DEFAULT_BATCH_NUM;
		}

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		int count = 0;
		String indexId = null;
		String indexName = null;
		String indexType = null;
		String source = null;
		BulkResponse bulkResponse = null;
		for (SourceData data : datas) {
			indexId = data.getIndexId();
			indexType = data.getIndexType();
			indexName = data.getIndexName().toLowerCase();
			source = data.getSource();

			if (StringUtils.isBlank(indexType) || StringUtils.isBlank(indexName) || StringUtils.isBlank(source)) {
				logger.warn("generate elasticsearch index cancel.case:data is invalid.data:{}", JSON.toJSONString(data));
				continue;
			}

			count++;
			if (StringUtils.isBlank(indexId)) {
				bulkRequest.add(client.prepareIndex(indexName, indexType).setSource(source));
			} else {
				bulkRequest.add(client.prepareIndex(indexName, indexType, indexId).setSource(source));
			}

			// 每batchNum条提交一次
			if (count % batchNum == 0) {
				bulkResponse = bulkRequest.get();
				if (bulkResponse.hasFailures()) {
					// process failures by iterating through each bulk response item
					iterateHandle(bulkResponse);
				}
			} else {
				if (count == datas.size()) {
					bulkResponse = bulkRequest.get();
					if (bulkResponse.hasFailures()) {
						iterateHandle(bulkResponse);
					}
				}
			}
		}
	}

	/**
	 * 批处理迭代异常错位处理
	 * 
	 * @param bulkResponse
	 */
	private void iterateHandle(BulkResponse bulkResponse) {
		Iterator<BulkItemResponse> itemResponseIterator = bulkResponse.iterator();
		while (itemResponseIterator.hasNext()) {
			BulkItemResponse itemResponse = itemResponseIterator.next();
			if (itemResponse != null && itemResponse.isFailed()) {
				logger.error("bulk request is Fail.indexing  error indexmessage:{},FailureMessage:{}",
						Joiner.on(',').join(itemResponse.getIndex(), itemResponse.getType(), itemResponse.getId()),
						bulkResponse.buildFailureMessage());
			}
		}
	}

	@Override
	public <T> EsSearchResponse queryDataByIndexId(SearchCondition<T> searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| StringUtils.isBlank(searchCondition.getIndexId())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		if (searchCondition.getOperationThreaded() == null) {
			logger.info("getDataByIndexId.operationThreaded is null.used default value:true");
			searchCondition.setOperationThreaded(true);
		}
		TransportClient client = getESClient();
		GetResponse response = client.prepareGet(searchCondition.getIndexName(), searchCondition.getIndexType(), searchCondition.getIndexId())
				.setOperationThreaded(searchCondition.getOperationThreaded()).execute().actionGet();

		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(JSON.parseObject(JSON.toJSONString(response.getSource()), searchCondition.getGenericClass()));
		return esResponse;
	}

	@Override
	public <T> EsSearchResponse queryDataByField(SearchCondition<T> searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		List<T> list = new ArrayList<T>();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| StringUtils.isBlank(searchCondition.getField())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		TransportClient client = getESClient();
		SearchResponse response = client.prepareSearch(searchCondition.getIndexName()).setTypes(searchCondition.getIndexType())
				.setQuery(QueryBuilders.queryStringQuery(searchCondition.getField())).get();
		SearchHits searchHits = response.getHits();
		for (SearchHit searchHit : searchHits) {
			Map<String, Object> resultMap = searchHit.getSource();
			T data = (T) JSON.parseObject(JSON.toJSONString(resultMap), searchCondition.getGenericClass());
			list.add(data);
		}
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(list);
		return esResponse;
	}

	@Override
	public <T> EsSearchResponse queryDataByKeyValuePair(SearchCondition<T> searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		List<T> list = new ArrayList<T>();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| StringUtils.isBlank(searchCondition.getField()) || StringUtils.isBlank(searchCondition.getValue())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		TransportClient client = getESClient();
		SearchResponse response = client.prepareSearch(searchCondition.getIndexName()).setTypes(searchCondition.getIndexType())
				.setQuery(QueryBuilders.termQuery(searchCondition.getField(), searchCondition.getValue())).get();
		SearchHits searchHits = response.getHits();
		for (SearchHit searchHit : searchHits) {
			Map<String, Object> resultMap = searchHit.getSource();
			T data = (T) JSON.parseObject(JSON.toJSONString(resultMap), searchCondition.getGenericClass());
			list.add(data);
		}
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(list);
		return esResponse;
	}

	/**
	 * @description 根据索引id删除记录,删除的时候慎用
	 * @param indexId
	 * @param indexName
	 * @param indexType
	 */
	@Override
	public EsSearchResponse deleteDataByIndexId(SearchCondition searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| StringUtils.isBlank(searchCondition.getIndexId())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		TransportClient client = getESClient();
		try {
			client.prepareDelete(searchCondition.getIndexName(), searchCondition.getIndexType(), searchCondition.getIndexId()).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage("根据索引id删除记录失败,indexName:" + searchCondition.getIndexName() + ",indexType:" + searchCondition.getIndexType()
					+ ",indexId:" + searchCondition.getIndexId());
			return esResponse;
		}
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		return esResponse;
	}

	/**
	 * @description 根据键值对以及索引名称进行数据的批量同步删除
	 * @param key
	 * @param value
	 * @param indexName
	 * @return 删除数据的数量 matchQuery的机制是：先检查字段类型是否是analyzed，如果是，则先分词，再去去匹配token；如果不是，则直接去匹配token； termQuery的机制是：直接去匹配token
	 */
	@Override
	public EsSearchResponse deleteDataByKeyValuePair(SearchCondition searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getField())
				|| StringUtils.isBlank(searchCondition.getValue())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}

		try {
			BulkIndexByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(getESClient())
					.filter(QueryBuilders.matchQuery(searchCondition.getIndexName(), searchCondition.getValue()))
					.source(searchCondition.getIndexName()).get();
			response.getDeleted();// 删除数量
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage("根据索引以及键值对删除记录失败,indexName:" + searchCondition.getIndexName() + ",feild:" + searchCondition.getField()
					+ ",value:" + searchCondition.getValue());
			return esResponse;
		}
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		return esResponse;
		/*
		 * 异步处理方法如下,用execute代替get,并提供listener: final Object[] objs = new Object[1];
		 * DeleteByQueryAction.INSTANCE.newRequestBuilder(getESClient()).filter(QueryBuilders.matchQuery(key, value)).source(indexName) .execute(new
		 * ActionListener<BulkIndexByScrollResponse>() {
		 * 
		 * @Override public void onResponse(BulkIndexByScrollResponse response) { objs[0] = response.getDeleted(); }
		 * 
		 * @Override public void onFailure(Exception e) { // Handle the exception } }); return(Number) objs[0] ).longValue();
		 */
	}

	/**
	 * @description 根据索引名称、类型、索引id以及jsonobject更新索引。 更新索引的方法还有prepareUpdate()方法(通过script或者是doc),本质上是一样,这里仅以字符串的方式进行更新。
	 * @param indexId
	 * @param indexName
	 * @param indexType
	 * @param jsonObject
	 */
	@Override
	public EsSearchResponse dataUpdateByJsonObject(SearchCondition searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| StringUtils.isBlank(searchCondition.getIndexId())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(searchCondition.getIndexName());
		updateRequest.type(searchCondition.getIndexType());
		updateRequest.id(searchCondition.getIndexId());
		updateRequest.doc(searchCondition.getJsonObject());
		// updateRequest.doc(XContentFactory.jsonBuilder().startObject().field("gender", "male").endObject());
		try {
			getESClient().update(updateRequest).get();
		} catch (SystemException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage("根据索引名称、类型、索引id以及jsonobject更新索引失败,indexType:" + searchCondition.getIndexType() + ",indexId:"
					+ searchCondition.getIndexId());
			return esResponse;
		}
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		return esResponse;
	}

	/**
	 * 同一个索引以及类型根据索引id数组批量获取数据信息。
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 * @param indexIds
	 *            索引id数组
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> EsSearchResponse multiDataGetByIndexids(SearchCondition<T> searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| searchCondition.getIndexIds().length < 1) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		List<T> list = new ArrayList<T>();
		TransportClient client = getESClient();
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
				.add(searchCondition.getIndexName(), searchCondition.getIndexType(), searchCondition.getIndexIds()).get();
		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
				T data = (T) JSON.parseObject(JSON.toJSONString(json), searchCondition.getGenericClass());
				list.add(data);
			}
		}
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(list);
		return esResponse;
	}

	public static void main(String[] args) {
		ElasticSearchHandler obj = new ElasticSearchHandler();
		TransportClient client = obj.getESClient();
		IndexResponse response = null;
		String source = "{" + "\"actionName\":\"com.sunshine.common.datas.init.InitAppDataBean.afterPropertiesSet\","
				+ "\"callerDate\":\"2017-04-118 17:30:15\"," + "\"hostName\":\"tianzz\","
				+ "\"infoData\":\"==================================== Init Datas start ==================================\"" + "}";
		response = client.prepareIndex("ygkz", "ygkzindex", PKGenerator.generateId()).setSource(source).get();
	}

	@Override
	/**
	 * Note that from + size can not be more than the index.max_result_window index setting which defaults to 10,000.
	 * 如果搜索size大于10000，需要设置index.max_result_window参数,该参数默认为10,000(单页超过10,000的最好使用深度查询,See the Scroll or 
	 * Search After API for more efficient ways to do deep scrolling).
	 */
	public <T> EsSearchResponse queryFeildByPagination(SearchCondition<T> searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		List<T> list = new ArrayList<T>();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| searchCondition.getGenericClass() == null) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		TransportClient client = getESClient();
		SearchResponse response = client.prepareSearch(searchCondition.getIndexName()).setTypes(searchCondition.getIndexType())
				.setFrom(searchCondition.getCurPage() * searchCondition.getPageSize()).setSize(searchCondition.getPageSize())
				.setQuery(QueryBuilders.queryStringQuery(searchCondition.getField())).get();
		SearchHits searchHits = response.getHits();
		for (SearchHit searchHit : searchHits) {
			Map<String, Object> resultMap = searchHit.getSource();
			T data = (T) JSON.parseObject(JSON.toJSONString(resultMap), searchCondition.getGenericClass());
			list.add(data);
		}
		paginationSet(esResponse, searchCondition.getCurPage(), searchCondition.getPageSize(), (int) searchHits.totalHits());
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(list);
		return esResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see elasticsearch.handler.ElasticsearchHandlerService#queryKeyValueByPagination(elasticsearch.SearchCondition)
	 */
	@Override
	public <T> EsSearchResponse queryKeyValueByPagination(SearchCondition<T> searchCondition) {
		// TODO Auto-generated method stub
		EsSearchResponse esResponse = new EsSearchResponse();
		List<T> list = new ArrayList<T>();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| searchCondition.getGenericClass() == null) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		TransportClient client = getESClient();
		SearchResponse response = client.prepareSearch(searchCondition.getIndexName()).setTypes(searchCondition.getIndexType())
				.setFrom(searchCondition.getPageSize() * searchCondition.getCurPage()).setSize(searchCondition.getPageSize())
				.setQuery(QueryBuilders.termQuery(searchCondition.getField(), searchCondition.getValue())).get();
		// if (searchCondition.getSortField() != null) {
		// for (Map.Entry<String, SortOrder> entry : searchCondition.getSortField().entrySet()) {
		// esSearch.addSort(entry.getKey(), entry.getValue());
		// }
		// }
		SearchHits searchHits = response.getHits();
		for (SearchHit searchHit : searchHits) {
			Map<String, Object> resultMap = searchHit.getSource();
			T data = (T) JSON.parseObject(JSON.toJSONString(resultMap), searchCondition.getGenericClass());
			list.add(data);
		}
		paginationSet(esResponse, searchCondition.getCurPage(), searchCondition.getPageSize(), (int) searchHits.totalHits());
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(list);
		return esResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see elasticsearch.handler.ElasticsearchHandlerService#queryActionNameCount(elasticsearch.SearchCondition)
	 */
	@Override
	public EsSearchResponse queryActionNameCount(SearchCondition searchCondition) {
		EsSearchResponse esResponse = new EsSearchResponse();
		if (StringUtils.isBlank(searchCondition.getIndexName()) || StringUtils.isBlank(searchCondition.getIndexType())
				|| StringUtils.isBlank(searchCondition.getActionName())) {
			esResponse.setResultCode(ResultCodeType.REQUEST_PARAM_ERROR[0]);
			esResponse.setResultMessage(ResultCodeType.REQUEST_PARAM_ERROR[1]);
			return esResponse;
		}
		TransportClient client = getESClient();
		SearchRequestBuilder searchRequest = client.prepareSearch(searchCondition.getIndexName()).setTypes(searchCondition.getIndexType());
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.termQuery(ResultCodeType.ACTION_NAME + ResultCodeType.SPTLIT_SUFFIX, searchCondition.getActionName()));
		if (StringUtils.isNoneBlank(searchCondition.getField())) {
			queryBuilder.must(QueryBuilders.termsQuery(searchCondition.getField() + ResultCodeType.SPTLIT_SUFFIX, searchCondition.getValue()));
		}
		SearchResponse response = searchRequest.setQuery(queryBuilder).get();
		SearchHits searchHits = response.getHits();
		esResponse.setResultCode(ResultCodeType.SUCCESS[0]);
		esResponse.setResultMessage(ResultCodeType.SUCCESS[1]);
		esResponse.setResult(searchHits.getTotalHits());
		return esResponse;
	}

	/**
	 * 在响应中设置分页信息
	 * 
	 * @param searchResponse
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @param totalNum
	 *            总记录数
	 */
	private void paginationSet(EsSearchResponse searchResponse, Integer curPage, Integer pageSize, Integer totalNum) {
		searchResponse.setCurPage(curPage);// 当前页
		searchResponse.setPageSize(pageSize);
		searchResponse.setTotalNum(totalNum);
		// 总页数
		if (searchResponse.getTotalNum() % searchResponse.getPageSize() == 0) {
			searchResponse.setTotalPages(searchResponse.getTotalNum() / searchResponse.getPageSize());
		} else {
			searchResponse.setTotalPages(searchResponse.getTotalNum() / searchResponse.getPageSize() + 1);
		}
		// 首页
		searchResponse.setHomePage(0);
		// 末页
		searchResponse.setLastPage(searchResponse.getTotalPages());
		// 下一页
		if (searchResponse.getCurPage() == searchResponse.getTotalPages()) {
			searchResponse.setNextPage(searchResponse.getCurPage());
		} else {
			searchResponse.setNextPage(searchResponse.getCurPage() + 1);
		}
		// 上一页
		if (searchResponse.getCurPage() == 0) {
			searchResponse.setPrePage(searchResponse.getCurPage());
		} else {
			searchResponse.setPrePage(searchResponse.getCurPage() - 1);
		}
	}

}

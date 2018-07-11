/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.elasticsearch.test;

import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.elasticsearch.EsSearchResponse;
import com.sunshine.framework.elasticsearch.SearchCondition;
import com.sunshine.framework.elasticsearch.handler.impl.ElasticSearchHandler;
import com.sunshine.framework.kafka.appender.logback.LogBackEventObject;
import com.sunshine.framework.kafka.appender.logback.LogBackInfoObject;
import com.sunshine.framework.utils.Junit4SpringContextHolder;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch.test
 * @ClassName ESClientTest.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月7日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class ESClientTest extends Junit4SpringContextHolder {
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(ESClientTest.class);

	@Test
	public void saveTest() {
		ElasticSearchHandler handler = SpringContextHolder.getBean(ElasticSearchHandler.class);

		LogBackEventObject logBackEventObject = new LogBackEventObject();
		logBackEventObject.setLoggerName("elasticsearch.test.ESClientTest");
		java.util.GregorianCalendar now = new GregorianCalendar();
		String callerDate = FastDateFormat.getInstance("yyyy-MM-DD HH:mm:ss").format(now);
		logBackEventObject.setCallerDate(callerDate);
		logBackEventObject.setHostName("127.0.0.1");
		logBackEventObject.setInfoData("测试ElasticSearchHandler.insertData");

		LogBackInfoObject info = new LogBackInfoObject();
		String indexId = "00002";
		String indexName = "test_1";
		String indexType = "test";
		info.setIndexId(indexId);
		info.setIndexName(indexName);
		info.setIndexType(indexType);
		info.setLogBackEventObject(logBackEventObject);

		handler.insertData(info);

		SearchCondition<LogBackEventObject> searchCondition = new SearchCondition<>();
		searchCondition.setIndexName("other");
		searchCondition.setIndexId("124d83fe1fa64dab9f1bfeb8835b9a23");
		searchCondition.setIndexType("other");
		// searchCondition.setT((SourceData) SourceData.class.newInstance());//SourceData.class
		searchCondition.setOperationThreaded(true);
		EsSearchResponse response = handler.queryDataByIndexId(searchCondition);
		logger.info("getInfo:{}", JSON.toJSONString(response.getResult()));
	}

	@Test
	public void queryOpCount() {
		String actionName = "com.sunshine.mobileapp.biz.register.controller.RegisterChooseController.chooseHospital";
		ElasticSearchHandler handler = SpringContextHolder.getBean(ElasticSearchHandler.class);
		SearchCondition<LogBackEventObject> searchCondition = new SearchCondition<>();
		searchCondition.setIndexName("mobileapp.biz.register");
		searchCondition.setIndexType("mobileapp.biz.register");
		searchCondition.setActionName(actionName);
		EsSearchResponse objs = handler.queryDataByField(searchCondition);
		logger.info("getInfo:{}", JSON.toJSONString(objs.getResult()));
		// for (LogBackEventObject logObj : objs) {
		// logger.info(JSON.toJSONString(logObj));
		// }
	}

	@Test
	public void queryActionNameCount() {
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setPageSize(25);
		searchCondition.setCurPage(1);
		searchCondition.setIndexName("other");
		searchCondition.setIndexType("other");
		searchCondition.setField("hostName");
		searchCondition.setValue("tianzz");
		// searchCondition.setJsonObject("{" + "\"hostName\":\"tianzz\"" + "}");//"hostName":"tianzz"
		searchCondition.setActionName("org.springframework.core.io.support.PropertiesLoaderSupport.loadProperties");
		ElasticSearchHandler handler = SpringContextHolder.getBean(ElasticSearchHandler.class);
		EsSearchResponse response = handler.queryActionNameCount(searchCondition);
		System.out.println("按照actionName查询数量：" + response.getResult());
	}
}

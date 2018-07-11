/**
 * <html>
 * <body>
 *  <P>  Copyright 2017 阳光康众</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2017-3-20</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.kafka.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.common.threadpool.SimpleThreadFactory;
import com.sunshine.framework.kafka.KafkaContant;
import com.sunshine.framework.kafka.callable.LogDataCollectCall;
import com.sunshine.framework.kafka.config.KafkaConsumerConfig;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.task.system.collector
 * @ClassName LogDataCollector.java
 * @Description kafka日志数据采集
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class LogDataCollector implements InitializingBean, DisposableBean {
	private static Logger logger = LoggerFactory.getLogger(LogDataCollector.class);
	private ExecutorService logExecutor;
	private List<LogDataCollectCall> consumerCollectCalls = new ArrayList<>();

	@Override
	public void afterPropertiesSet() {
		KafkaConsumerConfig consumerConfig = SpringContextHolder.getBean(KafkaConsumerConfig.class);
		Boolean isValidate = isValidate(consumerConfig);
		if (!isValidate) {
			logger.error("日志数据采集参数不合法,强制取消!consumerConfig:{}", JSON.toJSONString(consumerConfig));
		} else {
			Integer partitionNum = consumerConfig.getPartitionNum();
			Integer pollNum = consumerConfig.getPollNum() == null ? KafkaContant.DEFAULT_DATA_POLL_NUM : consumerConfig.getPollNum();
			logExecutor = Executors.newFixedThreadPool(partitionNum, new SimpleThreadFactory("LogDataCollector"));
			for (int i = 0; i < partitionNum; i++) {
				LogDataCollectCall collectCall = new LogDataCollectCall(i, consumerConfig.buildOnlyTopicComsumer(Integer.valueOf(i)), pollNum);
				consumerCollectCalls.add(collectCall);
				logExecutor.submit(collectCall);
			}

			// Runtime.getRuntime().addShutdownHook(new Thread() {
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// for (LogDataCollectCall consumerCollectCall : consumerCollectCalls) {
			// consumerCollectCall.shutdown();
			// }
			// logExecutor.shutdown();
			// try {
			// logExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			//
			// });
		}
	}

	/**
	 * 验证是否缺失关键参数
	 * 
	 * @param consumerConfig
	 * @return
	 */
	private Boolean isValidate(KafkaConsumerConfig consumerConfig) {
		Boolean isValidate = true;
		if (CollectionUtils.isEmpty(consumerConfig.getTopicList())) {
			isValidate = false;
		}
		if (StringUtils.isBlank(consumerConfig.getGroupId())) {
			isValidate = false;
		}
		if (StringUtils.isBlank(consumerConfig.getBootstrapServers())) {
			isValidate = false;
		}
		if (consumerConfig.getPartitionNum() == null) {
			isValidate = false;
		}
		return isValidate;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		for (LogDataCollectCall consumerCollectCall : consumerCollectCalls) {
			consumerCollectCall.shutdown();
		}
		logExecutor.shutdown();
		try {
			logExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

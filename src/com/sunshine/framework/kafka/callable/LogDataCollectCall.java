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
package com.sunshine.framework.kafka.callable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.elasticsearch.SourceData;
import com.sunshine.framework.elasticsearch.handler.impl.ElasticSearchHandler;
import com.sunshine.framework.kafka.KafkaContant;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.task.system.callable
 * @ClassName LogDataCollectCall.java
 * @Description kafka获取日志信息到es
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class LogDataCollectCall implements Runnable {
	public static int DEFAULT_POLL_NUMBER = 100;
	private static Logger logger = LoggerFactory.getLogger(LogDataCollectCall.class);
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private KafkaConsumer<String, String> consumer;
	private Integer consumerId;
	private Integer pollNum;

	public LogDataCollectCall(Integer consumerId, KafkaConsumer<String, String> consumer, Integer pollNum) {
		super();
		this.consumerId = consumerId;
		this.consumer = consumer;
		this.pollNum = pollNum;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<SourceData> datas = new ArrayList<>();
		if (pollNum == null || pollNum.intValue() == 0) {
			logger.warn("kafka poolNum is invalid.used default pollNum:100");
			pollNum = DEFAULT_POLL_NUMBER;
		}
		try {
			while (!closed.get()) {
				try {
					ConsumerRecords<String, String> records = consumer.poll(KafkaContant.DEFAULT_DATA_POLL_NUM);
					if (records.count() > 0) {
						logger.info("partitionNum:{},poll num records count:{}", consumerId, records.count());
						Long lastOffset = null;
						for (TopicPartition partition : records.partitions()) {
							List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
							for (ConsumerRecord<String, String> record : partitionRecords) {
								SourceData sourceData = JSON.parseObject(record.value(), SourceData.class);
								datas.add(sourceData);
							}
							// 消费者自己手动提交消费的offest,确保消息正确处理后再提交
							lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
							consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
						}

						if (!CollectionUtils.isEmpty(datas)) {
							ElasticSearchHandler handler = SpringContextHolder.getBean(ElasticSearchHandler.class);
							handler.batchInsertData(datas, KafkaContant.DEFAULT_DATA_POLL_NUM);
						}
					} else {
						logger.info("partitionNum:{},poll num records count:0", consumerId);
					}
					Thread.sleep(30000);
				} catch (Exception e) {
					// TODO: handle exception
					// e.printStackTrace();
					logger.error("op kafka client error.case:{}", e.getMessage());
				}
			}
		} finally {
			consumer.close();
		}

	}

	public void shutdown() {
		closed.set(true);
		consumer.wakeup();
	}

	public KafkaConsumer<String, String> getConsumer() {
		return consumer;
	}

	public void setConsumer(KafkaConsumer<String, String> consumer) {
		this.consumer = consumer;
	}

	public Integer getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}
}

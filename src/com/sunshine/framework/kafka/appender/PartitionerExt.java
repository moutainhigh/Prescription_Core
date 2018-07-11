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
package com.sunshine.framework.kafka.appender;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.kafka.appender
 * @ClassName PartitionerExt.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yuce@sunshine.com.cn
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class PartitionerExt implements Partitioner {
	private static Logger logger = LoggerFactory.getLogger(PartitionerExt.class);
	private final AtomicLong counter = new AtomicLong(0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.kafka.common.Configurable#configure(java.util.Map)
	 */
	@Override
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.kafka.clients.producer.Partitioner#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.kafka.clients.producer.Partitioner#partition(java.lang.String, java.lang.Object, byte[], java.lang.Object, byte[],
	 * org.apache.kafka.common.Cluster)
	 */
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		// TODO Auto-generated method stub
		// logger.info("topic:{},key:{},value:{}", new Object[] { topic, key, value });
		int partitionCount = cluster.partitionCountForTopic(topic);
		int partition = 0;
		long hashVal = 0;
		if (key == null) {
			key = counter.getAndIncrement();
		}

		if (key instanceof Number) {
			hashVal = Integer.parseInt(key.toString());
		} else {
			hashVal = key.toString().hashCode();
		}

		// 斐波那契（Fibonacci）散列
		hashVal = (hashVal * 2654435769L) >> 28;

		// 避免hashVal超出 MAX_VALUE = 0x7fffffff时变为负数,取绝对值
		partition = Integer.valueOf(Math.abs(hashVal) % partitionCount + "");
		if (logger.isDebugEnabled()) {
			logger.debug("Partitioner-partition:{},key:{},value:{}", partition, key, value);
		}
		return partition;
	}
}

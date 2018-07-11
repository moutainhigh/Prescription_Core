/**
 * 
 */
package com.sunshine.framework.elasticsearch.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch.pool
 * @ClassName ESClientPoolAbstract.java
 * @Description es连接池抽象类
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class ESClientPoolAbstract extends Pool<TransportClient> {
	public ESClientPoolAbstract() {
		super();
	}

	public ESClientPoolAbstract(GenericObjectPoolConfig poolConfig, PooledObjectFactory<TransportClient> factory) {
		super(poolConfig, factory);
	}

	@Override
	public void returnBrokenResource(TransportClient client) {
		super.returnBrokenResource(client);
	}

	@Override
	public void returnResource(TransportClient client) {
		super.returnResource(client);
	}
}

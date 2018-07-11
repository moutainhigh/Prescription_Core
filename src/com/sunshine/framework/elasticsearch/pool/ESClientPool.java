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
package com.sunshine.framework.elasticsearch.pool;

import java.util.GregorianCalendar;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.transport.TransportClient;

import com.sunshine.framework.elasticsearch.ESClientFactory;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch.pool
 * @ClassName ESClientPool.java
 * @Description Elasticsearch client 连接池
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class ESClientPool extends ESClientPoolAbstract {
	public static final String VALIDATE_TYPE = "validate";
	public static final String VALIDATE_INDEX_NAME = VALIDATE_TYPE.concat(String.valueOf(new GregorianCalendar().getTimeInMillis()));

	/**
	 * 
	 * @param poolConfig
	 *            连接池配置
	 * @param clusterName
	 *            ES实例的名称
	 * @param clientTransportSniff
	 *            是否自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
	 * @param clusterAddresses
	 *            实例的地址(集群使用逗号隔开) 如： 192.168.200.224:9200,192.168.200.225:9200
	 */
	public ESClientPool(final GenericObjectPoolConfig poolConfig, final String clusterName, final Boolean clientTransportSniff,
			final String clusterAddresses) {
		super(poolConfig, new ESClientFactory(clusterName, clientTransportSniff, clusterAddresses));
		getResource().prepareIndex(VALIDATE_INDEX_NAME, VALIDATE_TYPE).setSource(VALIDATE_INDEX_NAME).execute().actionGet();
	}

	@Override
	public TransportClient getResource() {
		TransportClient transportClient = super.getResource();
		return transportClient;
	}

	@Override
	public void returnBrokenResource(final TransportClient resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	@Override
	public void returnResource(final TransportClient resource) {
		if (resource != null) {
			try {
				returnResourceObject(resource);
			} catch (Exception e) {
				returnBrokenResource(resource);
				throw new ElasticsearchException("Could not return the resource to the pool", e);
			}
		}
	}
}

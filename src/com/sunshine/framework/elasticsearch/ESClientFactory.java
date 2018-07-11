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
package com.sunshine.framework.elasticsearch;

import java.net.InetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.framework.elasticsearch.exceptions.ElasticSearchException;
import com.sunshine.framework.elasticsearch.pool.ESClientPool;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch
 * @ClassName ESClientFactory.java
 * @Description es client 工厂类
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class ESClientFactory implements PooledObjectFactory<TransportClient> {
	private static Logger logger = LoggerFactory.getLogger(ESClientFactory.class);
	private static final String DEFAULT_CLUSTER_NAME = "sunshine-es";
	private static final String SPLIT_CLUSTER_ADDRESS_CHAR = ",";
	private static final String SPLIT_IP_PORT_CHAR = ":";

	/**
	 * 设置ES实例的名称
	 */
	private final String clusterName;

	/**
	 * 是否自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
	 */
	private final Boolean clientTransportSniff;

	/**
	 * 实例的地址(集群使用逗号隔开) 如： 192.168.200.224:9200,192.168.200.225:9200
	 */
	private final String clusterAddresses;

	public ESClientFactory(final String clusterName, final Boolean clientTransportSniff, final String clusterAddresses) {
		super();
		if (StringUtils.isBlank(clusterName)) {
			logger.error("parameter clusterName is null,used default clusterName:{}", DEFAULT_CLUSTER_NAME);
			this.clusterName = DEFAULT_CLUSTER_NAME;
		} else {
			this.clusterName = clusterName;
		}
		if (clientTransportSniff == null) {
			this.clientTransportSniff = true;
		} else {
			this.clientTransportSniff = clientTransportSniff;
		}
		if (StringUtils.isBlank(clusterAddresses)) {
			logger.error("elasticsearch clent create fail.clusterAddresses:{}", clusterAddresses);
			throw new ElasticSearchException("elasticsearch clent create fail ,clusterAddresses is invalid.");
		} else {
			this.clusterAddresses = clusterAddresses;
		}
	}

	/**
	 * 激活对象,当Pool中决定移除一个对象交付给调用者时额外的激活操作,比如可以在activateObject方法中"重置"参数列表让调用者使用时感觉像一个"新创建"的对象一样; <br>
	 * 如果object是一个线程,可以在"激活"操作中重置"线程中断标记",或者让线程从阻塞中唤醒等; <br>
	 * 如果object是一个socket,那么可以在"激活操作"中刷新通道,或者对socket进行链接重建(假如socket意外关闭)等.
	 * 
	 * @see org.apache.commons.pool2.PooledObjectFactory#activateObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public void activateObject(PooledObject<TransportClient> clientPool) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 销毁对象,如果对象池中检测到某个"对象"idle的时间超时,或者操作者向对象池"归还对象"时检测到"对象"已经无效,那么此时将会导致"对象销毁"; <br>
	 * "销毁对象"的操作设计相差甚远,但是必须明确:当调用此方法时,"对象"的生命周期必须结束.如果object是线程,那么此时线程必须退出; <br>
	 * 如果object是socket操作,那么此时socket必须关闭;如果object是文件流操作,那么此时"数据flush"且正常关闭.
	 * 
	 * @see org.apache.commons.pool2.PooledObjectFactory#destroyObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public void destroyObject(PooledObject<TransportClient> clientPool) throws Exception {
		// TODO Auto-generated method stub
		final TransportClient client = clientPool.getObject();
		client.close();

	}

	/**
	 * 创建一个新对象;当对象池中的对象个数不足时,将会使用此方法来"输出"一个新的"对象",并交付给对象池管理. (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool2.PooledObjectFactory#makeObject()
	 */
	@Override
	public PooledObject<TransportClient> makeObject() throws Exception {
		// TODO Auto-generated method stub
		Settings esSettings = Settings.builder().put("cluster.name", clusterName.trim()) // 设置ES实例的名称
				.put("client.transport.sniff", clientTransportSniff) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
				.build();
		TransportClient transportClient = new PreBuiltTransportClient(esSettings);
		String[] transportAddresses = clusterAddresses.split(SPLIT_CLUSTER_ADDRESS_CHAR);
		for (String transportAddress : transportAddresses) {
			String[] addressParam = transportAddress.split(SPLIT_IP_PORT_CHAR);
			if (addressParam.length != 2) {
				logger.error("clusterAddresses is invalid.clusterAddresses:{}", clusterAddresses);
				transportClient.close();
				throw new ElasticsearchException("clusterAddresses is invalid.clusterAddresses:" + clusterAddresses);
			} else {
				transportClient.addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(addressParam[0].trim()), Integer.valueOf(addressParam[1].trim())));
			}

		}

		return new DefaultPooledObject<>(transportClient);
	}

	/**
	 * "钝化"对象,当调用者"归还对象"时,Pool将会"钝化对象";钝化的言外之意,就是此"对象"暂且需要"休息"一下.<br>
	 * 如果object是一个socket,那么可以passivateObject中清除buffer,将socket阻塞;<br>
	 * 如果object是一个线程,可以在"钝化"操作中将线程sleep或者将线程中的某个对象wait.需要注意的时,activateObject和passivateObject两个方法需要对应,避免死锁或者"对象"状态的混乱.
	 * 
	 * @see org.apache.commons.pool2.PooledObjectFactory#passivateObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public void passivateObject(PooledObject<TransportClient> clientPool) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 检测对象是否"有效";Pool中不能保存无效的"对象",因此"后台检测线程"会周期性的检测Pool中"对象"的有效性, <br>
	 * 如果对象无效则会导致此对象从Pool中移除,并destroy;此外在调用者从Pool获取一个"对象"时,也会检测"对象"的 <br>
	 * 有效性,确保不能讲"无效"的对象输出给调用者;当调用者使用完毕将"对象归还"到Pool时,仍然会检测对象的有效性.<br>
	 * 所谓有效性,就是此"对象"的状态是否符合预期,是否可以对调用者直接使用;如果对象是Socket,那么它的有效性就是socket的通道是否畅通/阻塞是否超时等.
	 * 
	 * @see org.apache.commons.pool2.PooledObjectFactory#validateObject(org.apache.commons.pool2.PooledObject)
	 */
	@Override
	public boolean validateObject(PooledObject<TransportClient> clientPool) {
		// TODO Auto-generated method stub
		final TransportClient client = clientPool.getObject();
		try {
			SearchResponse response = client.prepareSearch(ESClientPool.VALIDATE_INDEX_NAME).setTypes(ESClientPool.VALIDATE_TYPE)
					.setQuery(QueryBuilders.queryStringQuery(ESClientPool.VALIDATE_INDEX_NAME)).get();
			if (response != null) {
				return true;
			} else {
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
	}

}

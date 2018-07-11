package com.sunshine.framework.kafka.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.sunshine.framework.kafka.KafkaContant;

/**
 * @Package org.elasticsearch.river.kafka
 * @ClassName KafkaClient
 * @Statement <p>
 *            kafka连接配置信息类
 *            </p>
 * @JDK version used: 1.7
 * @Author: 于策
 * @Create Date: 2017-3-20
 * @modify-Author:
 * @modify-Date:
 * @modify-Why/What:
 * @Version 1.0
 */
public class KafkaConsumerConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8534711072942394776L;

	/**
	 * zk连接地址
	 */
	private String bootstrapServers;

	/**
	 * 消费组
	 */
	private String groupId;

	/**
	 * 是否自动提交offset
	 */
	private String enableAutoCommit;

	/**
	 * 自动提交offset的间隔
	 */
	private String autoCommitIntervalMS;

	/**
	 * 连接超时时间
	 */
	private String sessionTimeoutMS;

	private String keyDeserializer;

	private String valueDeserializer;

	/**
	 * 消费主题列表
	 */
	private String topics;
	private List<String> topicList;

	/**
	 * 每次消费数量
	 */
	private Integer pollNum;

	/**
	 * topic的分区数量
	 */
	public Integer partitionNum;

	public KafkaConsumerConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KafkaConsumerConfig(String bootstrapServers, String groupId, String enableAutoCommit, String autoCommitIntervalMS,
			String sessionTimeoutMS, String keyDeserializer, String valueDeserializer, String topics, Integer partitionNum, Integer pollNum) {
		super();
		this.bootstrapServers = bootstrapServers;
		this.groupId = groupId;
		this.enableAutoCommit = enableAutoCommit;
		this.autoCommitIntervalMS = autoCommitIntervalMS;
		this.sessionTimeoutMS = sessionTimeoutMS;
		this.keyDeserializer = keyDeserializer;
		this.valueDeserializer = valueDeserializer;
		this.topics = topics;
		this.pollNum = pollNum;
		this.partitionNum = partitionNum;
	}

	public KafkaConsumer<String, String> buildComsumer() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, getGroupId());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, StringUtils.isNotBlank(getEnableAutoCommit()) ? getEnableAutoCommit()
				: KafkaContant.DEFAULT_ENABLE_AUTO_COMMIT);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, StringUtils.isNotBlank(getAutoCommitIntervalMS()) ? getAutoCommitIntervalMS()
				: KafkaContant.DEFAULT_COMMIT_INTERVAL_MS);

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringUtils.isNotBlank(getKeyDeserializer()) ? getKeyDeserializer()
				: KafkaContant.DEFAULT_KEY_DESERIALIZER);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringUtils.isNotBlank(getValueDeserializer()) ? getValueDeserializer()
				: KafkaContant.DEFAULT_VALUE_DESERIALIZER);

		// this.requestTimeoutMs > sessionTimeOutMs this.requestTimeoutMs > fetchMaxWaitMs 为保证启动此3参数全部使用默认配置
		props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, KafkaContant.DEFAULT_REQUEST_TIMEOUT_MS);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, KafkaContant.DEFAULT_SESSION_TIMEOUT_MS);
		props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, KafkaContant.DEFAULT_FETCH_MAX_WAIT_MS);
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(getTopicList());
		return consumer;
	}

	/**
	 * 构建单个主题 指定分区 如果配置多个主题,只取第一个主题
	 * 
	 * @Description
	 * @param partitionNum
	 * @return
	 * @date 2017年4月14日
	 */
	public KafkaConsumer<String, String> buildOnlyTopicComsumer(Integer partitionNum) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, getGroupId());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, StringUtils.isNotBlank(getEnableAutoCommit()) ? getEnableAutoCommit()
				: KafkaContant.DEFAULT_ENABLE_AUTO_COMMIT);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, StringUtils.isNotBlank(getAutoCommitIntervalMS()) ? getAutoCommitIntervalMS()
				: KafkaContant.DEFAULT_COMMIT_INTERVAL_MS);

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringUtils.isNotBlank(getKeyDeserializer()) ? getKeyDeserializer()
				: KafkaContant.DEFAULT_KEY_DESERIALIZER);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringUtils.isNotBlank(getValueDeserializer()) ? getValueDeserializer()
				: KafkaContant.DEFAULT_VALUE_DESERIALIZER);

		// this.requestTimeoutMs > sessionTimeOutMs this.requestTimeoutMs > fetchMaxWaitMs 为保证启动此3参数全部使用默认配置
		props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, KafkaContant.DEFAULT_REQUEST_TIMEOUT_MS);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, KafkaContant.DEFAULT_SESSION_TIMEOUT_MS);
		props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, KafkaContant.DEFAULT_FETCH_MAX_WAIT_MS);
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		TopicPartition partition = new TopicPartition(getTopicList().get(0), partitionNum);
		consumer.assign(Arrays.asList(partition));
		return consumer;
	}

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getEnableAutoCommit() {
		return enableAutoCommit;
	}

	public void setEnableAutoCommit(String enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
	}

	public String getAutoCommitIntervalMS() {
		return autoCommitIntervalMS;
	}

	public void setAutoCommitIntervalMS(String autoCommitIntervalMS) {
		this.autoCommitIntervalMS = autoCommitIntervalMS;
	}

	public String getSessionTimeoutMS() {
		return sessionTimeoutMS;
	}

	public void setSessionTimeoutMS(String sessionTimeoutMS) {
		this.sessionTimeoutMS = sessionTimeoutMS;
	}

	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	public void setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
	}

	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public void setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
	}

	public String getTopics() {
		return topics;
	}

	public Integer getPollNum() {
		return pollNum;
	}

	public void setPollNum(Integer pollNum) {
		this.pollNum = pollNum;
	}

	public Integer getPartitionNum() {
		return partitionNum;
	}

	public void setPartitionNum(Integer partitionNum) {
		this.partitionNum = partitionNum;
	}

	public List<String> getTopicList() {
		if (StringUtils.isNotBlank(topics)) {
			topicList = Arrays.asList(topics.split(","));
		} else {
			topicList = new ArrayList<String>();
		}
		return topicList;
	}

	public void setTopicList(List<String> topicList) {
		this.topicList = topicList;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

}

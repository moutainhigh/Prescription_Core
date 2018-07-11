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
package com.sunshine.framework.mvc.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.sunshine.framework.mvc.mongodb.config.MongoDBConfig;

/**
 * @Project Core_PlatForm
 * @Package mongodb
 * @ClassName MongoDBFactory.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月26日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class MongoDBFactory {
	private MongoDBConfig config;
	private static MongoClient client;
	public static String DEFAULT_DATA_BASE;

	private void init() {
		// 构建server列表
		List<ServerAddress> serverList = new ArrayList<ServerAddress>();
		if (StringUtils.isNotBlank(config.getServerAddresses())) {
			String[] serverAddressArray = config.getServerAddresses().split(",");
			for (String serverAddress : serverAddressArray) {
				if (StringUtils.isBlank(serverAddress) || serverAddress.indexOf(":") < 0 || serverAddress.split(":").length != 2) {
					throw new MongoException("serverAddress is invalid for mongoDB connection.serverAddress:" + serverAddress);
				} else {
					ServerAddress server = new ServerAddress(serverAddress.split(":")[0], Integer.valueOf(serverAddress.split(":")[1]));
					serverList.add(server);
				}
			}
		} else {
			throw new MongoException("not found address config for mongoDB connection!");
		}
		if (serverList.size() == 0) {
			throw new MongoException("not config serverAddress for mongoDB connection!");
		}

		// 构建鉴权信息
		List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
		credentialsList.add(MongoCredential.createScramSha1Credential(config.getUser(), config.getDataBase(), config.getPassWord().toCharArray()));

		// 构建操作选项,默认参数满足大多数场景
		MongoClientOptions options = MongoClientOptions.builder().sslEnabled(config.getSslEnabled())
				.connectionsPerHost(config.getConnectionsPerHost())
				.threadsAllowedToBlockForConnectionMultiplier(config.getThreadsAllowedToBlockForConnectionMultiplier())
				.maxWaitTime(config.getMaxWaitTime()).connectTimeout(config.getConnectTimeout()).socketTimeout(config.getSocketTimeout()).build();
		client = new MongoClient(serverList, credentialsList, options);
		DEFAULT_DATA_BASE = config.getDataBase();
	}

	public MongoClient createClient() {
		if (client == null) {
			init();
		}
		return client;
	}

	public MongoDBConfig getConfig() {
		return config;
	}

	public void setConfig(MongoDBConfig config) {
		this.config = config;
	}
}

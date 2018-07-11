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
package com.sunshine.framework.mvc.mongodb.config;

import java.io.Serializable;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package mongodb
 * @ClassName MongoDBConfig.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月26日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class MongoDBConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4141295350425580293L;
	/**
	 * 服务器实际建立的连接数量<br>
	 * connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量
	 */
	private Integer connectionsPerHost = 200;

	/**
	 * 每个连接上可以排队等待的线程数量<br>
	 * connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量, Default is 5.
	 */
	private Integer threadsAllowedToBlockForConnectionMultiplier = 5;

	/**
	 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间<br>
	 * e.g:当前数据库的连接都在使用中，线程T10尝试访问数据库。此时T10会在某个connection上排队等待。<br>
	 * 如果超过maxWaitTime都没有获取到这个连接的话。该线程就会抛出Exception（此处为巨坑，一定要注意）。<br>
	 * 所以maxWaitTime一定要设置的足够大，以免由于排队线程过多造成的访问数据库失败的情况。 <br>
	 * Default is 120,000. A value of 0 means that it will not wait
	 */
	private Integer maxWaitTime = 120000;

	/**
	 * 与数据库建立连接的timeout, Default is 10000毫秒
	 */
	private Integer connectTimeout = 10000;

	/**
	 * 数据库连接读取和写入数据的timeout,0 means no timeout
	 */
	private Integer socketTimeout = 0;

	private Boolean sslEnabled = false;

	private String user;
	private String dataBase;
	private String passWord;

	private String serverAddresses;

	public MongoDBConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MongoDBConfig(String user, String dataBase, String passWord, String serverAddresses) {
		super();
		this.user = user;
		this.dataBase = dataBase;
		this.passWord = passWord;
		this.serverAddresses = serverAddresses;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @Description 服务器列表 比如:192.168.0.1:27017,192.168.0.2:27017
	 * @return
	 * @date 2017年5月27日
	 */
	public String getServerAddresses() {
		return serverAddresses;
	}

	/**
	 * @Description 服务器列表 比如:192.168.0.1:27017,192.168.0.2:27017
	 * @return
	 * @date 2017年5月27日
	 */
	public void setServerAddresses(String serverAddresses) {
		this.serverAddresses = serverAddresses;
	}

	/**
	 * 
	 * 服务器实际建立的连接数量,default 200<br>
	 * connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量
	 *
	 * @Description
	 * @return
	 * @date 2017年5月27日
	 */
	public Integer getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(Integer connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}

	/**
	 * 
	 * @Description 每个连接上可以排队等待的线程数量<br>
	 *              connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量, Default is 5.
	 * @return
	 * @date 2017年5月27日
	 */
	public Integer getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}

	/**
	 * @Description 每个连接上可以排队等待的线程数量<br>
	 *              connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量, Default is 5.
	 * @param threadsAllowedToBlockForConnectionMultiplier
	 * @date 2017年5月27日
	 */
	public void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}

	/**
	 * @Description 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间 <br>
	 *              Default is 120,000. A value of 0 means that it will not wait
	 * @return
	 * @date 2017年5月27日
	 */
	public Integer getMaxWaitTime() {
		return maxWaitTime;
	}

	/**
	 * @Description 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间 <br>
	 *              Default is 120,000. A value of 0 means that it will not wait
	 * @param maxWaitTime
	 * @date 2017年5月27日
	 */
	public void setMaxWaitTime(Integer maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	/**
	 * @Description 与数据库建立连接的timeout, Default is 10,000
	 * @return
	 * @date 2017年5月27日
	 */
	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * @Description 与数据库建立连接的timeout, Default is 10,000
	 * @param connectTimeout
	 * @date 2017年5月27日
	 */
	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * @Description 数据库连接读取和写入数据的timeout,0 means no timeout.default is 0
	 * @return
	 * @date 2017年5月27日
	 */
	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * @Description 数据库连接读取和写入数据的timeout,0 means no timeout.default is 0
	 * @param socketTimeout
	 * @date 2017年5月27日
	 */
	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Boolean getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(Boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}
}

/**
 * 
 */
package com.sunshine.framework.elasticsearch.pool;

import java.io.Serializable;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch.pool
 * @ClassName ESClientPoolConfig.java
 * @Description ES client连接池配置
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class ESClientPoolConfig extends GenericObjectPoolConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6762968058895230090L;

	/**
	 * 链接池中最大连接数,默认为50.
	 */
	private int maxTotal = 50;

	/**
	 * 链接池中最大空闲的连接数,默认为10.
	 */
	private int maxIdle = 10;

	/**
	 * 连接池中最少空闲的连接数,默认为0
	 */
	private int minIdle = 0;

	/**
	 * 当连接池资源耗尽时，调用者最大阻塞的时间，超时将跑出异常。单位，毫秒数;默认为-1.表示永不超时
	 */
	private long maxWaitMillis = 60000;

	/**
	 * 连接空闲的最小时间，达到此值后空闲连接将可能会被移除。负值(-1)表示不移除
	 */
	private long minEvictableIdleTimeMillis = 60000;

	/**
	 * 对于空闲链接检测线程而言,每次检测的链接资源的个数。默认为3
	 */
	private int numTestsPerEvictionRun = 3;

	/**
	 * 向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
	 */
	private boolean testOnBorrow = false;

	/**
	 * 向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值
	 */
	private boolean testOnReturn = false;

	/**
	 * 向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值
	 */
	private boolean testWhileIdle = true;

	/**
	 * 空闲链接检测线程,检测的周期,毫秒数.如果为负值,表示不运行检测线程。默认为-1
	 */
	private long timeBetweenEvictionRunsMillis = 30000;

	/**
	 * 当连接池中active数量达到阀值时，即链接资源耗尽时，连接池需要采取的手段, 默认为1. <br>
	 * 0 : 抛出异常 <br>
	 * 1 : 阻塞,直到有可用链接资源<br>
	 * 2 : 强制创建新的链接资源<br>
	 * private int whenExhaustedAction;
	 */

	public ESClientPoolConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMaxTotal() {
		return maxTotal;
	}

	@Override
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	@Override
	public int getMaxIdle() {
		return maxIdle;
	}

	@Override
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	@Override
	public int getMinIdle() {
		return minIdle;
	}

	@Override
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	@Override
	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	@Override
	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	@Override
	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	@Override
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	@Override
	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	@Override
	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	@Override
	public boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	@Override
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	@Override
	public boolean getTestOnReturn() {
		return testOnReturn;
	}

	@Override
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	@Override
	public boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	@Override
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	@Override
	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	@Override
	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}
}

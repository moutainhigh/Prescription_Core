/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年2月17日</p>
 *  <p> Created by 申姜</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.threadpool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Project: sunshine_health_payment_platform 
 * @Package: com.sunshine.framework.common.threadpool
 * @ClassName: SpringThreadPoolExecutor
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 申姜
 * @Create Date: 2017年2月17日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class SimpleThreadPoolExecutor {
	private static Logger logger = LoggerFactory.getLogger(SimpleThreadPoolExecutor.class);
	/**
	 * 核心线程数
	 */
	private int corePoolSize = 10;
	/**
	 * 最大线程数
	 */
	private int maxPoolSize = 20;
	/**
	 * 任务队列大小
	 */
	private int queueCapacity = 1000;
	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	private int keepAliveSeconds = 100;
	/**
	 * 线程池名称
	 */
	private String threadPoolName = "threadPool";
	/**
	 * 线程池饱和时默认处理策略:调用者的线程会执行该任务,如果执行器已关闭,则丢弃
	 */
	private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
	/**
	 * JDK原生线程池对象
	 */
	private ThreadPoolExecutor threadPoolExecutor;
	/**
	 * spring线程池对象
	 */
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public SimpleThreadPoolExecutor() {
		this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		this.threadPoolTaskExecutor.setCorePoolSize(this.corePoolSize);
		this.threadPoolTaskExecutor.setMaxPoolSize(this.maxPoolSize);
		this.threadPoolTaskExecutor.setQueueCapacity(this.queueCapacity);
		this.threadPoolTaskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
		this.threadPoolTaskExecutor.setBeanName(this.threadPoolName);
		this.threadPoolTaskExecutor.setRejectedExecutionHandler(this.rejectedExecutionHandler);
		this.threadPoolTaskExecutor.initialize();
		this.threadPoolExecutor = this.threadPoolTaskExecutor.getThreadPoolExecutor();
	}

	/**
	 * @param threadPoolName
	 */
	public SimpleThreadPoolExecutor(String threadPoolName) {
		super();
		this.threadPoolName = threadPoolName;
		this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		this.threadPoolTaskExecutor.setBeanName(this.threadPoolName);
		this.threadPoolTaskExecutor.setCorePoolSize(this.corePoolSize);
		this.threadPoolTaskExecutor.setMaxPoolSize(this.maxPoolSize);
		this.threadPoolTaskExecutor.setQueueCapacity(this.queueCapacity);
		this.threadPoolTaskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
		this.threadPoolTaskExecutor.setRejectedExecutionHandler(this.rejectedExecutionHandler);
		this.threadPoolTaskExecutor.initialize();
		this.threadPoolExecutor = this.threadPoolTaskExecutor.getThreadPoolExecutor();
	}

	/**
	 * @param corePoolSize
	 * @param maxPoolSize
	 * @param threadPoolName
	 */
	public SimpleThreadPoolExecutor(String threadPoolName, int corePoolSize, int maxPoolSize) {
		super();
		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
		this.threadPoolName = threadPoolName;
		this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		this.threadPoolTaskExecutor.setBeanName(this.threadPoolName);
		this.threadPoolTaskExecutor.setCorePoolSize(this.corePoolSize);
		this.threadPoolTaskExecutor.setMaxPoolSize(this.maxPoolSize);
		this.threadPoolTaskExecutor.setQueueCapacity(this.queueCapacity);
		this.threadPoolTaskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
		this.threadPoolTaskExecutor.setRejectedExecutionHandler(this.rejectedExecutionHandler);
		this.threadPoolTaskExecutor.initialize();
		this.threadPoolExecutor = this.threadPoolTaskExecutor.getThreadPoolExecutor();
	}

	/**
	 * @param corePoolSize
	 * @param maxPoolSize
	 * @param queueCapacity
	 * @param keepAliveSeconds
	 * @param threadPoolName
	 * @param rejectedExecutionHandler
	 * @param threadPoolExecutor
	 * @param threadPoolTaskExecutor
	 */
	public SimpleThreadPoolExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds, String threadPoolName,
			RejectedExecutionHandler rejectedExecutionHandler) {
		super();
		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
		this.queueCapacity = queueCapacity;
		this.keepAliveSeconds = keepAliveSeconds;
		this.threadPoolName = threadPoolName;
		this.rejectedExecutionHandler = rejectedExecutionHandler;
		this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		this.threadPoolTaskExecutor.setBeanName(this.threadPoolName);
		this.threadPoolTaskExecutor.setCorePoolSize(this.corePoolSize);
		this.threadPoolTaskExecutor.setMaxPoolSize(this.maxPoolSize);
		this.threadPoolTaskExecutor.setQueueCapacity(this.queueCapacity);
		this.threadPoolTaskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
		this.threadPoolTaskExecutor.setRejectedExecutionHandler(this.rejectedExecutionHandler);
		this.threadPoolTaskExecutor.initialize();
		this.threadPoolExecutor = this.threadPoolTaskExecutor.getThreadPoolExecutor();
	}

	/**
	 * 获取JDK原生线程池对象
	 * @return
	 */
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return this.threadPoolExecutor;
	}

	/**
	 * 获取spring线程池对象
	 * @return
	 */
	public ThreadPoolTaskExecutor geThreadPoolTaskExecutor() {
		return this.threadPoolTaskExecutor;
	}

	private String getThreadPoolExecutorInfo() {
		// 线程池大小 当前工作线程数量
		int poolSize = this.threadPoolExecutor.getPoolSize();
		// 正在运行的任务
		int activeCount = this.threadPoolExecutor.getActiveCount();
		// 运行结束的任务
		long completedTaskCount = this.threadPoolExecutor.getCompletedTaskCount();
		// 任务总数
		long taskCount = this.threadPoolExecutor.getTaskCount();
		// 等待运行的 任务队列里的任务数量
		long taskWaitCount = taskCount - completedTaskCount - activeCount;
		return new StringBuffer().append("当前工作线程数量=").append(poolSize).append(",正在运行的任务=").append(activeCount).append(",运行结束的任务=")
				.append(completedTaskCount).append(",任务总数=").append(taskCount).append(",等待运行的任务数量=").append(taskWaitCount).toString();
	}

	/**
	 * @return the corePoolSize
	 */
	public int getCorePoolSize() {
		return corePoolSize;
	}

	/**
	 * @param corePoolSize the corePoolSize to set
	 */
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	/**
	 * @return the queueCapacity
	 */
	public int getQueueCapacity() {
		return queueCapacity;
	}

	/**
	 * @param queueCapacity the queueCapacity to set
	 */
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the keepAliveSeconds
	 */
	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	/**
	 * @param keepAliveSeconds the keepAliveSeconds to set
	 */
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	/**
	 * @return the threadPoolName
	 */
	public String getThreadPoolName() {
		return threadPoolName;
	}

	/**
	 * @param threadPoolName the threadPoolName to set
	 */
	public void setThreadPoolName(String threadPoolName) {
		this.threadPoolName = threadPoolName;
	}

	/**
	 * @return the rejectedExecutionHandler
	 */
	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return rejectedExecutionHandler;
	}

	/**
	 * @param rejectedExecutionHandler the rejectedExecutionHandler to set
	 */
	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
	}

}

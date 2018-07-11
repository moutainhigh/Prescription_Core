/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年6月26日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */

package com.sunshine.framework.common.threadpool;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.sunshine.framework.common.threadpool.FixedThreadPoolExecutor;
import com.sunshine.framework.common.threadpool.SimpleThreadFactory;
import com.sunshine.framework.common.threadpool.Tasker;

/**
 * 固定大小的线程池,防止线程无限制增长 需要注意控制任务队列的大小,避免系统资源被耗尽
 * @Package: com.sunshine.framework.common.threadpool
 * @ClassName: FixedThreadPoolExecutor
 * @Statement: <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */

public class FixedThreadPoolExecutor implements Serializable {
	private static final long serialVersionUID = -7335649496979582087L;
	protected static Logger logger = Logger.getLogger(FixedThreadPoolExecutor.class);
	/**
	 * 默认线程池大小
	 */
	private int nThreads = 10;
	/**
	 * 任务队列最大容量
	 */
	private int maxTaskSize;
	/**
	 * 线程名称
	 */
	private String threadPoolName;
	/**
	 * 线程池
	 */
	private ThreadPoolExecutor threadPoolExecutor;
	/**
	 * 任务队列
	 */
	private BlockingQueue<Runnable> workQueue;
	/**
	 * 任务队列饱和时处理策略
	 */
	private RejectedExecutionHandler handler;

	public FixedThreadPoolExecutor() {
		// 线程池中所保存的线程数，包括空闲线程。
		int corePoolSize = this.nThreads;
		// 线程池中允许的最大线程数。
		int maximumPoolSize = this.nThreads;
		long keepAliveTime = 0L;
		// 无界队列.其实是有界的,其默认值为Integer.MAX_VALUE个大小.
		this.workQueue = new LinkedBlockingDeque<Runnable>();
		// 用于被拒绝任务的处理程序，它直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务。
		// 意思就是如果任务被拒绝,则使用调用者的线程来执行该任务
		this.handler = new ThreadPoolExecutor.AbortPolicy();
		// 固定大小的线程池,corePoolSize和maximumPoolSize保持一致,当使用无界队列（例如，不具有预定义容量的 LinkedBlockingQueue）将导致在所有 corePoolSize
		// 线程都忙时新任务在队列中等待。这样，创建的线程就不会超过 corePoolSize。（因此，maximumPoolSize的值也就无效了。）
		this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, handler);
	}

	/**
	 * 
	 * @param nThreads
	 *            线程池大小
	 * @param threadPoolName
	 *            线程名称
	 */
	public FixedThreadPoolExecutor(int nThreads, String threadPoolName) {
		this.nThreads = nThreads;
		this.threadPoolName = threadPoolName;
		int corePoolSize = this.nThreads;
		int maximumPoolSize = this.nThreads;
		long keepAliveTime = 0L;
		this.workQueue = new LinkedBlockingDeque<Runnable>();
		this.handler = new ThreadPoolExecutor.CallerRunsPolicy();
		this.threadPoolExecutor =
				new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, new SimpleThreadFactory(
						this.threadPoolName), handler);
	}

	/**
	 * 
	 * @param nThreads
	 *            线程池大小
	 * @param maxTaskSize
	 *            任务队列大小
	 */
	public FixedThreadPoolExecutor(int nThreads, int maxTaskSize) {
		this.nThreads = nThreads;
		this.maxTaskSize = maxTaskSize;
		int corePoolSize = this.nThreads;
		int maximumPoolSize = this.nThreads;
		long keepAliveTime = 0L;
		this.workQueue = new LinkedBlockingDeque<Runnable>(this.maxTaskSize);
		this.handler = new ThreadPoolExecutor.CallerRunsPolicy();
		this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, handler);
	}

	/**
	 * 
	 * @param nThreads
	 *            线程池大小
	 * @param maxTaskSize
	 *            任务队列大小
	 * @param threadPoolName
	 *            线程名称
	 */
	public FixedThreadPoolExecutor(int nThreads, int maxTaskSize, String threadPoolName) {
		this.nThreads = nThreads;
		this.threadPoolName = threadPoolName;
		this.maxTaskSize = maxTaskSize;
		int corePoolSize = this.nThreads;
		int maximumPoolSize = this.nThreads;
		long keepAliveTime = 0L;
		this.workQueue = new LinkedBlockingDeque<Runnable>(this.maxTaskSize);
		this.handler = new ThreadPoolExecutor.CallerRunsPolicy();
		this.threadPoolExecutor =
				new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, new SimpleThreadFactory(
						this.threadPoolName), handler);
	}

	/**
	 * 执行任务
	 * 
	 * @param tasker
	 */
	public void execute(Tasker tasker) {
		this.threadPoolExecutor.execute(tasker);
		if (logger.isInfoEnabled()) {
			// logger.info("thread:" + Thread.currentThread().getName() + "" + Thread.currentThread().getId());
			logger.info(getThreadPoolExecutorInfo(this.threadPoolExecutor));
		}
	}

	public void execute(Runnable tasker) {
		logger.info(getThreadPoolExecutorInfo(this.threadPoolExecutor));
		this.threadPoolExecutor.execute(tasker);
	}

	/**
	 * 获取当前ThreadPoolExecutor的信息
	 * 
	 * @param pool
	 * @return
	 */
	private String getThreadPoolExecutorInfo(ThreadPoolExecutor pool) {
		// 线程池大小 当前工作线程数量
		int poolSize = pool.getPoolSize();
		// 正在运行的任务
		int activeCount = pool.getActiveCount();
		// 运行结束的任务
		long completedTaskCount = pool.getCompletedTaskCount();
		// 任务总数
		long taskCount = pool.getTaskCount();
		// 等待运行的 任务队列里的任务数量
		long taskWaitCount = taskCount - completedTaskCount - activeCount;
		return new StringBuffer().append("当前工作线程数量=").append(poolSize).append(",正在运行的任务=").append(activeCount).append(",运行结束的任务=").append(completedTaskCount)
				.append(",任务总数=").append(taskCount).append(",等待运行的任务数量=").append(taskWaitCount).toString();
	}

	/**
	 * @return the nThreads
	 */

	public int getnThreads() {
		return nThreads;
	}

	/**
	 * @param nThreads
	 *            the nThreads to set
	 */

	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	/**
	 * @return the maxTaskSize
	 */

	public int getMaxTaskSize() {
		return maxTaskSize;
	}

	/**
	 * @param maxTaskSize
	 *            the maxTaskSize to set
	 */

	public void setMaxTaskSize(int maxTaskSize) {
		this.maxTaskSize = maxTaskSize;
	}

	/**
	 * @return the threadPoolName
	 */

	public String getThreadPoolName() {
		return threadPoolName;
	}

	/**
	 * @param threadPoolName
	 *            the threadPoolName to set
	 */

	public void setThreadPoolName(String threadPoolName) {
		this.threadPoolName = threadPoolName;
	}

	/**
	 * @return the threadPoolExecutor
	 */

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	/**
	 * @param threadPoolExecutor
	 *            the threadPoolExecutor to set
	 */

	public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}

	/**
	 * @return the workQueue
	 */

	public BlockingQueue<Runnable> getWorkQueue() {
		return workQueue;
	}

	/**
	 * @param workQueue
	 *            the workQueue to set
	 */

	public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
		this.workQueue = workQueue;
	}

	/**
	 * @return the handler
	 */

	public RejectedExecutionHandler getHandler() {
		return handler;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */

	public void setHandler(RejectedExecutionHandler handler) {
		this.handler = handler;
	}

}

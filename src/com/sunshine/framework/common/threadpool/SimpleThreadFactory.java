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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.sunshine.framework.common.threadpool.SimpleThreadFactory;

/**
 * threadFactory简单实现,改写自DefaultThreadFactory,使线程具有更有意义的名称
 * @Package: com.sunshine.framework.common.threadpool
 * @ClassName: SimpleThreadFactory
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
public class SimpleThreadFactory implements ThreadFactory {
	protected static Logger logger = Logger.getLogger(SimpleThreadFactory.class);
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	final ThreadGroup group;
	final AtomicInteger threadNumber = new AtomicInteger(1);
	final String namePrefix;

	public SimpleThreadFactory() {
		SecurityManager s = System.getSecurityManager();
		group = ( s != null ) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	public SimpleThreadFactory(String threadPoolName) {
		SecurityManager s = System.getSecurityManager();
		group = ( s != null ) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = threadPoolName + "-" + poolNumber.getAndIncrement() + "-thread-";
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

}

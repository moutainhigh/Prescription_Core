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
package com.sunshine.framework.interceptor;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.sunshine.framework.interceptor.MethodTimeAdviceInterceptor;

/**
 * 方法执行时长统计
 * @Package: com.sunshine.framework.interceptor
 * @ClassName: MethodTimeAdviceInterceptor
 * @Statement: <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By: long
 * @modify Date: 2017-3-17
 * @Why&What is modify: 成员变量tt改为线程局部变量
 * @Version: 1.0
 */
public class MethodTimeAdviceInterceptor implements MethodBeforeAdvice, AfterReturningAdvice {
	private static final Logger logger = LoggerFactory.getLogger(MethodTimeAdviceInterceptor.class);
	// long tt; 并发环境，数据不安全，改为：
	private static ThreadLocal<Long> tt= new ThreadLocal<Long>();

	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		tt.set(System.currentTimeMillis());
	}

	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
		long lastTime = System.currentTimeMillis() - tt.get();
		StringBuffer buf =
				new StringBuffer("EXECUTE:(").append(arg1.getName()).append(") BEGIN=").append(tt.get()).append(", END=")
						.append(System.currentTimeMillis()).append(", ELAPSE=").append(lastTime);
		if (logger.isDebugEnabled()) {
			logger.debug(buf.toString());
		}
	}
}

/**
 * <html>
 * <body>
 *  <P>  Copyright 2017 阳光康众</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2017-2-22</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.kafka.appender.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @Package ch.qos.logback.ext.formatter
 * @ClassName LogFormatter
 * @Statement <p>
 *            日志格式化接口
 *            </p>
 * @JDK version used: 1.7
 * @Author: 于策
 * @Create Date: 2017-2-22
 * @modify-Author:
 * @modify-Date:
 * @modify-Why/What:
 * @Version 1.0
 */
public interface LogFormatter {
	public String formate(ILoggingEvent event);
}

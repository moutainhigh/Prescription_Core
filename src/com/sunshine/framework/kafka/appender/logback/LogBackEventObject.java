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
package com.sunshine.framework.kafka.appender.logback;

import java.io.Serializable;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @Package kafka.appender.logback
 * @ClassName SourceObject
 * @Statement <p>
 *            </p>
 * @JDK version used: 1.7
 * @Author: 于策
 * @Create Date: 2017-2-22
 * @modify-Author:
 * @modify-Date:
 * @modify-Why/What:
 * @Version 1.0
 */
public class LogBackEventObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1743694023452760288L;

	/**
	 * 日志产生的时间yyyy-MM-DD HH:mm:ss
	 */
	protected String callerDate;

	/**
	 * 日志生成的IP或主机名称
	 */
	protected String hostName;

	/**
	 * 日志信息
	 */
	protected String infoData;

	/**
	 * 日志名称
	 */
	protected String loggerName;

	/**
	 * 操作名称
	 */
	protected String actionName;

	/**
	 * 执行操作的线程名称
	 */
	protected String threadName;

	public LogBackEventObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LogBackEventObject(ILoggingEvent eventObject) {
		loggerName = eventObject.getLoggerName();
		java.util.GregorianCalendar now = new GregorianCalendar();
		now.setTimeInMillis(eventObject.getTimeStamp());
		callerDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(now);
		hostName = eventObject.getLoggerContextVO().getPropertyMap().get("HOSTNAME");
		infoData = eventObject.getFormattedMessage();
		threadName = eventObject.getThreadName();
		StackTraceElement[] elements = eventObject.getCallerData();
		if (elements != null && elements.length > 0) {
			if (StringUtils.isNotBlank(elements[0].getClassName()) && StringUtils.isNotBlank(elements[0].getMethodName())) {
				actionName = elements[0].getClassName().concat(".").concat(elements[0].getMethodName());
			}
		}
	}

	public String getCallerDate() {
		return callerDate;
	}

	public void setCallerDate(String callerDate) {
		this.callerDate = callerDate;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInfoData() {
		return infoData;
	}

	public void setInfoData(String infoData) {
		this.infoData = infoData;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
}

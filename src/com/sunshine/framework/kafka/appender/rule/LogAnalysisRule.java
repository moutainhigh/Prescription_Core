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
package com.sunshine.framework.kafka.appender.rule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package kafka.appender.rule
 * @ClassName LogAnalysisRule.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月21日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class LogAnalysisRule implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3725669678560758503L;
	private Map<String, String> analysisRules = new HashMap<String, String>();
	private static final String DEFAULT_LOG_TYPE = "other";
	private static final String DEFAULT_LOG_NAME = "其它";

	public LogAnalysisRule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LogAnalysisRule(Map<String, String> analysisRules) {
		super();
		this.analysisRules = analysisRules;
	}

	public Map<String, String> getAnalysisRules() {
		return analysisRules;
	}

	public void setAnalysisRules(Map<String, String> analysisRules) {
		this.analysisRules = analysisRules;
	}

	public String analysisLogType(String loggerName) {
		String logType = DEFAULT_LOG_TYPE;
		for (String ruleKey : analysisRules.keySet()) {
			if (loggerName.indexOf(ruleKey) > -1) {
				logType = ruleKey;
				break;
			}
		}
		return logType;
	}

	public String analysisLogName(String logType) {
		if (StringUtils.isBlank(logType) || logType.equalsIgnoreCase(DEFAULT_LOG_TYPE)) {
			return DEFAULT_LOG_NAME;
		} else {
			return analysisRules.get(logType);
		}
	}
}

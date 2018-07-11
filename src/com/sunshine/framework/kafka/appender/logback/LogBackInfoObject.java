package com.sunshine.framework.kafka.appender.logback;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.elasticsearch.SourceData;
import com.sunshine.framework.kafka.appender.rule.LogAnalysisRule;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogBackInfoObject extends SourceData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7510905487866571012L;

	private LogBackEventObject logBackEventObject;

	private LogAnalysisRule analysisRule = SpringContextHolder.getBean(LogAnalysisRule.class);

	public LogBackInfoObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LogBackInfoObject(ILoggingEvent event) {
		logBackEventObject = new LogBackEventObject(event);
		indexType = analysisRule.analysisLogType(logBackEventObject.getLoggerName());
		indexName = analysisRule.analysisLogType(indexType);
		indexId = PKGenerator.generateId();
	}

	@Override
	public String getIndexName() {
		return indexName;
	}

	@Override
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	@Override
	public String getIndexType() {
		return indexType;
	}

	@Override
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	@Override
	public String getIndexId() {
		return indexId;
	}

	@Override
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(logBackEventObject);
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	public LogBackEventObject getLogBackEventObject() {
		return logBackEventObject;
	}

	public void setLogBackEventObject(LogBackEventObject logBackEventObject) {
		this.logBackEventObject = logBackEventObject;
	}
}

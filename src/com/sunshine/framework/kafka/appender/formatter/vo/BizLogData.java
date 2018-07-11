package com.sunshine.framework.kafka.appender.formatter.vo;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.elasticsearch.SourceData;
import com.sunshine.framework.kafka.appender.rule.LogAnalysisRule;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.dataanalysis.vo.biz
 * @ClassName BizLogData.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月4日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class BizLogData extends SourceData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3636050031796047924L;

	private BizEventObject bizEventObject;

	private LogAnalysisRule analysisRule = SpringContextHolder.getBean(LogAnalysisRule.class);

	public BizLogData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BizLogData(ILoggingEvent event) {
		bizEventObject = new BizEventObject(event);
		indexType = analysisRule.analysisLogType(bizEventObject.getLoggerName());
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
		return JSON.toJSONString(bizEventObject);
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	public BizEventObject getBizEventObject() {
		return bizEventObject;
	}

	public void setBizEventObject(BizEventObject bizEventObject) {
		this.bizEventObject = bizEventObject;
	}
}

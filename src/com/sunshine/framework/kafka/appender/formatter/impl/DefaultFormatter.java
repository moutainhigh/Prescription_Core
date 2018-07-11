package com.sunshine.framework.kafka.appender.formatter.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.elasticsearch.SourceData;
import com.sunshine.framework.kafka.appender.formatter.LogFormatter;
import com.sunshine.framework.kafka.appender.logback.LogBackInfoObject;

public class DefaultFormatter implements LogFormatter {
	@Override
	public String formate(ILoggingEvent eventObject) {
		// TODO Auto-generated method stub
		SourceData sourceData = new LogBackInfoObject(eventObject);
		return JSON.toJSONString(sourceData);
	}

}

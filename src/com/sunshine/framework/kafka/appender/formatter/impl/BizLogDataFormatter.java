package com.sunshine.framework.kafka.appender.formatter.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.alibaba.fastjson.JSON;
import com.sunshine.framework.elasticsearch.SourceData;
import com.sunshine.framework.kafka.appender.formatter.LogFormatter;
import com.sunshine.framework.kafka.appender.formatter.vo.BizLogData;

public class BizLogDataFormatter implements LogFormatter {
	@Override
	public String formate(ILoggingEvent event) {
		// TODO Auto-generated method stub
		SourceData sourceData = new BizLogData(event);
		return JSON.toJSONString(sourceData);
	}

}

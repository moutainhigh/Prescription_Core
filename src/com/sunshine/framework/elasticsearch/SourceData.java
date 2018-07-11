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
package com.sunshine.framework.elasticsearch;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package elasticsearch
 * @ClassName SourceObject.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年4月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class SourceData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2130078587434465526L;

	private static final String DEFAULT_INFO_KEY = "infoData";

	protected String indexName;

	protected String indexType;

	protected String indexId;

	protected String source;

	public SourceData(String indexName, String indexType, String indexId, String source) {
		super();
		this.indexName = indexName;
		this.indexType = indexType;
		this.indexId = indexId;
		this.source = source;
	}

	public SourceData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String infoData() {
		if (StringUtils.isNotBlank(source)) {
			Map<String, String> dataMap = JSON.parseObject(source, new TypeReference<Map<String, String>>() {
			});
			return dataMap.get(DEFAULT_INFO_KEY);
		} else {
			return null;
		}
	}
}

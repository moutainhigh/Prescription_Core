/**
 * <html>
 * <body>
 *  <P>  Copyright 2017 阳光康众</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2017-3-20</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.kafka;

/**
 * @Package org.elasticsearch.river.kafka
 * @ClassName KafkaContant
 * @Statement
 *            <p>
 *            </p>
 * @JDK version used 1.7
 * @Author 于策
 * @Create Date 2017-3-20
 * @modify-Author
 * @modify-Date
 * @modify-Why/What:
 * @Version 1.0
 */
public class KafkaContant {
	public static final String DEFAULT_KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
	public static final String DEFAULT_VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
	public static final String DEFAULT_ENABLE_AUTO_COMMIT = "false";
	public static final String DEFAULT_COMMIT_INTERVAL_MS = "1000";
	public static final String DEFAULT_SESSION_TIMEOUT_MS = "40000";
	public static final String DEFAULT_REQUEST_TIMEOUT_MS = "60000";
	public static final String DEFAULT_FETCH_MAX_WAIT_MS = "40000";

	public static final Integer DEFAULT_DATA_POLL_NUM = 100;

}

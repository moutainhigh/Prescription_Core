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
package com.sunshine.framework.kafka.appender;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.log4j.helpers.LogLog;
import org.springframework.util.CollectionUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.sunshine.framework.kafka.appender.formatter.LogFormatter;
import com.sunshine.framework.kafka.appender.formatter.impl.DefaultFormatter;

/**
 * @Package kafka.appender
 * @ClassName KafkaAppender
 * @Statement <p>
 *            扩展logback Appender 使日志直接写入kafka 参考官方org.apache.kafka.log4jappender.KafkaLog4jAppender实现
 *            </p>
 * @JDK version used: 1.7
 * @Author: 于策
 * @Create Date: 2017-2-22
 * @modify-Author:
 * @modify-Date:
 * @modify-Why/What:
 * @Version 1.0
 */
public class KafkaLogbackAppender extends AppenderBase<ILoggingEvent> {
	private static final ConcurrentHashMap<String, Integer> levelMap = new ConcurrentHashMap<String, Integer>();
	private static final String BOOTSTRAP_SERVERS_CONFIG = ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
	private static final String COMPRESSION_TYPE_CONFIG = ProducerConfig.COMPRESSION_TYPE_CONFIG;
	private static final String ACKS_CONFIG = ProducerConfig.ACKS_CONFIG;
	private static final String RETRIES_CONFIG = ProducerConfig.RETRIES_CONFIG;
	private static final String KEY_SERIALIZER_CLASS_CONFIG = ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
	private static final String VALUE_SERIALIZER_CLASS_CONFIG = ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
	private static final String SECURITY_PROTOCOL = CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
	private static final String SSL_TRUSTSTORE_LOCATION = SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
	private static final String SSL_TRUSTSTORE_PASSWORD = SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
	private static final String SSL_KEYSTORE_TYPE = SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
	private static final String SSL_KEYSTORE_LOCATION = SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
	private static final String SSL_KEYSTORE_PASSWORD = SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
	private static final String SASL_KERBEROS_SERVICE_NAME = SaslConfigs.SASL_KERBEROS_SERVICE_NAME;
	private static final String PARTITIONER_CLASS_CONFIG = ProducerConfig.PARTITIONER_CLASS_CONFIG;
	private static final String BATCH_SIZE_CONFIG = ProducerConfig.BATCH_SIZE_CONFIG;

	private static String FORMATTERS_SPLIT_CHAR = ",";
	private static String FORMATTER_PACKAGE_SPLIT_PACKAGE_CHAR = "\\|";
	private static String FORMATTER_CLASSNAME_SPLIT_PACKAGES_CHAR = "-";

	private String brokerList = null;
	private String topic = null;
	private String compressionType = null;
	private String securityProtocol = null;
	private String sslTruststoreLocation = null;
	private String sslTruststorePassword = null;
	private String sslKeystoreType = null;
	private String sslKeystoreLocation = null;
	private String sslKeystorePassword = null;
	private String saslKerberosServiceName = null;
	private String clientJaasConfPath = null;
	private String kerb5ConfPath = null;
	private String validPrefix;

	private int retries = 0;
	private int requiredNumAcks = Integer.MAX_VALUE;
	private boolean syncSend = false;
	private Producer<String, String> producer = null;

	private String formatters;
	private static ConcurrentHashMap<String, String> formatterMap = new ConcurrentHashMap<String, String>();

	public static String level;

	public static final String LOG_OFF = "OFF";
	public static final String LOG_ERROR = "ERROR";
	public static final String LOG_WARN = "WARN";
	public static final String LOG_INFO = "INFO";
	public static final String LOG_DEBUG = "DEBUG";
	public static final String LOG_TRACE = "TRACE";
	public static final String LOG_ALL = "ALL";

	static {
		levelMap.put(LOG_OFF, 2147483647);
		levelMap.put(LOG_ERROR, 40000);
		levelMap.put(LOG_WARN, 30000);
		levelMap.put(LOG_INFO, 20000);
		levelMap.put(LOG_DEBUG, 10000);
		levelMap.put(LOG_TRACE, 5000);
		levelMap.put(LOG_ALL, -2147483648);
	}

	public Producer<String, String> getProducer() {
		return producer;
	}

	protected Producer<String, String> getKafkaProducer(Properties props) {
		return new KafkaProducer<String, String>(props);
	}

	private LogFormatter queryLogFormatter(String loggerName) {
		LogFormatter formatter = null;
		if (StringUtils.isNotBlank(loggerName)) {
			if (!CollectionUtils.isEmpty(formatterMap)) {
				String formatterClass = null;
				for (String loggerPreix : formatterMap.keySet()) {
					if (loggerName.indexOf(loggerPreix) > -1) {
						formatterClass = formatterMap.get(loggerPreix);
						if (StringUtils.isNotBlank(formatterClass)) {
							try {
								formatter = (LogFormatter) Class.forName(formatterClass).newInstance();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								formatter = null;
							}
						}
					}
				}
			}
		}

		if (formatter == null) {
			formatter = new DefaultFormatter();
		}
		return formatter;
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		// TODO Auto-generated method stub
		Boolean isAppend = isNeedAppend(eventObject);

		if (isAppend) {
			LogFormatter formatter = queryLogFormatter(eventObject.getLoggerName());

			String logInfo = formatter.formate(eventObject);
			Long time = System.currentTimeMillis();

			Future<RecordMetadata> response = producer.send(new ProducerRecord<String, String>(topic, time.toString(), logInfo));
			if (syncSend) {
				try {
					response.get();
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				} catch (ExecutionException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		Properties props = new Properties();
		if (brokerList != null)
			props.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
		if (props.isEmpty())
			throw new ConfigException("The bootstrap servers property should be specified");
		if (topic == null)
			throw new ConfigException("Topic must be specified by the Kafka log4j appender");
		if (compressionType != null)
			props.put(COMPRESSION_TYPE_CONFIG, compressionType);
		if (requiredNumAcks != Integer.MAX_VALUE)
			props.put(ACKS_CONFIG, Integer.toString(requiredNumAcks));
		if (retries > 0)
			props.put(RETRIES_CONFIG, retries);
		if (securityProtocol != null) {
			props.put(SECURITY_PROTOCOL, securityProtocol);
		}
		if (securityProtocol != null && securityProtocol.contains("SSL") && sslTruststoreLocation != null && sslTruststorePassword != null) {
			props.put(SSL_TRUSTSTORE_LOCATION, sslTruststoreLocation);
			props.put(SSL_TRUSTSTORE_PASSWORD, sslTruststorePassword);

			if (sslKeystoreType != null && sslKeystoreLocation != null && sslKeystorePassword != null) {
				props.put(SSL_KEYSTORE_TYPE, sslKeystoreType);
				props.put(SSL_KEYSTORE_LOCATION, sslKeystoreLocation);
				props.put(SSL_KEYSTORE_PASSWORD, sslKeystorePassword);
			}
		}
		if (securityProtocol != null && securityProtocol.contains("SASL") && saslKerberosServiceName != null && clientJaasConfPath != null) {
			props.put(SASL_KERBEROS_SERVICE_NAME, saslKerberosServiceName);
			System.setProperty("java.security.auth.login.config", clientJaasConfPath);
			if (kerb5ConfPath != null) {
				System.setProperty("java.security.krb5.conf", kerb5ConfPath);
			}
		}
		if (StringUtils.isBlank(level)) {
			level = LOG_ALL;
		}

		// 默认是16384 16KB 此处设置为1KB
		props.put(BATCH_SIZE_CONFIG, 1024);
		// props.put("buffer.memory", 33554432);
		// compression.type producer所使用的压缩器 目前支持gzip, snappy和lz4
		// org.apache.kafka.common.serialization.StringSerializer
		props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		// props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put(PARTITIONER_CLASS_CONFIG, PartitionerExt.class.getName()); // 可以不设置
		this.producer = getKafkaProducer(props);
		LogLog.debug("Kafka producer connected to " + brokerList);
		LogLog.debug("Logging for topic: " + topic);

		//
		if (StringUtils.isNotBlank(formatters)) {
			String[] formatterStrs = formatters.split(FORMATTERS_SPLIT_CHAR);
			for (String formatterStr : formatterStrs) {
				if (StringUtils.isNotBlank(formatterStr)) {
					String[] formatterParams = formatterStr.split(FORMATTER_CLASSNAME_SPLIT_PACKAGES_CHAR);
					if (formatterParams.length > 1 && StringUtils.isNotBlank(formatterParams[0]) && StringUtils.isNotBlank(formatterParams[1])) {
						String[] packageNames = formatterParams[1].split(FORMATTER_PACKAGE_SPLIT_PACKAGE_CHAR);
						for (String packageName : packageNames) {
							if (StringUtils.isNotBlank(packageName)) {
								formatterMap.put(packageName.trim(), formatterParams[0].trim());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 根据日志Level判断是否输出日志
	 * 
	 * @param eventObject
	 * @return
	 */
	private Boolean isNeedAppend(ILoggingEvent eventObject) {
		Boolean isAppend = false;

		// 判断是否是系统需要的有效日志
		String loggerName = eventObject.getLoggerName();

		if (StringUtils.isNotBlank(validPrefix)) {
			String[] prefixs = validPrefix.split(",");
			if (prefixs.length > 1) {
				for (String prefix : prefixs) {
					if (loggerName.indexOf(prefix) > -1) {
						isAppend = true;
						break;
					}
				}
			} else {
				if (loggerName.indexOf(validPrefix) > -1) {
					isAppend = true;
				}
			}
		} else {
			isAppend = true;
		}

		// 根据配置的日志级别过滤不需要的日志
		if (isAppend) {
			Level lv = eventObject.getLevel();
			if (LOG_ALL.equalsIgnoreCase(level) || LOG_TRACE.equalsIgnoreCase(level)) {
				isAppend = true;
			} else if (LOG_OFF.equalsIgnoreCase(level)) {
				isAppend = false;
			} else if (LOG_DEBUG.equalsIgnoreCase(level)) {
				if (lv.levelInt > levelMap.get(LOG_TRACE)) {
					isAppend = true;
				} else {
					isAppend = false;
				}
			} else if (LOG_INFO.equalsIgnoreCase(level)) {
				if (lv.levelInt > levelMap.get(LOG_DEBUG)) {
					isAppend = true;
				} else {
					isAppend = false;
				}
			} else if (LOG_WARN.equalsIgnoreCase(level)) {
				if (lv.levelInt > levelMap.get(LOG_INFO)) {
					isAppend = true;
				} else {
					isAppend = false;
				}
			} else if (LOG_ERROR.equalsIgnoreCase(level)) {
				if (lv.levelInt > levelMap.get(LOG_WARN)) {
					isAppend = true;
				} else {
					isAppend = false;
				}
			} else {
				isAppend = false;
			}
		}
		return isAppend;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
		this.producer.close();
	}

	public String getBrokerList() {
		return brokerList;
	}

	public void setBrokerList(String brokerList) {
		this.brokerList = brokerList;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(String compressionType) {
		this.compressionType = compressionType;
	}

	public String getSecurityProtocol() {
		return securityProtocol;
	}

	public void setSecurityProtocol(String securityProtocol) {
		this.securityProtocol = securityProtocol;
	}

	public String getSslTruststoreLocation() {
		return sslTruststoreLocation;
	}

	public void setSslTruststoreLocation(String sslTruststoreLocation) {
		this.sslTruststoreLocation = sslTruststoreLocation;
	}

	public String getSslTruststorePassword() {
		return sslTruststorePassword;
	}

	public void setSslTruststorePassword(String sslTruststorePassword) {
		this.sslTruststorePassword = sslTruststorePassword;
	}

	public String getSslKeystoreType() {
		return sslKeystoreType;
	}

	public void setSslKeystoreType(String sslKeystoreType) {
		this.sslKeystoreType = sslKeystoreType;
	}

	public String getSslKeystoreLocation() {
		return sslKeystoreLocation;
	}

	public void setSslKeystoreLocation(String sslKeystoreLocation) {
		this.sslKeystoreLocation = sslKeystoreLocation;
	}

	public String getSslKeystorePassword() {
		return sslKeystorePassword;
	}

	public void setSslKeystorePassword(String sslKeystorePassword) {
		this.sslKeystorePassword = sslKeystorePassword;
	}

	public String getSaslKerberosServiceName() {
		return saslKerberosServiceName;
	}

	public void setSaslKerberosServiceName(String saslKerberosServiceName) {
		this.saslKerberosServiceName = saslKerberosServiceName;
	}

	public String getClientJaasConfPath() {
		return clientJaasConfPath;
	}

	public void setClientJaasConfPath(String clientJaasConfPath) {
		this.clientJaasConfPath = clientJaasConfPath;
	}

	public String getKerb5ConfPath() {
		return kerb5ConfPath;
	}

	public void setKerb5ConfPath(String kerb5ConfPath) {
		this.kerb5ConfPath = kerb5ConfPath;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getRequiredNumAcks() {
		return requiredNumAcks;
	}

	public void setRequiredNumAcks(int requiredNumAcks) {
		this.requiredNumAcks = requiredNumAcks;
	}

	public boolean isSyncSend() {
		return syncSend;
	}

	public void setSyncSend(boolean syncSend) {
		this.syncSend = syncSend;
	}

	public void setProducer(Producer<String, String> producer) {
		this.producer = producer;
	}

	public static String getLevel() {
		return level;
	}

	public String getValidPrefix() {
		return validPrefix;
	}

	public void setValidPrefix(String validPrefix) {
		this.validPrefix = validPrefix;
	}

	public static void setLevel(String level) {
		KafkaLogbackAppender.level = level;
	}

	public String getFormatters() {
		return formatters;
	}

	public void setFormatters(String formatters) {
		this.formatters = formatters;
	}
}

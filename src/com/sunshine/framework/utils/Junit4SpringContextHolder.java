package com.sunshine.framework.utils;

import java.net.URL;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.log.LogbackConfigurer;

/**
 * spring集成Junit测试框架上下文环境配置,单元测试时只需继承该助手类即可.
 * 
 * @author 申午武
 * @version 1.0
 * @since 2015年2月11日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-framework.xml" })
public class Junit4SpringContextHolder extends AbstractJUnit4SpringContextTests {

	public Junit4SpringContextHolder() {
		super();
		// TODO Auto-generated constructor stub
		try {
			URL u = ClassLoader.getSystemResource("");
			LogbackConfigurer.initLogging(u.getPath() + "logback/logback.xml");
		} catch (Exception ex) {
			System.err.println("Cannot Initialize logback");
		}
	}

	@Before
	public void initApplicationContext() {
		SpringContextHolder.setApplicationContextTest(applicationContext);
		SystemConfig.loadSystemConfig();
	}

	public <T> T getBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public ApplicationContext getContext() {
		return applicationContext;
	}

}

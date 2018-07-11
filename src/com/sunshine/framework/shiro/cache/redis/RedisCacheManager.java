/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月12日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.shiro.cache.redis;

import org.apache.shiro.ShiroException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.parser.ParserConfig;
import com.sunshine.framework.shiro.cache.redis.RedisCache;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.shiro.cache.redis
 * @ClassName RedisCacheManager.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月12日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Service(value = "shiroRedisCacheManager")
public class RedisCacheManager implements CacheManager, Initializable, Destroyable {
	@Autowired
	private RedisCache shiroRedisCache;

	@Override
	public <K, V> Cache<K, V> getCache(String arg0) throws CacheException {
		// TODO Auto-generated method stub
		return shiroRedisCache;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		shiroRedisCache.clear();
	}

	@Override
	public void init() throws ShiroException {
		// TODO Auto-generated method stub
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
	}

}

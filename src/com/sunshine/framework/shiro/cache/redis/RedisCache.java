package com.sunshine.framework.shiro.cache.redis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunshine.framework.shiro.cache.redis.RedisCache;
import com.sunshine.framework.cache.redis.RedisConstant;
import com.sunshine.framework.cache.redis.RedisService;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.shiro.cache.redis
 * @ClassName RedisCache.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月13日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@SuppressWarnings("unchecked")
@Service(value = "shiroRedisCache")
public class RedisCache<K, V> implements Cache<K, V> {
	private static Logger logger = LoggerFactory.getLogger(RedisCache.class);

	private static final String SHIRO_CACHE_KEY = "shiro.data.cache";

	@Override
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		redisService.del(SHIRO_CACHE_KEY);
		logger.info("权限系统缓存全部清除！");
	}

	@Override
	public V get(K key) throws CacheException {
		// TODO Auto-generated method stub
		try {
			if (key == null) {
				return null;
			} else {
				RedisService redisService = SpringContextHolder.getBean(RedisService.class);
				String jsonData = redisService.hget(SHIRO_CACHE_KEY, key.toString());
				if (RedisConstant.isHadValue(jsonData)) {
					V value = (V) JSON.parse(jsonData);
					return value;
				} else {
					return null;
				}
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		return (Set<K>) redisService.hkeys(SHIRO_CACHE_KEY);
	}

	@Override
	public V put(K key, V value) throws CacheException {
		// TODO Auto-generated method stub
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		V previous = null;// get(key);
		SerializerFeature[] featureArr = { SerializerFeature.WriteClassName };
		redisService.hset(SHIRO_CACHE_KEY, key.toString(), JSON.toJSONString(value, featureArr));
		return previous;
	}

	@Override
	public V remove(K key) throws CacheException {
		// TODO Auto-generated method stub
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		V previous = null;// get(key);
		redisService.hdel(SHIRO_CACHE_KEY, key.toString());
		return previous;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		return redisService.hkeys(SHIRO_CACHE_KEY).size();
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		RedisService redisService = SpringContextHolder.getBean(RedisService.class);
		Collection<V> collection = new ArrayList<>();
		List<String> jsonList = redisService.hvals(SHIRO_CACHE_KEY);
		for (String jsonData : jsonList) {
			if (RedisConstant.isHadValue(jsonData)) {
				collection.add((V) JSON.parse(jsonData));
			}
		}
		return null;
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> getGenericClass() {
		Type t = getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return ((Class<?>) p[0]);
		}
		return null;
	}

}

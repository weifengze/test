package com.zicms.framework.shiro.redis;

import com.zicms.commons.constant.ShiroConstants;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @program: bjjh-demo
 * @Description: RedisCache
 * @Author: 魏丰泽
 * @Create: 2020-07-29 12:49
 **/
public class RedisCache<K, V> implements Cache<K, V> {
    /**
     * 缓存的超时时间，单位为s
     */
    private long expireTime;

    /**
     * 通过构造方法注入该对象
     */
    private RedisTemplate<K, V> redisTemplate;

    public RedisCache() {
        super();
    }

    public RedisCache(long expireTime, RedisTemplate<K, V> redisTemplate) {
        super();
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }

    public String cacheKey(String cacheName, K key) {
        return keyPrefix(cacheName) + key;
    }

    public String keyPrefix(String cacheName) {
        return cacheName + ":";
    }

    /**
     * 通过key来获取对应的缓存对象
     */
    @Override
    public V get(K key) throws CacheException {
        return redisTemplate.opsForValue().get(cacheKey(ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID, key));
    }

    /**
     * 将权限信息加入缓存中
     */
    @Override
    public V put(K key, V value) throws CacheException {
        if ("loginRecordCache".equals(ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID)) {
            redisTemplate.opsForValue().set((K) cacheKey(ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID, key), value, 600, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set((K) cacheKey(ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID, key), value, expireTime, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 将权限信息从缓存中删除
     */
    @Override
    public V remove(K key) throws CacheException {
        V v = redisTemplate.opsForValue().get(cacheKey(ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID, key));
        redisTemplate.opsForValue().getOperations().delete((K) cacheKey(ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID, key));
        return v;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}

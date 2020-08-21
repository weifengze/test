package com.zicms.framework.redis

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import java.util.*
import javax.annotation.Resource


@Configuration
@EnableCaching
open class RedisConfig : CachingConfigurerSupport() {

    @Resource
    private val lettuceConnectionFactory: LettuceConnectionFactory? = null

    @Bean
    open fun cacheManager(redisConnectionFactory: RedisConnectionFactory?): CacheManager? {
        val config = RedisCacheConfiguration.defaultCacheConfig() //key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer())) //value序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues() //缓存过期时间
                .entryTtl(Duration.ofSeconds(30 * 60))
        val builder = RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .withInitialCacheConfigurations(getRedisCacheConfigurationMap())
        return builder.build()
    }

    private fun keySerializer(): RedisSerializer<String>? {
        return StringRedisSerializer()
    }

    private fun valueSerializer(): RedisSerializer<Any>? {
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        val om = ObjectMapper()
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        jackson2JsonRedisSerializer.setObjectMapper(om)
        return jackson2JsonRedisSerializer

        // 设置序列化 两种方式区别不大
//	    return new GenericJackson2JsonRedisSerializer();
    }


    private fun getRedisCacheConfigurationMap(): Map<String, RedisCacheConfiguration>? {
        val redisCacheConfigurationMap: MutableMap<String, RedisCacheConfiguration> = HashMap()
        /**
         * @CacheConfig(cacheNames = "SsoCache")
         * public class SsoCache{
         * @Cacheable(keyGenerator = "cacheKeyGenerator")
         * public String getTokenByGsid(String gsid)
         * }
         * //二者选其一,可以使用value上的信息，来替换类上cacheNames的信息
         * @Cacheable(value = "BasicDataCache",keyGenerator = "cacheKeyGenerator")
         * public String getTokenByGsid(String gsid)
         */
        //SsoCache和BasicDataCache进行过期时间配置
        redisCacheConfigurationMap["menuCache"] = getRedisCacheConfigurationWithTtl(24 * 60 * 60)
        redisCacheConfigurationMap["BasicDataCache"] = getRedisCacheConfigurationWithTtl(30 * 60)
        return redisCacheConfigurationMap
    }

    private fun getRedisCacheConfigurationWithTtl(seconds: Int): RedisCacheConfiguration {
        var redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofSeconds(seconds.toLong())) //key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer())) //value序列化方式;
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
        return redisCacheConfiguration
    }

    @Bean(name = ["cacheKeyGenerator"])
    open fun cacheKeyGenerator(): KeyGenerator? {
        return KeyGenerator { target, method, params ->
            val sb = StringBuffer()
            sb.append(target.javaClass.name)
            sb.append(method.getName())
            for (obj in params) {
                sb.append(obj.toString())
            }
            sb.toString()
        }
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    open fun redisTemplate(lettuceConnectionFactory: LettuceConnectionFactory?): RedisTemplate<String, Any>? {
        // 设置序列化
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.keySerializer = keySerializer()
        redisTemplate.hashKeySerializer = keySerializer()
        //        redisTemplate.setHashValueSerializer(valueSerializer());
//        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.connectionFactory = lettuceConnectionFactory
        return redisTemplate
    }
}
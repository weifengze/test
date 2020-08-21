package com.zicms.framework.redis

import org.slf4j.LoggerFactory
import org.springframework.data.geo.Metric
import org.springframework.data.geo.Point
import org.springframework.data.geo.format.PointFormatter
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Service
class RedisServiceImpl : RedisService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Resource
    private val redisTemplates: StringRedisTemplate? = null

    override fun remove(vararg keys: String) {
        for (key in keys) {
            this.remove(key)
        }
    }

    override fun removePattern(pattern: String) {
        val keys = this.redisTemplates?.keys(pattern)
        if (keys!!.size > 0) {
            this.redisTemplates?.delete(keys)
        }
    }

    override fun remove(key: String) {
        if (this.exists(key)) {
            this.redisTemplates?.delete(key)
        }
    }

    override fun exists(key: String): Boolean {
        return this.redisTemplates?.hasKey(key)!!
    }

    override fun get(key: String): String {
        val opsForValue = this.redisTemplates?.opsForValue()
        return opsForValue?.get(key)!!
    }

    override fun set(key: String, value: String): Boolean {
        var result = false
        try {
            val opsForValue = this.redisTemplates?.opsForValue()
            opsForValue?.set(key, value)
            result = true;
        } catch (e: Exception) {
            logger.error(e.message)
        }
        return result;
    }

    override fun set(key: String, value: String, expireTime: Long, timeUnit: TimeUnit): Boolean {
        var result = false
        try {
            val opsForValue = this.redisTemplates?.opsForValue()
            opsForValue?.set(key, value)
            this.redisTemplates?.expire(key, expireTime, timeUnit)
            result = true;
        } catch (e: Exception) {
            logger.error(e.message)
        }
        return result
    }

    override fun range(key: String, start: Long, end: Long): List<String> {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.range(key, start, end)!!
    }

    override fun trim(key: String, start: Long, end: Long) {
        val opsForList = this.redisTemplates?.opsForList()
        opsForList?.trim(key, start, end)!!
    }

    override fun size(key: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.size(key)!!
    }

    override fun leftPush(key: String, value: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.leftPush(key, value)!!
    }

    override fun leftPushAll(key: String, vararg values: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.leftPushAll(key, *values)!!
    }

    override fun leftPushIfPresent(key: String, value: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.leftPushIfPresent(key, value)!!
    }

    override fun leftPush(key: String, pivot: String, value: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.leftPush(key, pivot, value)!!
    }

    override fun rightPush(key: String, value: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPush(key, value)!!
    }

    override fun rightPushAll(key: String, vararg values: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPushAll(key, *values)!!
    }

    override fun rightPushIfPresent(key: String, value: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPushIfPresent(key, value)!!
    }

    override fun rightPush(key: String, pivot: String, value: String): Long {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPush(key, pivot, value)!!
    }

    override fun set(key: String, index: Long, value: String) {
        val opsForList = this.redisTemplates?.opsForList()
        opsForList?.set(key, index, value)!!
    }

    override fun remove(key: String, count: Long, value: Any): Long? {
        val opsForList = this.redisTemplates?.opsForList()
        if (this.exists(key)) {
            opsForList?.remove(key, count, value)
        }
        return null;
    }

    override fun index(key: String, index: Long): String {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.index(key, index)!!
    }

    override fun leftPop(key: String): String? {
        val opsForList = this.redisTemplates?.opsForList()
        if (this.exists(key)) {
            return opsForList?.leftPop(key)!!
        }
        return null
    }

    override fun leftPop(key: String, timeout: Long, unit: TimeUnit): String {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.leftPop(key, timeout, unit)!!
    }

    override fun rightPop(key: String): String? {
        val opsForList = this.redisTemplates?.opsForList()
        if (this.exists(key)) {
            return opsForList?.rightPop(key)!!
        }
        return null
    }

    override fun rightPop(key: String, timeout: Long, unit: TimeUnit): String {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPop(key, timeout, unit)!!
    }

    override fun rightPopAndLeftPush(sourceKey: String, destinationKey: String): String {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPopAndLeftPush(sourceKey, destinationKey)!!
    }

    override fun rightPopAndLeftPush(sourceKey: String, destinationKey: String, timeout: Long, unit: TimeUnit): String {
        val opsForList = this.redisTemplates?.opsForList()
        return opsForList?.rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit)!!
    }

    override fun geoAdd(key: String, point: String, member: String): Long {
        return this.redisTemplates?.opsForGeo()?.add(key, PointFormatter.INSTANCE.convert(point), member)!!
    }

    override fun geoRemove(key: String, vararg members: String): Long {
        return this.redisTemplates?.opsForGeo()?.remove(key, *members)!!
    }

    override fun geoPos(key: String, vararg members: String): List<Point> {
        return this.redisTemplates?.opsForGeo()?.position(key, *members)!!
    }

    override fun geoHash(key: String, vararg members: String): List<String> {
        return this.redisTemplates?.opsForGeo()?.hash(key, *members)!!
    }

    override fun geoDist(key: String, member1: String, member2: String, metric: Metric): Double {
        return this.redisTemplates?.opsForGeo()?.distance(key, member1, member2, metric)?.value!!
    }
}
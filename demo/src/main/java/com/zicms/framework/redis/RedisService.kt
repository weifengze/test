package com.zicms.framework.redis

import org.springframework.data.geo.Metric
import org.springframework.data.geo.Point

import java.util.concurrent.TimeUnit


interface RedisService {
    /**
     * @Title: remove
     * @Description: TODO(批量删除对应的value)
     * @param @param keys    参数
     * @return void    返回类型
     * @throws
     */
    fun remove(vararg keys: String)

    /**
     * @Title: removePattern
     * @Description: TODO(批量删除key)
     * @param @param pattern    参数
     * @return void    返回类型
     * @throws
     */
    fun removePattern(pattern: String)

    /**
     * @Title: remove
     * @Description: TODO(删除对应的value)
     * @param @param key    参数
     * @return void    返回类型
     * @throws
     */
    fun remove(key: String)

    /**
     * @Title: exists
     * @Description: TODO(判断缓存中是否有对应的value)
     * @param @param key
     * @param @return    参数
     * @return boolean    返回类型
     * @throws
     */
    fun exists(key: String): Boolean

    /**
     * @Title: get
     * @Description: TODO(读取缓存)
     * @param @param key
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    operator fun get(key: String): String

    /**
     * @Title: set
     * @Description: TODO(写入缓存，永久)
     * @param @param key
     * @param @param value
     * @param @return    参数
     * @return boolean    返回类型
     * @throws
     */
    operator fun set(key: String, value: String): Boolean

    /**
     * @Title: set
     * @Description: TODO(写入缓存（有时间限制）)
     * @param @param key
     * @param @param value
     * @param @param expireTime
     * @param @param timeUnit
     * @param @return    参数
     * @return boolean    返回类型
     * @throws
     */
    operator fun set(key: String, value: String, expireTime: Long, timeUnit: TimeUnit): Boolean


    /**
     * @Title: range
     * @Description: TODO(获取指定key的范围内的value值的 list列表。 （0  -1）返回所有值列表 )
     * @param @param key
     * @param @param start
     * @param @param end
     * @param @return    参数
     * @return List<String>    返回类型
     * @throws
    </String> */
    fun range(key: String, start: Long, end: Long): List<String>

    /**
     * @Title: trim
     * @Description: TODO(保留key指定范围内的列表值。其它的都删除。)
     * @param @param key
     * @param @param start
     * @param @param end
     * @return void    返回类型
     * @throws
     */
    fun trim(key: String, start: Long, end: Long)

    /**
     * @Title: size
     * @Description: TODO(获取key 列表的长度)
     * @param @param key
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun size(key: String): Long

    /**
     * @Title: leftPush
     * @Description: TODO(写入缓存，是左面进入 先进后出)
     * @param @param key
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun leftPush(key: String, value: String): Long

    /**
     * @Title: leftPushAll
     * @Description: TODO(多个值写入缓存，是左面进入 先进后出)
     * @param @param key
     * @param @param values
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun leftPushAll(key: String, vararg values: String): Long

    /**
     * @Title: leftPushIfPresent
     * @Description: TODO(如果列表存在，则在列表左边插入值value)
     * @param @param key
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun leftPushIfPresent(key: String, value: String): Long

    /**
     * @Title: leftPush
     * @Description: TODO(在key的列表中指定的value左边（前面）插入一个新的value.如果 指定的value不存在则不插入任何值。)
     * @param @param key
     * @param @param pivot
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun leftPush(key: String, pivot: String, value: String): Long

    /**
     * @Title: rightPush
     * @Description: TODO(写入缓存，是右边面进入 先进先出)
     * @param @param key
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun rightPush(key: String, value: String): Long

    /**
     * @Title: rightPushAll
     * @Description: TODO(多个值写入缓存，是右边面进入 先进先出)
     * @param @param key
     * @param @param values
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun rightPushAll(key: String, vararg values: String): Long

    /**
     * @Title: rightPushIfPresent
     * @Description: TODO(如果列表存在，则在列表右边插入值value)
     * @param @param key
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun rightPushIfPresent(key: String, value: String): Long

    /**
     * @Title: rightPush
     * @Description: TODO(在key的列表中指定的value右边（前面）插入一个新的value.如果 指定的value不存在则不插入任何值。)
     * @param @param key
     * @param @param pivot
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun rightPush(key: String, pivot: String, value: String): Long

    /**
     * @Title: set
     * @Description: TODO(设置key列表中指定位置的值为value index不能大于列表长度。大于抛出异常,为负数则从右边开始计算)
     * @param @param key
     * @param @param index
     * @param @param value    参数
     * @return void    返回类型
     * @throws
     */
    operator fun set(key: String, index: Long, value: String)

    /**
     * @Title: remove
     * @Description: TODO(删除列表中第一个遇到的value值。count指定删除多少个,count为0则全部删除)
     * @param @param key
     * @param @param count (大于0从左边开始，等于0全部删除，小于0从右边开始)
     * @param @param value
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun remove(key: String, count: Long, value: Any): Long?

    /**
     * @Title: index
     * @Description: TODO(通过索引获取列表中的元素)
     * @param @param key
     * @param @param index  (大于0从左边开始，小于0从右边开始)
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun index(key: String, index: Long): String

    /**
     * @Title: leftPop
     * @Description: TODO(移除列表中的第一个值，并返回该值)
     * @param @param key
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun leftPop(key: String): String?

    /**
     * @Title: leftPop
     * @Description: TODO(移除列表中的第一个值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。)
     * @param @param key
     * @param @param timeout
     * @param @param unit
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun leftPop(key: String, timeout: Long, unit: TimeUnit): String

    /**
     * @Title: rightPop
     * @Description: TODO(移除列表中的最后一个值，并返回该值)
     * @param @param key
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun rightPop(key: String): String?

    /**
     * @Title: rightPop
     * @Description: TODO(移除列表中的最后一个值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。)
     * @param @param key
     * @param @param timeout
     * @param @param unit
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun rightPop(key: String, timeout: Long, unit: TimeUnit): String

    /**
     * @Title: rightPopAndLeftPush
     * @Description: TODO(从指定列表中从右边（尾部）移除第一个值，并将这个值从左边（头部）插入目标列表)
     * @param @param sourceKey
     * @param @param destinationKey
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun rightPopAndLeftPush(sourceKey: String, destinationKey: String): String

    /**
     * @Title: rightPopAndLeftPush
     * @Description: TODO(从指定列表中从右边（尾部）移除第一个值，并将这个值从左边（头部）插入目标列表，如果移除的列表中没有值，则一直阻塞指定的单位时间)
     * @param @param sourceKey
     * @param @param destinationKey
     * @param @param timeout
     * @param @param unit
     * @param @return    参数
     * @return String    返回类型
     * @throws
     */
    fun rightPopAndLeftPush(sourceKey: String, destinationKey: String, timeout: Long, unit: TimeUnit): String

    /**
     * @Title: geoAdd
     * @Description: TODO(添加geo)
     * @param @param key
     * @param @param point
     * @param @param member
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun geoAdd(key: String, point: String, member: String): Long

    /**
     * @Title: geoRemove
     * @Description: TODO(删除成员)
     * @param @param key
     * @param @param members
     * @param @return    参数
     * @return Long    返回类型
     * @throws
     */
    fun geoRemove(key: String, vararg members: String): Long

    /**
     * @Title: geoPos
     * @Description: TODO(查询地址的经纬度)
     * @param @param key
     * @param @param members
     * @param @return    参数
     * @return List<Point>    返回类型
     * @throws
    </Point> */
    fun geoPos(key: String, vararg members: String): List<Point>

    /**
     * @Title: geoHash
     * @Description: TODO(查询位置的geohash)
     * @param @param key
     * @param @param members
     * @param @return    参数
     * @return List<String>    返回类型
     * @throws
    </String> */
    fun geoHash(key: String, vararg members: String): List<String>

    /**
     * @Title: geoDist
     * @Description: TODO(查询2位置距离)
     * @param @param key
     * @param @param member1 成员1
     * @param @param member2 成员2
     * @param @param metric  单位
     * @param @return    参数
     * @return Double    返回类型
     * @throws
     */
    fun geoDist(key: String, member1: String, member2: String, metric: Metric): Double
}
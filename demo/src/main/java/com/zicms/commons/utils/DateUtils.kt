package com.zicms.commons.utils

import org.apache.commons.lang3.time.DateFormatUtils
import java.lang.management.ManagementFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils : org.apache.commons.lang3.time.DateUtils() {
    companion object {
        @JvmField
        var YYYY = "yyyy"

        @JvmField
        var YYYY_MM = "yyyy-MM"

        @JvmField
        var YYYY_MM_DD = "yyyy-MM-dd"

        @JvmField
        var YYYYMMDDHHMMSS = "yyyyMMddHHmmss"

        @JvmField
        var YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"

        private val parsePatterns = arrayOf(
                "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
                "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
                "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM")

        /**
         * 获取当前Date型日期
         *
         * @return Date() 当前日期
         */
        @JvmStatic
        fun getNowDate(): Date? {
            return Date()
        }

        /**
         * 获取当前日期, 默认格式为yyyy-MM-dd
         *
         * @return String
         */
        @JvmStatic
        fun getDate(): String? {
            return dateTimeNow(YYYY_MM_DD)
        }

        @JvmStatic
        fun getTime(): String {
            return dateTimeNow(YYYY_MM_DD_HH_MM_SS)
        }

        @JvmStatic
        fun dateTimeNow(): String {
            return dateTimeNow(YYYYMMDDHHMMSS)
        }

        @JvmStatic
        fun dateTimeNow(format: String?): String {
            return parseDateToStr(format, Date())
        }

        @JvmStatic
        fun dateTime(date: Date?): String {
            return parseDateToStr(YYYY_MM_DD, date)
        }

        @JvmStatic
        fun parseDateToStr(format: String?, date: Date?): String {
            return SimpleDateFormat(format).format(date)
        }

        @JvmStatic
        fun dateTime(format: String?, ts: String?): Date {
            return try {
                SimpleDateFormat(format).parse(ts)
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }

        /**
         * 日期路径 即年/月/日 如2018/08/08
         */
        @JvmStatic
        fun datePath(): String {
            val now = Date()
            return DateFormatUtils.format(now, "yyyy/MM/dd")
        }

        /**
         * 日期路径 即年/月/日 如20180808
         */
        @JvmStatic
        fun dateTime(): String {
            val now = Date()
            return DateFormatUtils.format(now, "yyyyMMdd")
        }

        /**
         * 日期型字符串转化为日期 格式
         */
        @JvmStatic
        fun parseDate(str: Any?): Date? {
            return if (str == null) {
                null
            } else try {
                // * 是Java中的 ...
                parseDate(str.toString(), *parsePatterns)
            } catch (e: ParseException) {
                null
            }
        }

        /**
         * 获取服务器启动时间
         */
        @JvmStatic
        fun getServerStartDate(): Date? {
            val time = ManagementFactory.getRuntimeMXBean().startTime
            return Date(time)
        }

        /**
         * 计算两个时间差
         */
        @JvmStatic
        fun getDatePoor(endDate: Date, nowDate: Date): String? {
            val nd = 1000 * 24 * 60 * 60.toLong()
            val nh = 1000 * 60 * 60.toLong()
            val nm = 1000 * 60.toLong()
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            val diff = endDate.time - nowDate.time
            // 计算差多少天
            val day = diff / nd
            // 计算差多少小时
            val hour = diff % nd / nh
            // 计算差多少分钟
            val min = diff % nd % nh / nm
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            return day.toString() + "天" + hour + "小时" + min + "分钟"
        }


    }
}
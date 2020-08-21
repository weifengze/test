package com.zicms.commons.constant

/**
 * 通用数据库映射Map数据
 */
class CommonMap {
    companion object {
        var javaTypeMap = hashMapOf(
                "tinyint" to "Integer",
                "smallint" to "Integer",
                "mediumint" to "Integer",
                "int" to "Integer",
                "number" to "Integer",
                "integer" to "integer",
                "bigint" to "Long",
                "float" to "Float",
                "double" to "Double",
                "decimal" to "BigDecimal",
                "bit" to "Boolean",
                "char" to "String",
                "varchar" to "String",
                "varchar2" to "String",
                "tinytext" to "String",
                "text" to "String",
                "mediumtext" to "String",
                "longtext" to "String",
                "time" to "Date",
                "date" to "Date",
                "datetime" to "Date",
                "timestamp" to "Date"
        )
    }
}
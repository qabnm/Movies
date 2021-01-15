package com.junliu.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:52
 * @des:首页配置信息
 */
data class ConfigureBean(
    val category: List<Category>,
    val columns: List<Column>,
    val isRs: Boolean,
    val version: Version
)

data class Category(
    val img: String,
    val key: String,
    val name: String
)

data class Column(
    val key: String,
    val name: String
)

data class Version(
    val content: String,
    val is_force: Boolean,
    val url: String,
    val version: String,
    val version_number: Int
)
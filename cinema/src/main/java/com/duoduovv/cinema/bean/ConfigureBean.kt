package com.duoduovv.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:52
 * @des:首页配置信息
 */
data class ConfigureBean(
    val columns: List<Column>?,
    val isRs: Boolean,
    val version: Version,
    val hotSearch: List<String>
)

data class Column(
    val id: String,
    val name: String
)

data class Version(
    val content: String,
    val is_force: Boolean,
    val url: String,
    val version: String,
    val version_number: Int
)
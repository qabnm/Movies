package com.duoduovv.cinema.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:52
 * @des:首页配置信息
 */
data class ConfigureBean(
    val columns: List<Column>?,
    val version: Version,
    val hotSearch: List<String>,
    val way:String
)

data class Column(
    val id: String,
    val name: String
)

data class Version(
    val content: String,
    @SerializedName("is_force")
    val isForce: String,
    val url: String,
    val version: String,
    @SerializedName("version_number")
    val versionNum: Int
)
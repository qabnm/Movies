package com.duoduovv.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/3/22 18:27
 * @des:檢查升級
 */
data class UpgradeBean(val version: VersionBean)

data class VersionBean(
    val content: String,
    val is_force: Int,
    val url: String,
    val version: String,
    val version_number: Int
)

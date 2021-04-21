package com.duoduovv.personal.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/3/22 18:27
 * @des:檢查升級
 */
data class UpgradeBean(val version: VersionBean)

data class VersionBean(
    val content: String,
    @SerializedName("is_force")
    val isForce: Int,
    val url: String,
    val version: String,
    @SerializedName("version_number")
    val versionNum: Int
)

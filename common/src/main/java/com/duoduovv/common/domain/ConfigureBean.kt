package com.duoduovv.common.domain

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:52
 * @des:首页配置信息
 */
@Keep
@Parcelize
data class ConfigureBean(
    val columns: List<Column>?,
    val version: Version?,
    val hotSearch: List<String>,
    val way: String,
    val ad:AdBean?
) : Parcelable

@Keep
@Parcelize
data class AdBean(
    val centerTop: AdValue?,
    val logout: AdValue?,
    val mainPageBanner: AdValue?,
    val movieDetailBanner: AdValue?,
    val searchBanner:AdValue?,
    val search: AdValue?,
    val splash: AdValue?,
    val videoAd: AdValue?,
    val insertAd:AdValue?,
    val libraryAd:AdValue?
) : Parcelable

@Keep
@Parcelize
data class AdValue(val type: Int, val value: String) : Parcelable

/**
 * 首页顶部栏目分类
 */
@Keep
@Parcelize
data class Column(
    val id: String,
    var name: String
) : Parcelable

/**
 * 版本更新
 */
@Keep
@Parcelize
data class Version(
    val content: String,
    @SerializedName("is_force")
    val isForce: String,
    val url: String,
    val version: String,
    @SerializedName("version_number")
    val versionNum: Int
) : Parcelable
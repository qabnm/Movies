package com.duoduovv.cinema.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author: jun.liu
 * @date: 2021/1/8 17:42
 * @des:搜索结果展示页面
 */
data class SearchResultBean(
    val result: List<SearchResultList>?
)

data class SearchResultList(
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("movie_items")
    val movieItems: List<MovieItem>?,
    @SerializedName("str_id")
    val strId: String,
    @SerializedName("vod_area_text")
    val vodArea: String,
    @SerializedName("vod_director")
    val vodDirector: String,
    @SerializedName("vod_lang")
    val vodLang: String,
    @SerializedName("vod_name")
    val vodName: String,
    @SerializedName("vod_year")
    val vodYear: String,
    @SerializedName("type_id_text")
    val typeText: String,
    @SerializedName("movie_flag")
    val movieFlag: String,
    val way: String
)

@Parcelize
data class MovieItem(
    var vid: String,
    var title: String,
    var isSelect: Boolean = false
) : Parcelable
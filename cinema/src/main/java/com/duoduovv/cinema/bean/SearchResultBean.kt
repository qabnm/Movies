package com.duoduovv.cinema.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jun.liu
 * @date: 2021/1/8 17:42
 * @des:搜索结果展示页面
 */
data class SearchResultBean(
    val paging: Paging,
    val result: List<SearchResultList>?
)

data class Paging(
    val page: Int,
    val pageSize: Int
)

data class SearchResultList(
    val cover_url: String,
    val hot: String,
    val is_copy: String,
    val is_end: String,
    val last_remark: String,
    val movie_items: List<MovieItem>?,
    val remark: String,
    val score: String,
    val str_id: String,
    val type_id: String,
    val vod_actor: String,
    val vod_area: String,
    val vod_area_text: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: Int,
    val vod_year: String,
    val type_id_text: String,
    val id: String,
    val movie_flag: String,
    val way:Int
)

@Parcelize
data class MovieItem(
    var vid: String,
    var title: String,
    var isSelect: Boolean = false
) : Parcelable
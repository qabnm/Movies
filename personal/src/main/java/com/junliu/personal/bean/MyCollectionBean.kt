package com.junliu.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/1/11 14:06
 * @des:我的收藏
 */
data class MyCollectionBean(
    val favorites: List<FavoriteBean>,
    val paging: Paging
)

data class FavoriteBean(
    val cover_url: String,
    val hot: Double,
    val id: Double,
    val is_copy: Double,
    val is_end: Double,
    val last_remark: String,
    val remark: String,
    val score: String,
    val str_id: String,
    val type_id: Double,
    val vod_actor: String,
    val vod_area: Double,
    val vod_area_text: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: Double,
    val vod_year: String,
    var isSelect:Boolean = false
)

data class Paging(
    val page: Double,
    val pageSize: Double
)
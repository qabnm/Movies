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
    val hot: String,
    val id: String,
    val is_copy: String,
    val is_end: String,
    val last_remark: String,
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
    val vod_number: String,
    val vod_year: String,
    var isSelect:Boolean = false
)

data class Paging(
    val page: Int,
    val pageSize: Int
)
package com.junliu.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/8 17:42
 * @des:搜索结果展示页面
 */
data class SearchResultBean(
    val paging: Paging,
    val result: List<SearchResultList>
)

data class Paging(
    val page: String,
    val pageSize: String
)

data class SearchResultList(
    val cover_url: String,
    val hot: String,
    val is_copy: String,
    val is_end: String,
    val last_remark: String,
    val movie_items: List<MovieItem>,
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
    val type_text:String
)

data class MovieItem(
    val key: String,
    val name: String
)
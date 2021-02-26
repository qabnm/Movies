package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/12 13:16
 * @des:榜单页面
 */
data class MovieRankBean(
    val ranks: List<RankList>?
)

data class RankList(
    val cover_url: String,
    val hot: String,
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
    val type_text:String
)
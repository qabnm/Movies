package com.junliu.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:06
 * @des:
 */
data class MainPageBean(
    val banner: List<Banner>,
    val playRecommends: List<PlayRecommend>,
    val recommends: List<Recommend>
)

data class Banner(
    val img: String,
    val title: String
)

data class PlayRecommend(
    val cover_url: String,
    val hot: String,
    val id: String,
    val score: String,
    val vod_actor: String,
    val vod_area: String,
    val vod_area_text: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: String,
    val vod_year: String
)

data class Recommend(
    val cover_url: String,
    val hot: String,
    val id: String,
    val score: String,
    val vod_actor: String,
    val vod_area: String,
    val vod_area_text: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: Int,
    val vod_year: String
)
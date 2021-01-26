package com.junliu.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/26 18:09
 * @des:影片详情bean类
 */
data class MovieDetailBean(
    val movie: MovieDetail,
    val movieItems: List<MovieItem>,
    val playInfo: PlayInfo,
    val recommends: List<DetailRecommend>,
    val way: String
)

data class MovieDetail(
    val cover_url: String,
    val hot: String,
    val is_end: String,
    val last_remark: String,
    val remark: String,
    val score: String,
    val str_id: String,
    val type_id: String,
    val vod_actor: String,
    val vod_area: String,
    val vod_area_text: String,
    val vod_blurb: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: String,
    val vod_year: String
)

data class MovieItem(
    val key: String,
    val title: String
)

data class PlayInfo(
    val lineList: List<Line>,
    val playNumber: String,
    val playUrls: List<PlayUrl>
)

data class DetailRecommend(
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
    val vod_year: String
)

data class Line(
    val line: String,
    val name: String
)

data class PlayUrl(
    val deft: String,
    val url: String
)
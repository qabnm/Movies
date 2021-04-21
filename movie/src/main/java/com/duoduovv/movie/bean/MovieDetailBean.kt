package com.duoduovv.movie.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/1/26 18:09
 * @des:影片详情bean类
 */
data class MovieDetailBean(
    val movie: MovieDetail,
    val lineList: List<LineList>,
    val movieItems: List<MovieItem>,
    val playUrls: List<PlayUrlsBean>?,
    val recommends: List<DetailRecommend>,
    val way: Int,
)

data class LineList(val line: String, val name: String)

data class PlayUrlsBean(val key: String, val name: String, val url: String)

data class MovieItem(val vid: String, val title: String, var isSelect: Boolean = false)

data class MovieDetail(
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("last_remark")
    val lastRemark: String,
    @SerializedName("str_id")
    val strId: String,
    @SerializedName("vod_actor")
    val vodActor: String,
    @SerializedName("vod_area_text")
    val vodArea: String,
    @SerializedName("vod_blurb")
    val vodDetail: String,
    @SerializedName("vod_director")
    val vodDirector: String,
    @SerializedName("vod_lang")
    val vodLang: String,
    @SerializedName("vod_name")
    val vodName: String,
    @SerializedName("vod_year")
    val vodYear: String,
    @SerializedName("movie_flag")
    val movieFlag: String?,
    val id: String
)

data class DetailRecommend(
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("last_remark")
    val lastRemark: String,
    @SerializedName("str_id")
    val strId: String,
    @SerializedName("vod_name")
    val vodName: String
)

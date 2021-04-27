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
    val way: String,
)

/**
 * 线路信息
 * @property line String
 * @property name String
 * @constructor
 */
data class LineList(val line: String, val name: String)

/**
 * 播放线路信息
 * @property key String
 * @property name String
 * @property url String
 * @constructor
 */
data class PlayUrlsBean(val key: String, val name: String, val url: String)

/**
 * 选集信息
 * @property vid String
 * @property title String
 * @property isSelect Boolean
 * @constructor
 */
data class MovieItem(val vid: String, val title: String, var isSelect: Boolean = false)

/**
 * 详情
 * @property coverUrl String
 * @property lastRemark String
 * @property strId String
 * @property vodActor String
 * @property vodArea String
 * @property vodDetail String
 * @property vodDirector String
 * @property vodLang String
 * @property vodName String
 * @property vodYear String
 * @property movieFlag String?
 * @property id String
 * @constructor
 */
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

/**
 * 视频 推荐
 * @property coverUrl String
 * @property lastRemark String
 * @property strId String
 * @property vodName String
 * @constructor
 */
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

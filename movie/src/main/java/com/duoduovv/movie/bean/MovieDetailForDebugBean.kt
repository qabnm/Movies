package com.duoduovv.movie.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/3/10 10:26
 * @des:审核版的影视详情
 */
data class MovieDetailForDebugBean(
    val movie: MovieForDebug,
    val movieDetail: MovieDetailForDebug
)

/**
 * 影视详情信息
 * @property coverUrl String
 * @property vodActor String
 * @property vodArea String
 * @property vodDetail String
 * @property vodDirector String
 * @property vodLang String
 * @property vodName String
 * @property vodYear String
 * @property typeText String
 * @constructor
 */
data class MovieForDebug(
    @SerializedName("cover_url")
    val coverUrl: String,
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
    @SerializedName("type_id_text")
    val typeText:String
)

data class MovieDetailForDebug(
    @SerializedName("actor_array")
    val actorArray: List<ActorArray>?,
    @SerializedName("stage_photo_array")
    val photoArray: List<StagePhotoArray>?
)

/**
 * 演员表
 * @property imgCover String
 * @property name String
 * @constructor
 */
data class ActorArray(
    @SerializedName("Img")
    val imgCover: String,
    @SerializedName("Name")
    val name:String
)

/**
 * 剧照
 * @property imgCover String
 * @constructor
 */
data class StagePhotoArray(
    @SerializedName("Img")
    val imgCover: String
)
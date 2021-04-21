package com.duoduovv.movie.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/1/12 13:16
 * @des:榜单页面
 */
data class MovieRankBean(
    val ranks: List<RankList>?
)

data class RankList(
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
    @SerializedName("vod_director")
    val vodDirector: String,
    @SerializedName("vod_lang")
    val vodLang: String,
    @SerializedName("vod_name")
    val vodName: String,
    @SerializedName("vod_year")
    val vodYear: String,
    @SerializedName("type_id_text")
    val typeText:String,
    val way:Int
)
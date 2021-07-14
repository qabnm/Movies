package com.duoduovv.movie.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/6/25 16:18
 * @des: 专题详情页
 */
data class SubjectDetailBean(@SerializedName("movies") val list: List<SubjectDetailListBean>)

data class SubjectDetailListBean(
    @SerializedName("cover_url")
    val coverUrl: String,
    val remark: String,
    @SerializedName("str_id")
    val strId: String,
    @SerializedName("vod_name")
    val vodName: String,
    val way: String,
    @SerializedName("vod_director")
    val vodDirector: String,
    @SerializedName("vod_lang")
    val vodLang: String,
    @SerializedName("vod_year")
    val vodYear: String,
    @SerializedName("type_id_text")
    val typeText:String,
    @SerializedName("vod_area_text")
    val vodArea: String,
    @SerializedName("vod_actor")
    val vodActor: String
)
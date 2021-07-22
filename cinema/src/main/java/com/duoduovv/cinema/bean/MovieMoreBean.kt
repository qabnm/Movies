package com.duoduovv.cinema.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/7/22 9:57
 * @des:
 */
data class MovieMoreBean(val movies: List<MovieMoreList>?)

data class MovieMoreList(
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("vod_name")
    val vodName: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("str_id")
    val strId: String,
    val way: String
)
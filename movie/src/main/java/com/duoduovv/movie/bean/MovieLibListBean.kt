package com.duoduovv.movie.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/1/21 15:24
 * @des:片库列表
 */
data class MovieLibListBean(
    val movies:List<MovieLibList>?
)

data class MovieLibList(
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("remark")
    val lastRemark:String,
    @SerializedName("str_id")
    val strId:String,
    @SerializedName("vod_name")
    val vodName:String,
    val way:String,
    val itemType:String?=null,
    var hasLoad:Boolean = false
)
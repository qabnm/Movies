package com.duoduovv.cinema.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/7/20 22:32
 * @des:
 */
data class CinemaListBean(
    val columns: List<ColumnBean>?
)

data class ColumnBean(
    val banner: List<BannerBean>?,
    val category: List<Category>?,
    @SerializedName("cover_url")
    val coverUrl: String?,
    @SerializedName("vod_name")
    val vodName: String?,
    val remark: String?,
    @SerializedName("str_id")
    val strId: String?,
    val titleName: String?,
    @SerializedName("itemType")
    val type: String?,
    val way: String?,
    val id:String?,
    var hasLoad:Boolean = false
)

data class BannerBean(
    val img: String,
    @SerializedName("jump_type")
    val jumpType: String,
    @SerializedName("movie_id")
    val movieId: String,
    val title: String,
    val type: String? = null
)

data class Category(
    val icon: String,
    val name: String,
    @SerializedName("type_spe_array")
    val typeSpeArray: TypeSpeBean
)

data class TypeSpeBean(@SerializedName("type_id") val typeId: String)
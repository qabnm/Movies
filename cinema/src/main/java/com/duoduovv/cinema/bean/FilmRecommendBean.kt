package com.duoduovv.cinema.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author: jun.liu
 * @date: 2021/1/18 10:19
 * @des:今日推荐的bean
 */
@Parcelize
data class FilmRecommendBean(
    @SerializedName("cover_url")
    val coverUrl: String,
    @SerializedName("vod_name")
    val vodName: String,
    @SerializedName("last_remark")
    val remark:String,
    @SerializedName("str_id")
    val strId:String,
    val way:String
):Parcelable
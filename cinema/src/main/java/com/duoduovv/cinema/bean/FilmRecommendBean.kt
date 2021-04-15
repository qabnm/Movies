package com.duoduovv.cinema.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: jun.liu
 * @date: 2021/1/18 10:19
 * @des:推荐的bean
 */
@Parcelize
data class FilmRecommendBean(
    val cover_url: String,
    val hot: String,
    val id: String,
    val score: String,
    val vod_actor: String,
    val vod_area: String,
    val vod_area_text: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: String,
    val vod_year: String,
    val remark:String,
    val str_id:String,
    val way:Int
):Parcelable
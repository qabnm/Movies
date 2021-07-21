package com.duoduovv.cinema.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:06
 * @des:
 */
data class MainPageBean(
    val banners: ArrayList<Banner>?,
    val category: List<Category>?,
    val playRecommends: List<FilmRecommendBean>?,//大家都在看
    val selectRecommends: List<FilmRecommendBean>?//今日推荐
)

/**
 * 首页banner
 */
data class Banner(
    val img: String,
    @SerializedName("jump_type")
    val jumpType: String,
    @SerializedName("movie_id")
    val movieId: String,
    val title: String,
    val type: String? = null
)

///**
// * 首页分类
// */
//data class Category(
//    val icon: String,
//    val name: String,
//    @SerializedName("type_spe_array")
//    val typeSpeArray: TypeSpeBean
//)
//
//data class TypeSpeBean(@SerializedName("type_id") val typeId: String)

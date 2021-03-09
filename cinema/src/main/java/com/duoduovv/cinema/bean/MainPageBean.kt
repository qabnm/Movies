package com.duoduovv.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:06
 * @des:
 */
data class MainPageBean(
    val banners: List<Banner>?,
    val category: List<Category>?,
    val playRecommends: List<FilmRecommendBean>?,//大家都在看
    val selectRecommends: List<FilmRecommendBean>?//今日推荐
)

data class Banner(
    val img: String,
    val jump_type: String,
    val movie_id: String,
    val title: String
)

data class Category(
    val icon: String,
    val name: String,
    val spe: String,
    val type_spe_array:TypeSpeBean
)

data class TypeSpeBean(val type_id:String)

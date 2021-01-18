package com.junliu.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:06
 * @des:
 */
data class MainPageBean(
    val banner: List<Banner>,
    val playRecommends: List<FilmRecommendBean>,
    val recommends: List<FilmRecommendBean>
)

data class Banner(
    val img: String,
    val title: String
)
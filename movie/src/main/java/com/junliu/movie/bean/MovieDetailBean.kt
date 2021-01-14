package com.junliu.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/13 18:09
 * @des:影片详情bean类
 */
data class MovieDetailBean(
    val detail: Detail,
    val recommend: List<Recommend>
)

data class Detail(
    val county: String,
    val detail: String,
    val fromName: String,
    val fromUrl: String,
    val movieName: String,
    val score: String,
    val subject: String,
    val totalLength: String,
    val update: String
)

data class Recommend(
    val coverUrl: String,
    val name: String,
    val score: String
)
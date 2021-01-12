package com.junliu.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/12 13:16
 * @des:榜单页面
 */
data class MovieRankBean(
    val coverUrl: String,
    val name: String,
    val year: String,
    val type: String,
    val county: String,
    val language: String,
    val score:String,
    val mainActor:String,
    val director:String
)
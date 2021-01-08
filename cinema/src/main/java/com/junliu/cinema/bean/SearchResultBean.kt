package com.junliu.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/8 17:42
 * @des:搜索结果展示页面
 */
data class SearchResultBean(
    val coverUrl: String,
    val title: String,
    val year: String,
    val type: String,
    val county: String,
    val language: String,
    val director: String
)

package com.duoduovv.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/25 14:59
 * @des:搜索结果分类
 */
data class SearchResultCategoryBean(
    val columns: List<SearchResultCategory>
)

data class SearchResultCategory(
    val id: String,
    var name: String
)

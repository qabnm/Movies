package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/25 13:45
 * @des:榜单分类
 */
data class MovieRankCategoryBean(
    val columns: List<RankCategory>
)

data class RankCategory(
    val id: String,
    var name: String
)

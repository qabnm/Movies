package com.junliu.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/20 18:28
 * @des: 片库顶部分类以及分类下的筛选条件
 */
data class MovieLibCategoryBean(
    val configs: List<Config>
)

data class Config(
    val filter: List<Filter>,
    val name: String,
    val v: Int
)

data class Filter(
    val array: List<Array>,
    val k: String,
    val name: String
)

data class Array(
    val name: String,
    val v: Any
)
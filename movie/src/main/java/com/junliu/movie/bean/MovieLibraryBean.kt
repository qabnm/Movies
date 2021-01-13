package com.junliu.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/13 9:50
 * @des:
 */
data class MovieLibraryBean(
    val movie: List<Movie>?,
    val type: List<Type>
)

data class Movie(
    val coverUrl: String,
    val movieName: String,
    val score: String
)

data class Type(
    val list: List<String>,
    val type: String
)
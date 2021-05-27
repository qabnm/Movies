package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/3/8 15:35
 * @des:播放信息
 */
data class MoviePlayInfoBean(
    val h5Url: String,
    val playUrls: List<PlayUrl>?,
    val request: Request,
    val type: String
)

data class PlayUrl(
    val deft: String,
    val num: String,
    val url: String,
    val vid: String
)

data class Request(
    val headers: List<Header>,
    val method: String,
    val url: String
)

data class Header(
    val name: String,
    val value: String
)
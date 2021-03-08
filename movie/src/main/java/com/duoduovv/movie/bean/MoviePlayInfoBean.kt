package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/3/8 15:35
 * @des:播放信息
 */
data class MoviePlayInfoBean(
    val playLine: String,
    val playUrls: List<PlayUrl>,
    val playVid: String
)

data class PlayUrl(
    val deft: String,
    val num: String,
    val url: String,
    val vid: String
)
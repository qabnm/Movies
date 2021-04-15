package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/11 17:55
 * @des:观看历史
 */
data class WatchHistoryBean(
    var coverUrl: String,
    var title: String,
    var type: String?,
    var movieId: String,
    var vid: String,
    var currentLength: Int,
    var totalLength: Int,
    var isSelect: Boolean = false
)

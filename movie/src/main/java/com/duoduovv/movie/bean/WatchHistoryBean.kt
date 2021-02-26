package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/1/11 17:55
 * @des:观看历史
 */
data class WatchHistoryBean(val coverUrl: String, val name: String, val where: String,var isSelect:Boolean = false)

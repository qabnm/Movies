package com.duoduovv.room.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: jun.liu
 * @date: 2021/1/21 15:08
 * @des:浏览历史数据
 */
@Entity
@Keep
data class VideoWatchHistoryBean(
    @PrimaryKey(autoGenerate = true)
    var number:Long = 0,
    var coverUrl:String,
    var title:String,
    var type:String?,
    var movieId:String,
    var vid:String,
    var currentLength:Int,
    var totalLength:Int,
    var vidTitle:String,
    var currentTime:Long,
    var isSelect:Boolean = false
)
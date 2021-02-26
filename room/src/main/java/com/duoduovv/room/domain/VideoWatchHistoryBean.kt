package com.duoduovv.room.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: jun.liu
 * @date: 2021/1/21 15:08
 * @des:浏览历史数据
 */
@Entity
data class VideoWatchHistoryBean(
    @PrimaryKey(autoGenerate = true)
    var number:Long = 0,
    var coverUrl:String,
    var playUrl:String,
    var title:String,
    var type:String,
    var videoId:String,
    var where:String,
    var currentLength:Long,
    var time:Long
)
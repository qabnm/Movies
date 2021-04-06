package com.duoduovv.room.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: jun.liu
 * @date: 2021/4/6 18:58
 * @des:收藏
 */
@Entity
data class CollectionBean(
    @PrimaryKey(autoGenerate = true)
    var number: Long = 0,
    var coverUrl:String,
    var strId:String,
    var movieId:String,
    var lastRemark:String,
    var actor:String,
    var direcotor:String,
    var movieName:String,
    var lang:String,
    var isCollect:Boolean,
    var collectionTime:Long
)
package com.junliu.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junliu.room.domain.VideoWatchHistoryBean

/**
 * @author: jun.liu
 * @date: 2021/1/21 17:09
 * @des:创建数据库
 */
@Database(entities = [VideoWatchHistoryBean::class], version = 1)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun history(): HistoryDao
}
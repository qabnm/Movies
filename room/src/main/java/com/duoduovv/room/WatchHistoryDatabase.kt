package com.duoduovv.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duoduovv.room.domain.VideoWatchHistoryBean

/**
 * @author: jun.liu
 * @date: 2021/1/21 17:09
 * @des:创建数据库
 */
@Database(entities = [VideoWatchHistoryBean::class], version = 1,exportSchema = false)
abstract class WatchHistoryDatabase : RoomDatabase() {
    abstract fun history(): HistoryDao

    companion object {
        @Volatile
        private var instance: WatchHistoryDatabase? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, WatchHistoryDatabase::class.java, "watchHistory")
                .build().also { instance = it }
        }
    }
}
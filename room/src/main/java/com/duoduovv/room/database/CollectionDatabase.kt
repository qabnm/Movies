package com.duoduovv.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duoduovv.room.dao.CollectionDao
import com.duoduovv.room.domain.CollectionBean

/**
 * @author: jun.liu
 * @date: 2021/4/6 19:30
 * @des:创建数据库
 */
@Database(entities = [CollectionBean::class], version = 1, exportSchema = false)
abstract class CollectionDatabase : RoomDatabase() {
    abstract fun collection(): CollectionDao

    companion object {
        private var instance: CollectionDatabase? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, CollectionDatabase::class.java, "collection")
                .build().also { instance = it }
        }
    }
}
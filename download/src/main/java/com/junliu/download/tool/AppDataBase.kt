package com.junliu.download.tool

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junliu.download.download.DownloadDao
import com.junliu.download.download.DownloadInfo

/**
 * @author: jun.liu
 * @date: 2020/12/19 17:29
 * @des:数据库的持有者
 */
@Database(entities = [DownloadInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun downloadDao(): DownloadDao
}
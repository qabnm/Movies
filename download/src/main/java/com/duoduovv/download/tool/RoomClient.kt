package com.duoduovv.download.tool

import android.content.Context
import androidx.room.Room

/**
 * @author: jun.liu
 * @date: 2020/12/20 17:31
 * @des:构建AppDataBase
 */
class RoomClient private constructor() {
    private val name = "download.db"
    fun getDataBase(context: Context) =
        Room.databaseBuilder(context, AppDataBase::class.java, name).build()

    companion object {
        val instance: RoomClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RoomClient()
        }
    }
}


package com.duoduovv.download.download

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.duoduovv.download.tool.Converters
import java.io.Serializable

/**
 * @author: jun.liu
 * @date: 2020/12/21 13:25
 * @des:保存下载数据的数据类
 */
@Entity
@TypeConverters(Converters::class)
data class DownloadInfo(
    @PrimaryKey
    var url: String = "",
    var path: String? = null,
    var data: Serializable? = null,//跟下载相关的数据信息
    var fileName: String? = null,
    var contentLength: Long = -1,
    var currentLength: Long = 0,
    var status: Int = NONE,
    var lastRefreshTime: Long = 0
) {
    companion object Status {
        const val NONE = 0  //无状态
        const val WAITING = 1 //等待中
        const val LOADING = 2 //下载中
        const val PAUSE = 3 //暂停
        const val ERROR = 4 //错误
        const val DONE = 5 //完成
    }

    /**
     * 重置任务
     */
    fun reset() {
        currentLength = 0
        contentLength = -1
        status = NONE
        lastRefreshTime = 0
    }
}
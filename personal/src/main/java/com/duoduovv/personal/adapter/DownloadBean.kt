package com.duoduovv.personal.adapter

import androidx.annotation.Keep
import java.io.Serializable

/**
 * @author: jun.liu
 * @date: 2021/3/24 17:13
 * @des:
 */
@Keep
data class DownloadBean(val downloadUrl:String,val process: Int):Serializable
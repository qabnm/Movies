package com.duoduovv.personal.listener

import android.net.Uri

/**
 * @author: jun.liu
 * @date: 2021/2/20 13:22
 * @des:
 */
interface ITakePhotoResult {
    fun takePhotoResult(uri: Uri?)
    fun takePhotoResult(path:String?)
}
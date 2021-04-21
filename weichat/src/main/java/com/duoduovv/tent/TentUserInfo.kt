package com.duoduovv.tent

import androidx.annotation.Keep

/**
 * @author: jun.liu
 * @date: 2021/2/18 15:08
 * @des:QQ登录获取的用户信息
 */
@Keep
data class TentUserInfo(
    val ret: String,
    val msg: String,
    val nickName: String,
    val sex: String,
    val headerUrl: String,
    var openId: String
)
package com.junliu.tent

/**
 * @author: jun.liu
 * @date: 2021/2/18 15:08
 * @des:QQ登录获取的用户信息
 */
data class TentUserInfo(
    val ret: Int,
    val msg: String,
    val nickName: String,
    val sex: String,
    val headerUrl: String
)
package com.junliu.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/2/25 14:29
 * @des:
 */
data class AccessTokenValidBean(
    val errcode: Int,
    val errmsg: String,
    var accessToken:String="",
    var openId:String=""
)

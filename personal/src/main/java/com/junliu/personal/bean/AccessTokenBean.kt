package com.junliu.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/2/25 13:36
 * @des:获取微信accessToken
 */
data class AccessTokenBean(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val openid: String,
    val scope: String
)

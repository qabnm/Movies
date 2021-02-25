package com.junliu.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/2/25 14:09
 * @des:刷新token
 */
data class RefreshTokenBean(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val openid: String,
    val scope: String
)

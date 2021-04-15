package com.duoduovv.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/2/25 14:43
 * @des:获取用户信息
 */
data class WeiChatUserInfoBean(
    val openid: String,
    val nickname: String,
    val sex: Int,
    val province: String?,
    val city: String?,
    val country: String?,
    val headimgurl: String?,
    val unionid: String
)

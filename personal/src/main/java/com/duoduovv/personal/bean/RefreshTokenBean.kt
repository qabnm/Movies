package com.duoduovv.personal.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/2/25 14:09
 * @des:刷新token
 */
data class RefreshTokenBean(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("openid")
    val openId: String,
    val scope: String
)

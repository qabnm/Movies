package com.duoduovv.personal.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/2/25 14:43
 * @des:获取用户信息
 */
data class WeiChatUserInfoBean(
    @SerializedName("openid")
    val openId: String,
    @SerializedName("nickname")
    val nickName: String,
    val sex: Int,
    val province: String?,
    val city: String?,
    val country: String?,
    @SerializedName("headimgurl")
    val imgUrl: String?,
    @SerializedName("unionid")
    val unionId:String
)

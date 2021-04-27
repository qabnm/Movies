package com.duoduovv.personal.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/2/25 14:29
 * @des:
 */
data class AccessTokenValidBean(
    @SerializedName("errcode")
    val errCode: Int,
    @SerializedName("errmsg")
    val errMsg: String,
    var accessToken:String="",
    var openId:String=""
)

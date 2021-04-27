package com.duoduovv.personal.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/2/24 18:05
 * @des:用户信息
 */
data class UserInfoBean(
    val user: User
)

data class User(
    val area: String,
    @SerializedName("cellphone")
    val cellPhone: String,
    val city: String,
    val country: String,
    @SerializedName("created_at")
    val createdTime: String,
    @SerializedName("img")
    val imgUrl: String,
    @SerializedName("nick")
    val nickName: String,
    val province: String,
    val sex: String,
    val uid: String
)
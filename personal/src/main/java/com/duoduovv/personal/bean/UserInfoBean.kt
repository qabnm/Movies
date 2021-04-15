package com.duoduovv.personal.bean

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
    val cellphone: String,
    val city: String,
    val country: String,
    val created_at: String,
    val img: String,
    val nick: String,
    val province: String,
    val sex: Int,
    val uid: String
)
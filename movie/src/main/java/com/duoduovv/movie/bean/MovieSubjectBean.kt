package com.duoduovv.movie.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: jun.liu
 * @date: 2021/6/25 11:03
 * @des:专题页
 */
data class MovieSubjectBean(@SerializedName("records") val subject: List<SubjectListBean>?)

data class SubjectListBean(
    @SerializedName("id")
    val subjectId: String,
    @SerializedName("image_url")
    val coverUrl: String,
    val title: String,
    @SerializedName("blurb")
    val des:String?
)

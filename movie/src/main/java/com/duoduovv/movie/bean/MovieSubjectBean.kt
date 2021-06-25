package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/6/25 11:03
 * @des:专题页
 */
data class MovieSubjectBean(val subject: List<SubjectListBean>)

data class SubjectListBean(val subjectId: String, val coverUrl: String, val title: String)

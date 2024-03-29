package com.duoduovv.movie.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author: jun.liu
 * @date: 2021/1/20 18:28
 * @des: 片库顶部分类以及分类下的筛选条件
 */
data class MovieLibCategoryBean(
    val configs: List<Config>
)

@Parcelize
data class Config(
    val filter: List<Filter>,
    val name: String,
    val key: String
):Parcelable

@Parcelize
data class Filter(
    val array: List<TypeListArray>,
    val key: String,
    val name: String
):Parcelable

@Parcelize
data class TypeListArray(
    val name: String,
    val key: String,
    var isSelect:Boolean = false
):Parcelable
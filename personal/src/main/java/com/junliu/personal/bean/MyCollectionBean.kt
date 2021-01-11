package com.junliu.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/1/11 14:06
 * @des:我的收藏
 */
data class MyCollectionBean(val coverUrl: String, val name: String, val where: String,var isSelect:Boolean = false)

package com.duoduovv.cinema.bean

/**
 * @author: jun.liu
 * @date: 2021/1/18 16:12
 * @des:首页数据三个接口的联合
 */
data class MainBean(
    val configureBean: ConfigureBean,
    val mainPageBean: MainPageBean,
    val mainRecommendBean: MainRecommendBean
)
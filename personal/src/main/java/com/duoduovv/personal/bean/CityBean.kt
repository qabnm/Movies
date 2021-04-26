package com.duoduovv.personal.bean

/**
 * @author: jun.liu
 * @date: 2021/4/26 11:34
 * @des:城市选择
 */
class CityBean : ArrayList<CityBeanItem>()

data class CityBeanItem(
    var city: List<City>,
    var name: String
)

data class City(
    var area: List<String>,
    var name: String
)
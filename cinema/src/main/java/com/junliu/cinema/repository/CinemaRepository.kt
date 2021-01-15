package com.junliu.cinema.repository

import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommendBean
import dc.android.bridge.net.BaseResponseData

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:00
 * @des:首页
 */
class CinemaRepository : CinemaApiRepository() {

    /**
     * 首页配置信息
     * @return BaseResponseData<ConfigureBean>
     */
    suspend fun configure() :BaseResponseData<ConfigureBean> = request {
        apiService.configure()
    }

    /**
     * 首页推荐
     * @param page Int
     * @return BaseResponseData<MainRecommendBean>
     */
    suspend fun mainRecommend(page:Int):BaseResponseData<MainRecommendBean> = request {
        apiService.mainRecommend(page)
    }

    /**
     * 首页
     * @return BaseResponseData<MainPageBean>
     */
    suspend fun mainPage():BaseResponseData<MainPageBean> = request {
        apiService.mainPage()
    }
}
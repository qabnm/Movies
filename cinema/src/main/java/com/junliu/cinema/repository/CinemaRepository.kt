package com.junliu.cinema.repository

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
    suspend fun configure() = request {
        apiService.configure()
    }

    /**
     * 首页推荐
     * @param page Int
     * @return BaseResponseData<MainRecommendBean>
     */
    suspend fun mainRecommend(page: Int, column: String) = request {
        apiService.mainRecommend(page, column)
    }

    /**
     * 首页
     * @return BaseResponseData<MainPageBean>
     */
    suspend fun mainPage(column: String) = request {
        apiService.mainPage(column = column)
    }
}
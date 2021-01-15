package com.junliu.cinema

import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommendBean
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:40
 * @des:
 */
interface CinemaApiService {

    /**
     * 获取配置信息
     */
    @GET("api/index")
    suspend fun configure(): BaseResponseData<ConfigureBean>

    /**
     * 首页
     * @return BaseResponseData<MainPageBean>
     */
    @GET("api/index")
    suspend fun mainPage(): BaseResponseData<MainPageBean>

    /**
     * 首页推荐
     * @param typeId String
     * @param page Int
     * @return BaseResponseData<MainRecommendBean>
     */
    @GET("")
    suspend fun mainRecommend(
        @Query("page") page: Int,
        @Query("type_id") typeId: String = "0"
    ): BaseResponseData<MainRecommendBean>
}
package com.duoduovv.cinema

import com.duoduovv.cinema.bean.*
import com.duoduovv.common.domain.ConfigureBean
import dc.android.bridge.net.BaseResponseData
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:40
 * @des:
 */
interface ICinemaApiService {

    /**
     * 获取配置信息
     */
    @GET("api/config")
    suspend fun configure(): BaseResponseData<ConfigureBean>

    /**
     * 首页
     * @return BaseResponseData<MainPageBean>
     */
    @GET("api/index")
    suspend fun mainPage(@Query("column") column: String): BaseResponseData<MainPageBean>

    /**
     * 首页推荐
     * @param typeId String
     * @param page Int
     * @return BaseResponseData<MainRecommendBean>
     */
    @GET("api/index/recommend")
    suspend fun mainRecommend(
        @Query("page") page: Int,
        @Query("column") typeId: String
    ): BaseResponseData<MainRecommendBean>

    /**
     * 搜索结果页
     * @param keyWord String
     * @param page Int
     * @param column String
     * @return BaseResponseData<SearchResultBean>
     */
    @GET("api/search")
    suspend fun searchResultList(
        @Query("wd") keyWord: String,
        @Query("page") page: Int,
        @Query("column") column: String
    ): BaseResponseData<SearchResultBean>

    /**
     * 下载更新
     * @param url String
     * @return ResponseBody
     */
    @Streaming
    @GET
    suspend fun downloadFile(@Url url:String):ResponseBody
}
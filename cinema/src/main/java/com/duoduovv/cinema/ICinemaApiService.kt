package com.duoduovv.cinema

import com.duoduovv.cinema.bean.CinemaListBean
import com.duoduovv.cinema.bean.MovieMoreBean
import com.duoduovv.cinema.bean.SearchResultBean
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
     * 首页栏目列表
     * @param page Int
     * @param typeId String
     * @return BaseResponseData<CinemaListBean>
     */
    @GET("api/v2/index")
    suspend fun cinemaList(
        @Query("page") page: Int,
        @Query("column") typeId: String
    ): BaseResponseData<CinemaListBean>

    /**
     * 不同分类点击更多
     * @param id String
     * @param page Int
     * @return BaseResponseData<MovieMoreBean>
     */
    @GET("api/v2/index/more")
    suspend fun movieMoreList(
        @Query("id") id: String,
        @Query("page") page: Int
    ): BaseResponseData<MovieMoreBean>

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
    suspend fun downloadFile(@Url url: String): ResponseBody
}
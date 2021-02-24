package com.junliu.movie

import com.junliu.movie.bean.*
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * @author: jun.liu
 * @date: 2021/1/20 18:31
 * @des:
 */
interface IMovieApiService {
    /**
     * 片库获取分类以及每个分类下的筛选条件
     * @return BaseResponseData<MovieLibCategoryBean>
     */
    @GET("api/store/config")
    suspend fun movieLibCategory(): BaseResponseData<MovieLibCategoryBean>

    /**
     * 片库列表
     * @param map HashMap<String, Any> 所有的筛选条件 如地区 时间
     * @param page Int 分页
     * @param typeId String 分类id
     * @return BaseResponseData<MovieLibListBean>
     */
    @GET("api/store/query")
    suspend fun movieLibList(
        @QueryMap map: HashMap<String, Any>,
        @Query("page") page: Int,
        @Query("type_id") typeId: String
    ): BaseResponseData<MovieLibListBean>

    /**
     * 榜单分类
     */
    @GET("api/config")
    suspend fun movieRankCategory(): BaseResponseData<MovieRankCategoryBean>

    /**
     * 电影榜单页面
     * @param column String
     * @return BaseResponseData<MovieRankBean>
     */
    @GET("api/ranking")
    suspend fun movieRankList(@Query("column") column: String): BaseResponseData<MovieRankBean>

    /**
     * 视频详情
     */
    @GET("vod")
    suspend fun movieDetail(
        @Query("id") id: String,
        @Query("num") num: String
    ): BaseResponseData<MovieDetailBean>
}
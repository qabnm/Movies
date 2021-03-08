package com.duoduovv.movie

import com.duoduovv.movie.bean.*
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.*

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
        @Query("vid") vid: String = ""
    ): BaseResponseData<MovieDetailBean>

    /**
     * 获取播放信息
     * @param vid String
     * @param id String
     * @return BaseResponseData<MoviePlayInfoBean>
     */
    @GET("vod/get_play")
    suspend fun moviePlayInfo(
        @Query("vid") vid: String,
        @Query("id") id: String
    ): BaseResponseData<MoviePlayInfoBean>

    /**
     * 添加收藏
     * @param movieId String
     * @return BaseResponseData<Any>
     */
    @FormUrlEncoded
    @POST("api/user/favorite")
    suspend fun addCollection(@Field("movie_id") movieId: String): BaseResponseData<Any>

    /**
     * 删除收藏
     * @param movieId String
     * @return BaseResponseData<Any>
     */
    @DELETE("api/user/favorite_1614157043983")
    suspend fun deleteCollection(@Query("movie_id") movieId: String): BaseResponseData<Any>
}
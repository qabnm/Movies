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
     * 电影榜单页面
     * @param column String
     * @return BaseResponseData<MovieRankBean>
     */
    @GET("api/ranking")
    suspend fun movieRankList(@Query("column") column: String): BaseResponseData<MovieRankBean>

    /**
     * 视频详情
     */
    @GET("api/v2/vod")
    suspend fun movieDetail(
        @Query("id") id: String,
        @Query("vid") vid: String = ""
    ): BaseResponseData<MovieDetailBean>

    /**
     * 审核版的影视详情
     * @param movieId String
     * @return BaseResponseData<MovieDetailForDebugBean>
     */
    @GET("api/vod/detail")
    suspend fun movieDetailForDebug(@Query("id") movieId: String): BaseResponseData<MovieDetailForDebugBean>

    /**
     * 获取播放信息
     * @param vid String
     * @param id String
     * @return BaseResponseData<MoviePlayInfoBean>
     */
    @GET("api/v2/vod/get_play")
    suspend fun moviePlayInfo(
        @Query("vid") vid: String,
        @Query("id") id: String,
        @Query("line") line: String,
        @Query("js") js: String
    ): BaseResponseData<MoviePlayInfoBean>

    /**
     * 解析播放地址
     * @param vid String
     * @param id String
     * @param line String
     * @return BaseResponseData<PlayUrl>
     */
    @FormUrlEncoded
    @POST("api/v2/vod/jx")
    suspend fun analysisPlayUrl(
        @Field("vid") vid: String,
        @Field("id") id: String,
        @Field("line") line: String,
        @Field("content") content: String
    ): BaseResponseData<JxPlayUrlBean>

    /**
     * 举报
     * @param content String
     * @param movieId String
     * @return BaseResponseData<Any>
     */
    @FormUrlEncoded
    @POST("api/report")
    suspend fun report(
        @Field("content") content: String,
        @Field("movie_id") movieId: String
    ): BaseResponseData<Any>

    /**
     * 视频播放失败的接口
     * @param pid String
     * @param status String
     * @param message String
     * @return BaseResponseData<Any>
     */
    @FormUrlEncoded
    @POST("api/v2/vod/play_err")
    suspend fun playError(
        @Field("pid") pid: String,
        @Field("status") status: String,
        @Field("message") message: String
    ): BaseResponseData<Any>

    /**
     * 专题列表页
     */
    @GET("api/special")
    suspend fun subjectList(@Query("page") page: Int): BaseResponseData<MovieSubjectBean>

    /**
     * 专题详情
     * @param subjectId String
     * @return BaseResponseData<SubjectDetailBean>
     */
    @GET("api/special/detail")
    suspend fun subjectDetail(@Query("id") subjectId: String): BaseResponseData<SubjectDetailBean>
}
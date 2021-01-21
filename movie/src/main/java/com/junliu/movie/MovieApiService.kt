package com.junliu.movie

import com.junliu.movie.bean.MovieLibCategoryBean
import com.junliu.movie.bean.MovieLibListBean
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * @author: jun.liu
 * @date: 2021/1/20 18:31
 * @des:
 */
interface MovieApiService {
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
    suspend fun movieLibList(
        @QueryMap map: HashMap<String, Any>,
        @Query("page") page: Int,
        @Query("type_id") typeId: String
    ): BaseResponseData<MovieLibListBean>
}
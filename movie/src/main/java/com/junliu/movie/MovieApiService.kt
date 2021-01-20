package com.junliu.movie

import com.junliu.movie.bean.MovieLibCategoryBean
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET

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
}
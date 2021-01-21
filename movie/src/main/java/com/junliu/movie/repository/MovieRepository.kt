package com.junliu.movie.repository

/**
 * @author: jun.liu
 * @date: 2021/1/21 9:46
 * @des:
 */
class MovieRepository :MovieApiRepository() {
    /**
     * 片库获取分类及分类的筛选信息
     * @return BaseResponseData<MovieLibCategoryBean>
     */
    suspend fun movieLibCategory() = request {
        apiService.movieLibCategory()
    }
}
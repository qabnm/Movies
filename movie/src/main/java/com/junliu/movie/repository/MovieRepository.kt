package com.junliu.movie.repository

/**
 * @author: jun.liu
 * @date: 2021/1/21 9:46
 * @des:
 */
class MovieRepository : MovieApiRepository() {
    /**
     * 片库获取分类及分类的筛选信息
     * @return BaseResponseData<MovieLibCategoryBean>
     */
    suspend fun movieLibCategory() = request {
        apiService.movieLibCategory()
    }

    /**
     * 片库分类列表
     * @param map HashMap<String, Any> 片库筛选条件 如地区 时间
     * @param page Int 分页
     * @param typeId String  大分类id 电影 电视剧
     * @return BaseResponseData<MovieLibListBean>
     */
    suspend fun movieLibList(map: HashMap<String, Any>, page: Int, typeId: String) = request {
        apiService.movieLibList(map, page, typeId)
    }
}
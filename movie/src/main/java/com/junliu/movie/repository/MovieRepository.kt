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

    /**
     * 电影榜单排名分类
     * @return BaseResponseData<MovieRankCategoryBean>
     */
    suspend fun movieRankCategory() = request { apiService.movieRankCategory() }

    /**
     * 榜单列表
     * @param column String
     * @return BaseResponseData<MovieRankBean>
     */
    suspend fun movieRankList(column: String) = request {
        apiService.movieRankList(column)
    }

    /**
     * 电影详情
     * @param id String 电影ID
     * @param num String 播放级数
     * @return BaseResponseData<MovieDetailBean>
     */
    suspend fun movieDetail(id: String, num: String) = request {
        apiService.movieDetail(id = id, num = num)
    }

    /**
     * 添加收藏
     * @param movieId String
     * @return BaseResponseData<Any>
     */
    suspend fun addCollection(movieId: String) = request {
        apiService.addCollection(movieId)
    }
}
package com.duoduovv.movie.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    suspend fun movieDetail(id: String, vid: String = "") = request {
        apiService.movieDetail(id = id, vid = vid)
    }

    /**
     * 审核版的影视详情
     * @param movieId String
     * @return BaseResponseData<MovieDetailForDebugBean>
     */
    suspend fun movieDetailForDebug(movieId: String) = request {
        apiService.movieDetailForDebug(movieId = movieId)
    }

    /**
     * 获取播放信息
     * @param vid String
     * @param id String
     * @param line String
     * @return BaseResponseData<MoviePlayInfoBean>
     */
    suspend fun moviePlayInfo(vid: String, id: String, line: String,js:String) = request {
        apiService.moviePlayInfo(vid, id, line,js)
    }

    /**
     * 举报
     * @param content String
     * @param movieId String
     * @return BaseResponseData<Any>
     */
    suspend fun report(content: String, movieId: String) = request {
        apiService.report(content, movieId)
    }

    /**
     * 解析播放地址
     * @param vid String
     * @param movieId String
     * @param line String
     * @return BaseResponseData<PlayUrl>
     */
    suspend fun analysisPlayUrl(vid: String, movieId: String, line: String, content: String) =
        request {
            apiService.analysisPlayUrl(vid = vid, id = movieId, line = line, content)
        }

    /**
     * 如果是第三方的 就去解析三方地址 get请求
     * @param url String
     * @param headers Map<String, String>
     * @return Any
     */
    suspend fun jxUrlForGEet(url: String, headers: Map<String, String>) =
        withContext(Dispatchers.IO) {
            jxApiService.jxUrlForGEet(url, headers)
        }

    /**
     * 解析三方源 post请求
     * @param url String
     * @param headers Map<String, String>
     * @param maps Map<String, String>
     * @return ResponseBody
     */
    suspend fun jxUrlForPost(url: String, headers: Map<String, String>, maps: Map<String, String>) =
        withContext(Dispatchers.IO) {
            jxApiService.jxUrlForPost(url, headers, maps)
        }

    /**
     * 视频播放失败
     * @param vid String 点播ID
     * @param url String 当前播放的url
     * @param message String 错误信息
     * @return BaseResponseData<Any>
     */
    suspend fun playError(vid: String, url: String, message: String) = request {
        apiService.playError(vid = vid, url = url, message = message)
    }
}
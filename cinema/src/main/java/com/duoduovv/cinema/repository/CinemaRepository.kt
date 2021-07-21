package com.duoduovv.cinema.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:00
 * @des:首页
 */
class CinemaRepository : CinemaApiRepository() {

    /**
     * 首页栏目列表
     * @param page Int
     * @param column String
     * @return BaseResponseData<CinemaListBean>
     */
    suspend fun cinemaList(page: Int, column: String) = request {
        apiService.cinemaList(page, column)
    }

    /**
     * 首页推荐
     * @param page Int
     * @return BaseResponseData<MainRecommendBean>
     */
    suspend fun mainRecommend(page: Int, column: String) = request {
        apiService.mainRecommend(page, column)
    }

    /**
     * 首页
     * @return BaseResponseData<MainPageBean>
     */
    suspend fun mainPage(column: String) = request {
        apiService.mainPage(column = column)
    }

    /**
     * 搜索结果
     * @param keyWord String
     * @param page Int
     * @param column String
     * @return BaseResponseData<SearchResultBean>
     */
    suspend fun searchResult(keyWord: String, page: Int, column: String) = request {
        apiService.searchResultList(keyWord, page, column)
    }

    /**
     * app下载更新
     * @param url String
     * @return ResponseBody
     */
    suspend fun downloadFile(url: String) = withContext(Dispatchers.IO) {
        apiService.downloadFile(url)
    }
}
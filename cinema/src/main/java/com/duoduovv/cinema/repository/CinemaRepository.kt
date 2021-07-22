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
     * 栏目点击查看更多
     * @param id String
     * @param page Int
     * @return BaseResponseData<MovieMoreBean>
     */
    suspend fun movieMoreList(id: String, page: Int) = request {
        apiService.movieMoreList(id, page)
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
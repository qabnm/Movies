package com.duoduovv.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.cinema.bean.SearchResultBean
import com.duoduovv.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/25 18:10
 * @des:搜索结果页
 */
class SearchResultViewModel : BaseViewModel() {
    private var searchResult: MutableLiveData<SearchResultBean> = MutableLiveData()
    fun getSearchResult() = searchResult
    private val repository = CinemaRepository()

    /**
     * 搜索结果
     * @param keyWord String
     * @param page Int
     * @param column String
     * @return Job
     */
    fun searchResult(keyWord: String, page: Int, column: String) = request {
        val result = repository.searchResult(keyWord, page, column)
        if (result.code == SUCCESS) searchResult.postValue(result.data)
    }
}
package com.duoduovv.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.cinema.bean.SearchResultList
import com.duoduovv.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/25 18:10
 * @des:搜索结果页
 */
class SearchResultViewModel : BaseViewModel() {
    private var searchResult: MutableLiveData<List<SearchResultList>> = MutableLiveData()
    fun getSearchResult() = searchResult
    private var noMoreData: MutableLiveData<String> = MutableLiveData()
    fun getNoMoreData() = noMoreData
    private val repository = CinemaRepository()
    private val dataList = ArrayList<SearchResultList>()

    /**
     * 搜索结果
     * @param keyWord String
     * @param page Int
     * @param column String
     * @return Job
     */
    fun searchResult(keyWord: String, page: Int, column: String) = request {
        val result = repository.searchResult(keyWord, page, column)
        if (isSuccess(result.code)){
            if (page == 1) dataList.clear()
            val list = result.data.result
            if (list?.isNotEmpty() == true){
                dataList.addAll(list)
                searchResult.postValue(dataList)
            }else{
                if (page == 1) {
                    searchResult.postValue(dataList)
                }else{
                    noMoreData.postValue(NO_MORE_DATA)
                }
            }
        }
    }
}
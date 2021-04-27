package com.duoduovv.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.cinema.bean.SearchResultCategoryBean
import com.duoduovv.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/25 15:03
 * @des:搜索结果顶部分类信息
 */
class SearchResultCategoryViewModel : BaseViewModel() {
    private var searchResultCategory: MutableLiveData<SearchResultCategoryBean> = MutableLiveData()
    fun getCategory() = searchResultCategory
    private val repository = CinemaRepository()

    /**
     * 搜索结果分类
     * @return Job
     */
    fun searchResultCategory() = request {
        val result = repository.searchResultCategory()
        if (result.code == SUCCESS) searchResultCategory.postValue(result.data)
    }
}
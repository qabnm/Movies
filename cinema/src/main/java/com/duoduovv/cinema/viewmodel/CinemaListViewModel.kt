package com.duoduovv.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.cinema.bean.FilmRecommendBean
import com.duoduovv.cinema.bean.MainBean
import com.duoduovv.cinema.bean.MainPageBean
import com.duoduovv.cinema.bean.MainRecommendBean
import com.duoduovv.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:33
 * @des:首页列表
 */
class CinemaListViewModel : BaseViewModel() {
    private var mainRecommend: MutableLiveData<ArrayList<FilmRecommendBean>> = MutableLiveData()
    private var mainBean: MutableLiveData<MainBean> = MutableLiveData()
    private var noMoreData: MutableLiveData<String> = MutableLiveData()
    private val repository = CinemaRepository()

    fun getMainRecommend() = mainRecommend
    fun getMain() = mainBean
    fun getNoMoreData() = noMoreData
    private val dataList = ArrayList<FilmRecommendBean>()

    /**
     * 合并首页三个请求接口
     * @param page Int
     */
    fun main(page: Int, column: String) = request {
        val result1: Deferred<BaseResponseData<MainPageBean>?> = async {
            try {
                repository.mainPage(column)
            } catch (e: Exception) {
                getException().value = e
                null
            }
        }
        val result2: Deferred<BaseResponseData<MainRecommendBean>?> = async {
            try {
                repository.mainRecommend(page, column)
            } catch (e: Exception) {
                getException().value = e
                null
            }
        }
        if (isSuccess(result1.await()?.code) && isSuccess(result2.await()?.code)) {
            dataList.clear()
            val list = result2.await()!!.data.recommends
            if (list?.isNotEmpty() == true) dataList.addAll(list)
            mainBean.postValue(MainBean(result1.await()!!.data, result2.await()!!.data))
        }
    }

    /**
     * 首页底部推荐
     * @param page Int
     * @return Job
     */
    fun mainRecommend(page: Int, column: String) = request {
        val result = repository.mainRecommend(page = page, column = column)
        if (isSuccess(result.code)) {
            val data = result.data.recommends
            if (data?.isNotEmpty() == true) {
                dataList.addAll(data)
                mainRecommend.postValue(dataList)
            } else {
                //没有更多数据了
                if (page != 1) {
                    noMoreData.postValue(NO_MORE_DATA)
                } else {
                    mainRecommend.postValue(dataList)
                }
            }
        }
    }
}
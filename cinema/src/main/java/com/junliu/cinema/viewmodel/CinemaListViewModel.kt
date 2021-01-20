package com.junliu.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.bean.MainBean
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommendBean
import com.junliu.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:33
 * @des:
 */
class CinemaListViewModel : BaseViewModel() {
    private var mainPageData: MutableLiveData<BaseResponseData<MainPageBean>> = MutableLiveData()
    private var mainRecommend: MutableLiveData<BaseResponseData<MainRecommendBean>> =
        MutableLiveData()
    private var mainBean: MutableLiveData<MainBean> = MutableLiveData()
    private val repository = CinemaRepository()

    fun getMainPage() = mainPageData
    fun getMainRecommend() = mainRecommend
    fun getMain() = mainBean

    /**
     * 合并首页三个请求接口
     * @param page Int
     */
    fun main(page: Int, column: String) = request {
        val result: Deferred<BaseResponseData<ConfigureBean>?> = async {
            try {
                repository.configure()
            } catch (e: Exception) {
                error.value = e
                null
            }
        }
        val result1: Deferred<BaseResponseData<MainPageBean>?> = async {
            try {
                repository.mainPage(column)
            } catch (e: Exception) {
                error.value = e
                null
            }
        }
        val result2: Deferred<BaseResponseData<MainRecommendBean>?> = async {
            try {
                repository.mainRecommend(page, column)
            } catch (e: Exception) {
                error.value = e
                null
            }
        }
        if (result.await()?.code == SUCCESS && result1.await()?.code == SUCCESS && result2.await()?.code == SUCCESS) {
            val bean =
                MainBean(result.await()!!.data, result1.await()!!.data, result2.await()!!.data)
            mainBean.postValue(bean)
        }
    }

    /**
     * 首页列表
     * @return Job
     */
    fun mainPage(column: String) = request {
        val result = repository.mainPage(column = column)
        if (result.code == SUCCESS) mainPageData.postValue(result)
    }

    /**
     * 首页底部推荐
     * @param page Int
     * @return Job
     */
    fun mainRecommend(page: Int, column: String) = request {
        val result = repository.mainRecommend(page = page, column = column)
        if (result.code == SUCCESS) mainRecommend.postValue(result)
    }
}
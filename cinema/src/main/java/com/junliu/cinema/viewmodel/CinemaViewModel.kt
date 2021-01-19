package com.junliu.cinema.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.bean.MainBean
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommendBean
import com.junliu.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:33
 * @des:
 */
class CinemaViewModel : BaseViewModel() {
    private var configure: MutableLiveData<BaseResponseData<ConfigureBean>> = MutableLiveData()
    private var mainPageData: MutableLiveData<BaseResponseData<MainPageBean>> = MutableLiveData()
    private var mainRecommend: MutableLiveData<BaseResponseData<MainRecommendBean>> =
        MutableLiveData()
    private var mainBean: MutableLiveData<MainBean> = MutableLiveData()
    private val repository = CinemaRepository()

    fun getConfigure() = configure
    fun getMainPage() = mainPageData
    fun getMainRecommend() = mainRecommend
    fun getMain() = mainBean

    /**
     * 合并首页三个请求接口
     * @param page Int
     */
    fun main(page: Int) = request {
        val result = async { repository.configure() }
        val result1 = async { repository.mainPage() }
        val result2 = async { repository.mainRecommend(page) }
        if (result.await().code == SUCCESS && result1.await().code == SUCCESS && result2.await().code == SUCCESS) {
            val bean = MainBean(result.await().data, result1.await().data, result2.await().data)
            mainBean.postValue(bean)
        }
    }

    fun hah(page: Int){
        viewModelScope.launch {
            val job1 = async { repository.configure() }
            val job2 = async { repository.mainPage() }
            val job3b = async { repository.mainRecommend(page) }
            try {
                val bean = MainBean(job1.await().data, job2.await().data,job3b.await().data)
                mainBean.postValue(bean)
            }catch (e:Exception){
                Log.e("e",e.toString())
            }
        }
    }

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (result.code == SUCCESS) configure.postValue(result)
    }

    /**
     * 首页列表
     * @return Job
     */
    fun mainPage() = request {
        val result = repository.mainPage()
        if (result.code == SUCCESS) mainPageData.postValue(result)
    }

    /**
     * 首页底部推荐
     * @param page Int
     * @return Job
     */
    fun mainRecommend(page: Int) = request {
        val result = repository.mainRecommend(page = page)
        if (result.code == SUCCESS) mainRecommend.postValue(result)
    }
}
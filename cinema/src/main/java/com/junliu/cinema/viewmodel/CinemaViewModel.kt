package com.junliu.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommendBean
import com.junliu.cinema.repository.CinemaRepository
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:33
 * @des:
 */
class CinemaViewModel : BaseViewModel() {
    private var configure:MutableLiveData<BaseResponseData<ConfigureBean>> = MutableLiveData()
    private var mainPageData :MutableLiveData<BaseResponseData<MainPageBean>> = MutableLiveData()
    private var mainRecommend : MutableLiveData<BaseResponseData<MainRecommendBean>> = MutableLiveData()
    private val repository = CinemaRepository()

    fun getConfigure() = configure
    fun getMainPage() = mainPageData
    fun getMainRecommend() = mainRecommend

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result =repository.configure()
        configure.postValue(result)
    }

    /**
     * 首页列表
     * @return Job
     */
    fun mainPage() = request {
        val result = repository.mainPage()
        mainPageData.postValue(result)
    }

    /**
     * 首页底部推荐
     * @param page Int
     * @return Job
     */
    fun mainRecommend(page:Int) = request {
        val result = repository.mainRecommend(page = page)
        mainRecommend.postValue(result)
    }
}
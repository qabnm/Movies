package com.junliu.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.bean.MainBean
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommendBean
import com.junliu.cinema.repository.CinemaRepository
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel
import kotlinx.coroutines.async

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:33
 * @des:
 */
class CinemaViewModel : BaseViewModel() {
    private var configure:MutableLiveData<BaseResponseData<ConfigureBean>> = MutableLiveData()
    private var mainPageData :MutableLiveData<BaseResponseData<MainPageBean>> = MutableLiveData()
    private var mainRecommend : MutableLiveData<BaseResponseData<MainRecommendBean>> = MutableLiveData()
    private var mainBean:MutableLiveData<MainBean> = MutableLiveData()
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
        val bean = MainBean(result.await().data, result1.await().data,result2.await().data)
        mainBean.postValue(bean)
    }
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